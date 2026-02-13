package com.example.finansapii.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record SabitOdemeCreateRequest(
        @NotBlank String odemeAdi,
        @NotNull LocalDate sonOdemeGunu,
        Boolean aktif,
        String aciklama
) {}
