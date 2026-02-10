package com.example.finansapii.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record IslemResponse(
        Long islemId,
        Long kullaniciId,
        Long aileId,
        Long kategoriId,
        String kategoriAdiSnapshot,
        BigDecimal tutar,
        String paraBirimi,
        LocalDateTime islemTarihi,
        String aciklama,
        LocalDateTime createdAt
) {}
