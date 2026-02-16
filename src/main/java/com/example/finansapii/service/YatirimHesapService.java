package com.example.finansapii.service;

import com.example.finansapii.dto.*;
import com.example.finansapii.entity.YatirimHesap;
import com.example.finansapii.repository.YatirimHesapRepository;
import com.example.finansapii.security.CurrentUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class YatirimHesapService {

    private final YatirimHesapRepository repo;

    public YatirimHesapService(YatirimHesapRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public YatirimHesapResponse create(YatirimHesapCreateRequest req) {
        Long kullaniciId = CurrentUser.id();

        validateCreate(req);

        YatirimHesap y = new YatirimHesap();
        y.setKullaniciId(kullaniciId);
        y.setAileId(req.getAileId());
        y.setHesapAdi(req.getHesapAdi().trim());
        y.setVarlikTuru(req.getVarlikTuru().trim().toUpperCase());
        y.setAdet(req.getAdet());

        y.setIlkAlisFiyati(req.getIlkAlisFiyati());

        // ilk oluştururken güncel fiyat verilmezse, ilk alış fiyatını baz al
        BigDecimal guncel = (req.getGuncelFiyat() != null) ? req.getGuncelFiyat() : req.getIlkAlisFiyati();
        y.setGuncelFiyat(guncel);

        recalc(y);

        return toResponse(repo.save(y));
    }

    public List<YatirimHesapResponse> listMy() {
        Long kullaniciId = CurrentUser.id();
        return repo.findByKullaniciId(kullaniciId).stream().map(this::toResponse).toList();
    }

    public YatirimHesapResponse getMyById(Long yatirimId) {
        Long kullaniciId = CurrentUser.id();
        YatirimHesap y = repo.findByYatirimIdAndKullaniciId(yatirimId, kullaniciId)
                .orElseThrow(() -> new RuntimeException("Yatırım hesabı bulunamadı: " + yatirimId));
        return toResponse(y);
    }

    @Transactional
    public YatirimHesapResponse updateGuncelFiyat(Long yatirimId, YatirimGuncelFiyatUpdateRequest req) {
        Long kullaniciId = CurrentUser.id();

        if (req.getGuncelFiyat() == null || req.getGuncelFiyat().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("guncelFiyat pozitif olmalı");
        }

        YatirimHesap y = repo.findByYatirimIdAndKullaniciId(yatirimId, kullaniciId)
                .orElseThrow(() -> new RuntimeException("Yatırım hesabı bulunamadı: " + yatirimId));

        y.setGuncelFiyat(req.getGuncelFiyat());
        recalc(y);

        return toResponse(repo.save(y));
    }

    /**
     * adetDegisim > 0 => ekleme (alisFiyati zorunlu)
     * adetDegisim < 0 => çıkarma
     */
    @Transactional
    public YatirimHesapResponse changeAdet(Long yatirimId, YatirimAdetDegisRequest req) {
        Long kullaniciId = CurrentUser.id();

        if (req.getAdetDegisim() == null || req.getAdetDegisim().compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("adetDegisim 0 olamaz");
        }

        YatirimHesap y = repo.findByYatirimIdAndKullaniciId(yatirimId, kullaniciId)
                .orElseThrow(() -> new RuntimeException("Yatırım hesabı bulunamadı: " + yatirimId));

        BigDecimal delta = req.getAdetDegisim();

        if (delta.compareTo(BigDecimal.ZERO) > 0) {
            // EKLEME => ortalama maliyet güncelle
            if (req.getAlisFiyati() == null || req.getAlisFiyati().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Ekleme için alisFiyati zorunlu ve pozitif olmalı");
            }

            BigDecimal oldAdet = y.getAdet();
            BigDecimal newAdet = oldAdet.add(delta);

            // Eski toplam maliyet + yeni alım maliyeti
            BigDecimal oldMaliyet = y.getToplamMaliyet();
            BigDecimal newAlimMaliyeti = delta.multiply(req.getAlisFiyati());
            BigDecimal newToplamMaliyet = oldMaliyet.add(newAlimMaliyeti);

            // yeni ortalama maliyet
            BigDecimal newOrtalama = newToplamMaliyet
                    .divide(newAdet, 6, RoundingMode.HALF_UP);

            y.setAdet(newAdet);
            y.setToplamMaliyet(scale2(newToplamMaliyet));
            y.setIlkAlisFiyati(newOrtalama);

        } else {
            // ÇIKARMA => adet azalt, maliyet oransal düşsün
            BigDecimal abs = delta.abs();
            if (abs.compareTo(y.getAdet()) > 0) {
                throw new IllegalArgumentException("Çıkarma miktarı mevcut adetten büyük olamaz");
            }

            BigDecimal oldAdet = y.getAdet();
            BigDecimal newAdet = oldAdet.subtract(abs);

            // maliyeti, çıkarılan adet oranında düşür
            BigDecimal oldMaliyet = y.getToplamMaliyet();
            BigDecimal oran = abs.divide(oldAdet, 10, RoundingMode.HALF_UP);
            BigDecimal dusenMaliyet = oldMaliyet.multiply(oran);
            BigDecimal newMaliyet = oldMaliyet.subtract(dusenMaliyet);

            y.setAdet(newAdet.max(BigDecimal.ZERO));
            y.setToplamMaliyet(scale2(newMaliyet.max(BigDecimal.ZERO)));

            // adet 0 olduysa ortalama maliyet/fiyatları sıfırlayalım (istersen)
            if (y.getAdet().compareTo(BigDecimal.ZERO) == 0) {
                y.setIlkAlisFiyati(BigDecimal.ZERO);
                // guncelFiyat aynı kalsın istersen değiştirme; ben 0 yapmıyorum
            }
        }

        // Her durumda yeniden hesapla: guncelDeger, karZarar
        recalc(y);
        return toResponse(repo.save(y));
    }

    @Transactional
    public void deleteMy(Long yatirimId) {
        Long kullaniciId = CurrentUser.id();
        YatirimHesap y = repo.findByYatirimIdAndKullaniciId(yatirimId, kullaniciId)
                .orElseThrow(() -> new RuntimeException("Yatırım hesabı bulunamadı: " + yatirimId));
        repo.delete(y);
    }

    // -------------------------
    // Helpers
    // -------------------------

    private void validateCreate(YatirimHesapCreateRequest req) {
        if (req.getAileId() == null) throw new IllegalArgumentException("aileId zorunlu");
        if (req.getHesapAdi() == null || req.getHesapAdi().trim().isEmpty()) throw new IllegalArgumentException("hesapAdi zorunlu");
        if (req.getVarlikTuru() == null || req.getVarlikTuru().trim().isEmpty()) throw new IllegalArgumentException("varlikTuru zorunlu");
        if (req.getAdet() == null || req.getAdet().compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("adet pozitif olmalı");
        if (req.getIlkAlisFiyati() == null || req.getIlkAlisFiyati().compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("ilkAlisFiyati pozitif olmalı");
        if (req.getGuncelFiyat() != null && req.getGuncelFiyat().compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("guncelFiyat pozitif olmalı");
    }

    private void recalc(YatirimHesap y) {
        // toplamMaliyet = adet * ilkAlisFiyati  (create’de kendimiz setliyoruz; yine de garanti)
        if (y.getToplamMaliyet() == null) {
            y.setToplamMaliyet(scale2(y.getAdet().multiply(y.getIlkAlisFiyati())));
        }

        BigDecimal guncelDeger = y.getAdet().multiply(y.getGuncelFiyat());
        y.setGuncelDeger(scale2(guncelDeger));

        BigDecimal karZarar = y.getGuncelDeger().subtract(y.getToplamMaliyet());
        y.setKarZarar(scale2(karZarar));
    }

    private BigDecimal scale2(BigDecimal x) {
        return x.setScale(2, RoundingMode.HALF_UP);
    }

    private YatirimHesapResponse toResponse(YatirimHesap y) {
        YatirimHesapResponse r = new YatirimHesapResponse();
        r.setYatirimId(y.getYatirimId());
        r.setKullaniciId(y.getKullaniciId());
        r.setAileId(y.getAileId());
        r.setHesapAdi(y.getHesapAdi());
        r.setVarlikTuru(y.getVarlikTuru());
        r.setAdet(y.getAdet());
        r.setIlkAlisFiyati(y.getIlkAlisFiyati());
        r.setGuncelFiyat(y.getGuncelFiyat());
        r.setToplamMaliyet(y.getToplamMaliyet());
        r.setGuncelDeger(y.getGuncelDeger());
        r.setKarZarar(y.getKarZarar());
        r.setOlusturmaTarihi(y.getOlusturmaTarihi());
        r.setGuncellemeTarihi(y.getGuncellemeTarihi());
        return r;
    }
}
