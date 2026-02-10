package com.example.finansapii.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record HesapCreateRequest(
        @NotBlank @Size(max = 80) String hesapAdi,
        Long aileId,
        @NotBlank @Size(max = 10) String paraBirimi,
        @NotNull @DecimalMin("0.00") BigDecimal bakiye
) {}
