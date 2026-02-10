package com.example.finansapii.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String parola;

    public String getEmail() { return email; }
    public String getParola() { return parola; }
}
