package com.example.finansapii.dto;

import java.math.BigDecimal;

public record AylikAnalizResponse(
        String yilAy,
        BigDecimal aylikGelir,
        BigDecimal aylikGider
) {}
