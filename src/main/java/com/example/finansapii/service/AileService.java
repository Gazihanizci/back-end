package com.example.finansapii.service;

import com.example.finansapii.dto.AileCreateRequest;
import com.example.finansapii.dto.AileResponse;
import com.example.finansapii.entity.Aile;
import com.example.finansapii.repository.AileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AileService {

    private final AileRepository aileRepository;
    private final PasswordEncoder passwordEncoder;

    public AileService(AileRepository aileRepository, PasswordEncoder passwordEncoder) {
        this.aileRepository = aileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AileResponse create(Long kullaniciId, AileCreateRequest req) {

        aileRepository.findByAileSahibiKullaniciId(kullaniciId).ifPresent(a -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Zaten bir aile hesabın var.");
        });

        String aileAdi = req.aileAdi() == null ? "" : req.aileAdi().trim();
        String parola = req.parola() == null ? "" : req.parola().trim();

        if (aileAdi.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "aileAdi boş olamaz.");
        }
        if (parola.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "parola boş olamaz.");
        }

        Aile aile = new Aile();
        aile.setAileAdi(aileAdi);
        aile.setAileUyeSayisi(1);
        aile.setAileSahibiKullaniciId(kullaniciId);

        // ✅ DB’ye hash kaydet
        aile.setAileParola(passwordEncoder.encode(parola));

        Aile saved = aileRepository.save(aile);
        return new AileResponse(
                saved.getAileId(),
                saved.getAileAdi(),
                saved.getAileUyeSayisi(),
                saved.getAileSahibiKullaniciId()
        );
    }
}
