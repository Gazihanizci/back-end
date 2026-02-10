package com.example.finansapii.service;

import com.example.finansapii.dto.TaksitCreateRequest;
import com.example.finansapii.dto.TaksitResponse;
import com.example.finansapii.entity.*;
import com.example.finansapii.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
public class TaksitService {

    private final TaksitRepository taksitRepository;
    private final TaksitIslemLogRepository logRepository;
    private final UserRepository userRepository;
    private final KategoriRepository kategoriRepository;
    private final IslemRepository islemRepository;

    public TaksitService(
            TaksitRepository taksitRepository,
            TaksitIslemLogRepository logRepository,
            UserRepository userRepository,
            KategoriRepository kategoriRepository,
            IslemRepository islemRepository
    ) {
        this.taksitRepository = taksitRepository;
        this.logRepository = logRepository;
        this.userRepository = userRepository;
        this.kategoriRepository = kategoriRepository;
        this.islemRepository = islemRepository;
    }

    @Transactional
    public TaksitResponse create(Long kullaniciId, TaksitCreateRequest req) {
        User user = userRepository.findById(kullaniciId)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı"));

        Taksit t = new Taksit();
        t.setKullaniciId(kullaniciId);
        t.setAileId(user.getAileId());
        t.setTaksitBasligi(req.taksitBasligi().trim());
        t.setTutar(req.tutar());
        t.setParaBirimi("TL");
        t.setBaslangicTarihi(req.baslangicTarihi());
        t.setTaksitSayisi(req.taksitSayisi());
        t.setBittiMi(false);
        t.setAciklama(req.aciklama());

        Taksit saved = taksitRepository.save(t);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<TaksitResponse> my(Long kullaniciId) {
        return taksitRepository.findAllByKullaniciId(kullaniciId).stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * ✅ Günlük otomatik üretim:
     * - Bugün vadesi gelen taksit varsa -> islemler'e gider olarak ekler
     * - Kategori: "Diğer Giderler" (tip=GIDER)
     * - Aynı gün iki kez eklemeyi log ile engeller
     */
    @Transactional
    public int runDueInstallments(LocalDate today) {
        Kategori digerGider = kategoriRepository
                .findByKategoriAdAndTip("Diğer Giderler", "GIDER")
                .orElseThrow(() -> new IllegalArgumentException("Kategori bulunamadı: Diğer Giderler (GIDER)"));

        List<Taksit> active = taksitRepository.findAllByBittiMiFalse();

        int createdCount = 0;

        for (Taksit t : active) {
            int idx = installmentIndexIfDueToday(t, today); // 0..N-1 veya -1
            if (idx < 0) continue;

            LocalDate dueDate = dueDateForIndex(t.getBaslangicTarihi(), idx);

            // ✅ duplicate engeli
            if (logRepository.existsByTaksitIdAndVadeTarihi(t.getId(), dueDate)) {
                continue;
            }

            int taksitNo = idx + 1;

            // ✅ islemler kaydı oluştur
            Islem islem = new Islem();
            islem.setKullaniciId(t.getKullaniciId());
            islem.setAileId(t.getAileId());
            islem.setKategoriId(digerGider.getId());
            islem.setKategoriAdiSnapshot(digerGider.getKategoriAd());
            islem.setTutar(t.getTutar());
            islem.setParaBirimi(t.getParaBirimi());
            islem.setIslemTarihi(LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 12, 0));
            islem.setAciklama(buildAciklama(t, taksitNo));

            Islem savedIslem = islemRepository.save(islem);

            // ✅ log yaz
            TaksitIslemLog log = new TaksitIslemLog();
            log.setTaksitId(t.getId());
            log.setVadeTarihi(dueDate);
            log.setTaksitNo(taksitNo);
            log.setIslemId(savedIslem.getId());
            logRepository.save(log);

            createdCount++;

            // ✅ son taksit ise bitir
            if (taksitNo >= t.getTaksitSayisi()) {
                t.setBittiMi(true);
                taksitRepository.save(t);
            }
        }

        return createdCount;
    }

    // ---------------- helpers ----------------

    private String buildAciklama(Taksit t, int taksitNo) {
        String base = "Taksit ödemesi";
        String title = (t.getTaksitBasligi() == null ? "" : t.getTaksitBasligi().trim());
        String suffix = " (" + taksitNo + "/" + t.getTaksitSayisi() + ")";
        if (!title.isBlank()) return base + ": " + title + suffix;
        return base + suffix;
    }

    /**
     * Bugün vade günü mü?
     * - Başlangıç tarihinden itibaren her ay aynı gün (ayın son günü taşması varsa son güne kırpılır)
     * - Eğer bugün vade değilse -1 döner
     */
    private int installmentIndexIfDueToday(Taksit t, LocalDate today) {
        LocalDate start = t.getBaslangicTarihi();
        if (start == null) return -1;

        // start'tan önceyse olmaz
        if (today.isBefore(start)) return -1;

        int total = t.getTaksitSayisi() == null ? 0 : t.getTaksitSayisi();
        if (total <= 0) return -1;

        // Bugün hangi “ay farkı”na denk geliyor?
        int monthDiff = (today.getYear() - start.getYear()) * 12 + (today.getMonthValue() - start.getMonthValue());
        if (monthDiff < 0) return -1;
        if (monthDiff >= total) return -1;

        LocalDate due = dueDateForIndex(start, monthDiff);
        if (today.equals(due)) return monthDiff;

        return -1;
    }

    /**
     * idx=0 => start
     * idx=1 => start + 1 ay (gün taşarsa o ayın son günü)
     */
    private LocalDate dueDateForIndex(LocalDate start, int idx) {
        YearMonth ym = YearMonth.from(start).plusMonths(idx);
        int day = start.getDayOfMonth();
        int lastDay = ym.lengthOfMonth();
        int dueDay = Math.min(day, lastDay);
        return LocalDate.of(ym.getYear(), ym.getMonthValue(), dueDay);
    }

    private TaksitResponse toResponse(Taksit t) {
        return new TaksitResponse(
                t.getId(),
                t.getKullaniciId(),
                t.getAileId(),
                t.getTaksitBasligi(),
                t.getTutar(),
                t.getParaBirimi(),
                t.getBaslangicTarihi(),
                t.getTaksitSayisi(),
                t.getBittiMi(),
                t.getAciklama(),
                t.getCreatedAt()
        );
    }
}
