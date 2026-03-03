package com.example.finansapii.repository;

import com.example.finansapii.entity.PiyasaFiyat;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface PiyasaFiyatRepository extends JpaRepository<PiyasaFiyat, Long> {

    @Query("""
           SELECT p.guncelFiyat
           FROM PiyasaFiyat p
           WHERE p.paraBirimi = :varlikTuru
           ORDER BY p.zaman DESC
           LIMIT 1
           """)
    BigDecimal findLatestPrice(@Param("varlikTuru") String varlikTuru);
}