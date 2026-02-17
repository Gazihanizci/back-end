package com.example.finansapii.dto;

import java.math.BigDecimal;
import java.util.List;

public record YatirimGraphResponse(
        BigDecimal toplamMaliyet,
        BigDecimal toplamGuncelDeger,
        BigDecimal toplamKarZarar,
        List<YatirimGraphPointDto> points
) {}
