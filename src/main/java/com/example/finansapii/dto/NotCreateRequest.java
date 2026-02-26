package com.example.finansapii.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NotCreateRequest(
        @NotBlank(message = "notMetini boş olamaz")
        @Size(max = 2000, message = "notMetini en fazla 2000 karakter olabilir")
        String notMetini,

        @NotNull(message = "notTuru zorunlu (USER/FAMILY)")
        NotTuru notTuru
) { }