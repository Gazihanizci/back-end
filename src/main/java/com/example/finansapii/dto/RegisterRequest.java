package com.example.finansapii.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank
    private String ad;

    @NotBlank
    private String soyad;

    @Email
    @NotBlank
    private String email;

    @Size(min = 6)
    @NotBlank
    private String parola;

    private String telefon;

    // getters
    public String getAd() { return ad; }
    public String getSoyad() { return soyad; }
    public String getEmail() { return email; }
    public String getParola() { return parola; }
    public String getTelefon() { return telefon; }
}
