package com.example.finansapii.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class VerifyEmailRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "\\d{6}", message = "Kod 6 haneli olmalı")
    private String code;

    public String getEmail() { return email; }
    public String getCode() { return code; }
}
