package com.example.finansapii.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TaksitCreateRequest(
        @NotBlank String taksitBasligi,

        @NotNull
        @DecimalMin(value = "0.01", inclusive = true)
        BigDecimal tutar,

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate baslangicTarihi,

        @NotNull
        @Min(1)
        Integer taksitSayisi,

        String aciklama
) {}
