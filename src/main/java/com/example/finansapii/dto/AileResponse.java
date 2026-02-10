package com.example.finansapii.dto;

public record AileResponse(
        Long aileId,
        String aileAdi,
        Integer aileUyeSayisi,
        Long aileSahibiKullaniciId
) {}
