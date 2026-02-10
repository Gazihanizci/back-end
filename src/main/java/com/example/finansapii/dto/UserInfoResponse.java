package com.example.finansapii.dto;

public record UserInfoResponse(
        Long kullaniciId,
        String ad,
        String soyad,
        String email,
        Long aileId
) {}
