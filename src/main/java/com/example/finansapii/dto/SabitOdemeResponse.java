package com.example.finansapii.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SabitOdemeResponse(
        Long odemeId,
        Long kullaniciId,
        Long aileId,
        String odemeAdi,
        LocalDate sonOdemeGunu,
        Boolean aktif,
        String aciklama,
        LocalDateTime createdAt
) {}
