package com.example.finansapii.service;

import com.example.finansapii.dto.AileKatilRequest;
import com.example.finansapii.entity.AileKatilEntity;
import com.example.finansapii.repository.AileKatilRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AileKatilService {

    private final AileKatilRepository aileKatilRepository;
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    public AileKatilService(
            AileKatilRepository aileKatilRepository,
            JdbcTemplate jdbcTemplate,
            PasswordEncoder passwordEncoder
    ) {
        this.aileKatilRepository = aileKatilRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void katil(Long kullaniciId, AileKatilRequest req) {
        if (kullaniciId == null) throw new IllegalArgumentException("X-USER-ID zorunlu");
        if (req == null) throw new IllegalArgumentException("Body zorunlu");
        if (req.getAileId() == null) throw new IllegalArgumentException("aileId zorunlu");

        String parola = (req.getParola() == null) ? "" : req.getParola().trim();
        if (parola.isEmpty()) throw new IllegalArgumentException("parola zorunlu");

        AileKatilEntity aile = aileKatilRepository.findById(req.getAileId())
                .orElseThrow(() -> new RuntimeException("Aile bulunamadı"));

        // ✅ BCrypt karşılaştırma
        if (!passwordEncoder.matches(parola, aile.getAileParola())) {
            throw new RuntimeException("Parola yanlış");
        }

        // ✅ kullanıcıyı aileye bağla (kullanicilar.aile_id update)
        int updated = jdbcTemplate.update(
                "UPDATE kullanicilar SET aile_id = ? WHERE kullanici_id = ? AND aile_id IS NULL",
                aile.getAileId(), kullaniciId
        );

        if (updated == 0) {
            throw new RuntimeException("Zaten bir aileye bağlısın veya kullanıcı bulunamadı");
        }

        // ✅ üye sayısını artır
        int yeniSayi = (aile.getAileUyeSayisi() == null ? 0 : aile.getAileUyeSayisi()) + 1;
        aile.setAileUyeSayisi(yeniSayi);
        aileKatilRepository.save(aile);
    }
}
