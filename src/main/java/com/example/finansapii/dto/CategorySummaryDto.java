package com.example.finansapii.dto;

import java.math.BigDecimal;

public record CategorySummaryDto(
        Long kategoriId,
        String kategoriAd,
        String tip,          // "GELIR" / "GIDER"
        BigDecimal toplamTutar
) {}
