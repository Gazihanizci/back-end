package com.example.finansapii.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OzelKategoriCreateRequest(
        @NotBlank @Size(max = 80) String ad,
        @NotNull String tip
) {}
