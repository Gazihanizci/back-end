package com.example.finansapii.dto;

public class LoginResponse {
    public boolean ok;
    public String mesaj;
    public Long userId;
    public String ad;
    public String soyad;

    public LoginResponse(boolean ok, String mesaj, Long userId, String ad, String soyad) {
        this.ok = ok;
        this.mesaj = mesaj;
        this.userId = userId;
        this.ad = ad;
        this.soyad = soyad;
    }
}