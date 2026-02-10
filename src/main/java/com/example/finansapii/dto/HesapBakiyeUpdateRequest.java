package com.example.finansapii.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record HesapBakiyeUpdateRequest(
        @NotNull @DecimalMin("0.00") BigDecimal bakiye
) {}
