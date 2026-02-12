package com.example.finansapii.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OzelIslemCreateRequest(
        @NotNull Long aileId,
        @NotNull Long ozelKategoriId,

        @NotNull @Positive BigDecimal tutar,

        @NotNull @Size(min = 1, max = 10) String paraBirimi, // "TL"
        @NotNull LocalDateTime islemTarihi,

        @Size(max = 255) String aciklama
) {}
