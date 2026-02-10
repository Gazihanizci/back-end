package com.example.finansapii.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record IslemCreateRequest(
        @NotNull Long kategoriId,

        @NotNull
        @DecimalMin(value = "0.01", inclusive = true)
        BigDecimal tutar,

        // opsiyonel (gelmezse şimdi)
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime islemTarihi,

        // opsiyonel
        String aciklama
) {}
