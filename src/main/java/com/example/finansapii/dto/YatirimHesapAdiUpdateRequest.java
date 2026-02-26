package com.example.finansapii.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record YatirimHesapAdiUpdateRequest(

        @NotBlank(message = "hesapAdi boş olamaz")
        @Size(max = 100, message = "hesapAdi en fazla 100 karakter olabilir")
        String hesapAdi

) {}