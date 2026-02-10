package com.example.finansapii.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record HesapResponse(
        Long hesapId,
        Long kullaniciId,
        Long aileId,
        String hesapAdi,
        String paraBirimi,
        BigDecimal bakiye,
        Instant createdAt
) {}
