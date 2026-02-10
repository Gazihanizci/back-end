package com.example.finansapii.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaksitResponse(
        Long taksitId,
        Long kullaniciId,
        Long aileId,
        String taksitBasligi,
        BigDecimal tutar,
        String paraBirimi,
        LocalDate baslangicTarihi,
        Integer taksitSayisi,
        Boolean bittiMi,
        String aciklama,
        LocalDateTime createdAt
) {}
