package com.example.finansapii.dto;

public class AuthResponse {

    private String mesaj;
    private Long kullaniciId;
    private String email;

    public AuthResponse(String mesaj, Long kullaniciId, String email) {
        this.mesaj = mesaj;
        this.kullaniciId = kullaniciId;
        this.email = email;
    }

    public String getMesaj() { return mesaj; }
    public Long getKullaniciId() { return kullaniciId; }
    public String getEmail() { return email; }
}
