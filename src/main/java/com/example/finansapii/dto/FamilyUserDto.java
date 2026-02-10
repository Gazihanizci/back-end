package com.example.finansapii.dto;

public class FamilyUserDto {

    private Long kullaniciId;
    private String ad;
    private String soyad;
    private String email;

    public FamilyUserDto(Long kullaniciId, String ad, String soyad, String email) {
        this.kullaniciId = kullaniciId;
        this.ad = ad;
        this.soyad = soyad;
        this.email = email;
    }

    // getters
}
