package com.example.finansapii.service;

import com.example.finansapii.dto.SabitOdemeCreateRequest;
import com.example.finansapii.dto.SabitOdemeResponse;
import com.example.finansapii.entity.SabitOdeme;
import com.example.finansapii.repository.SabitOdemeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SabitOdemeService {

    private final SabitOdemeRepository repo;

    public SabitOdemeService(SabitOdemeRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public SabitOdemeResponse create(Long kullaniciId, Long aileId, SabitOdemeCreateRequest req) {
        SabitOdeme s = new SabitOdeme();
        s.setKullaniciId(kullaniciId);
        s.setAileId(aileId);

        s.setOdemeAdi(req.odemeAdi().trim());
        s.setSonOdemeGunu(req.sonOdemeGunu());

        if (req.aktif() != null) s.setAktif(req.aktif());
        s.setAciklama(req.aciklama() == null ? null : req.aciklama().trim());

        SabitOdeme saved = repo.save(s);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<SabitOdemeResponse> listMine(Long kullaniciId) {
        return repo.findByKullaniciIdOrderBySonOdemeGunuAsc(kullaniciId)
                .stream().map(this::toResponse).toList();
    }

    private SabitOdemeResponse toResponse(SabitOdeme s) {
        return new SabitOdemeResponse(
                s.getOdemeId(),
                s.getKullaniciId(),
                s.getAileId(),
                s.getOdemeAdi(),
                s.getSonOdemeGunu(),
                s.getAktif(),
                s.getAciklama(),
                s.getCreatedAt()
        );
    }
}
