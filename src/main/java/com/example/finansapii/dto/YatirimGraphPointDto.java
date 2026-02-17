package com.example.finansapii.dto;

import java.math.BigDecimal;

public record YatirimGraphPointDto(
        String label,
        BigDecimal toplamMaliyet,
        BigDecimal guncelDeger,
        BigDecimal karZarar
) {}
