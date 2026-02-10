package com.example.finansapii.service;

import com.example.finansapii.dto.IslemCreateRequest;
import com.example.finansapii.dto.IslemResponse;
import com.example.finansapii.entity.Islem;
import com.example.finansapii.entity.Kategori;
import com.example.finansapii.entity.User;
import com.example.finansapii.repository.IslemRepository;
import com.example.finansapii.repository.KategoriRepository;
import com.example.finansapii.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IslemService {

    private final IslemRepository islemRepository;
    private final KategoriRepository kategoriRepository;
    private final UserRepository userRepository;

    public IslemService(IslemRepository islemRepository,
                        KategoriRepository kategoriRepository,
                        UserRepository userRepository) {
        this.islemRepository = islemRepository;
        this.kategoriRepository = kategoriRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public IslemResponse create(Long kullaniciId, IslemCreateRequest req) {

        User user = userRepository.findById(kullaniciId)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı"));

        Kategori kategori = kategoriRepository.findById(req.kategoriId())
                .orElseThrow(() -> new IllegalArgumentException("Kategori bulunamadı"));

        Islem islem = new Islem();
        islem.setKullaniciId(kullaniciId);

        // ✅ aile_id: kullanıcıdan çek
        islem.setAileId(user.getAileId());

        // ✅ kategori_id ve adı snapshot
        islem.setKategoriId(kategori.getId());
        islem.setKategoriAdiSnapshot(kategori.getKategoriAd());

        // ✅ tutar
        islem.setTutar(req.tutar());

        // ✅ para birimi otomatik TL
        islem.setParaBirimi("TL");

        // ✅ tarih gelmezse entity prePersist doldurur
        islem.setIslemTarihi(req.islemTarihi());

        // ✅ açıklama opsiyonel
        islem.setAciklama(req.aciklama());

        Islem saved = islemRepository.save(islem);

        return new IslemResponse(
                saved.getId(),
                saved.getKullaniciId(),
                saved.getAileId(),
                saved.getKategoriId(),
                saved.getKategoriAdiSnapshot(),
                saved.getTutar(),
                saved.getParaBirimi(),
                saved.getIslemTarihi(),
                saved.getAciklama(),
                saved.getCreatedAt()
        );
    }

    // ✅ EK: Kullanıcının tüm işlemlerini sayfalı listele
    @Transactional(readOnly = true)
    public Page<IslemResponse> listByUser(Long kullaniciId, Pageable pageable) {
        return islemRepository.findByKullaniciId(kullaniciId, pageable)
                .map(this::toResponse);
    }

    private IslemResponse toResponse(Islem i) {
        return new IslemResponse(
                i.getId(),
                i.getKullaniciId(),
                i.getAileId(),
                i.getKategoriId(),
                i.getKategoriAdiSnapshot(),
                i.getTutar(),
                i.getParaBirimi(),
                i.getIslemTarihi(),
                i.getAciklama(),
                i.getCreatedAt()
        );
    }
}
