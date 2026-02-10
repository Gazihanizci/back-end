package com.example.finansapii.dto;

import java.util.List;

public record FamilyInfoResponse(
        Long aileId,
        Long aileSahibiKullaniciId,
        List<MemberDto> members
) {
    public record MemberDto(
            Long id,
            String ad,
            String soyad,
            String email
    ) {}
}
