package com.example.finansapii.dto;

public class AuthResponse {

    private String mesaj;
    private Long kullaniciId;
    private String email;

    // ✅ JWT alanları
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";

    public AuthResponse(String mesaj,
                        Long kullaniciId,
                        String email,
                        String accessToken,
                        String refreshToken) {
        this.mesaj = mesaj;
        this.kullaniciId = kullaniciId;
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getMesaj() { return mesaj; }
    public Long getKullaniciId() { return kullaniciId; }
    public String getEmail() { return email; }

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public String getTokenType() { return tokenType; }
}
