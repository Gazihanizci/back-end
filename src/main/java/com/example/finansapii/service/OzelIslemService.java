package com.example.finansapii.service;

import com.example.finansapii.dto.OzelIslemCreateRequest;
import com.example.finansapii.entity.Islem;
import com.example.finansapii.entity.OzelKategori;
import com.example.finansapii.repository.IslemRepository;
import com.example.finansapii.repository.OzelKategoriRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static com.example.finansapii.security.CurrentUser.id; // sende nasıl ise ona göre düzenle

@Service
public class OzelIslemService {

    private final OzelKategoriRepository ozelKategoriRepository;
    private final IslemRepository islemRepository;

    public OzelIslemService(OzelKategoriRepository ozelKategoriRepository, IslemRepository islemRepository) {
        this.ozelKategoriRepository = ozelKategoriRepository;
        this.islemRepository = islemRepository;
    }

    @Transactional
    public Long create(OzelIslemCreateRequest req) {
        Long kullaniciId = id(); // ✅ JWT içinden

        // ✅ Özel kategori sadece bu kullanıcıya ait olmalı
        OzelKategori ok = ozelKategoriRepository
                .findByIdAndKullaniciId(req.ozelKategoriId(), kullaniciId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Özel kategori bulunamadı veya size ait değil: " + req.ozelKategoriId()
                ));

        Islem islem = new Islem();
        islem.setKullaniciId(kullaniciId);
        islem.setAileId(req.aileId());

        // 🔥 İSTEDİĞİN ŞEY:
        islem.setKategoriId(ok.getId());                 // -> islemler.kategori_id
        islem.setKategoriAdiSnapshot(ok.getKategoriAdi()); // -> islemler.kategori_adi_snapshot

        islem.setTutar(req.tutar());
        islem.setParaBirimi(req.paraBirimi().trim().toUpperCase());
        islem.setIslemTarihi(req.islemTarihi());
        islem.setAciklama(req.aciklama());

        Islem saved = islemRepository.save(islem);
        return saved.getId(); // islem_id
    }
}
