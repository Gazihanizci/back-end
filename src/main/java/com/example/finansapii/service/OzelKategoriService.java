package com.example.finansapii.service;

import com.example.finansapii.dto.OzelKategoriCreateRequest;
import com.example.finansapii.dto.OzelKategoriResponse;
import com.example.finansapii.entity.OzelKategori;
import com.example.finansapii.repository.OzelKategoriRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OzelKategoriService {

    private final OzelKategoriRepository repo;

    public OzelKategoriService(OzelKategoriRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public OzelKategoriResponse create(Long kullaniciId, OzelKategoriCreateRequest req) {

        String ad = req.ad().trim();

        if (repo.existsByKullaniciIdAndKategoriAdiIgnoreCase(kullaniciId, ad)) {
            throw new RuntimeException("Bu isimde özel kategori zaten var.");
        }

        OzelKategori k = new OzelKategori();
        k.setKullaniciId(kullaniciId);
        k.setKategoriAdi(ad);
        k.setTip(OzelKategori.Tip.valueOf(req.tip().trim().toUpperCase()));

        OzelKategori saved = repo.save(k);

        return new OzelKategoriResponse(saved.getId(), saved.getKategoriAdi(), saved.getTip().name());
    }

    @Transactional(readOnly = true)
    public List<OzelKategoriResponse> listMine(Long kullaniciId) {
        return repo.findAllByKullaniciIdOrderByIdDesc(kullaniciId).stream()
                .map(k -> new OzelKategoriResponse(k.getId(), k.getKategoriAdi(), k.getTip().name()))
                .toList();
    }

    @Transactional
    public void deleteMine(Long kullaniciId, Long ozelKategoriId) {
        OzelKategori k = repo.findByIdAndKullaniciId(ozelKategoriId, kullaniciId)
                .orElseThrow(() -> new RuntimeException("Özel kategori bulunamadı."));

        repo.delete(k);
    }
}
