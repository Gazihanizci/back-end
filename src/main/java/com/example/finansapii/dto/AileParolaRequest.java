package com.example.finansapii.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AileParolaRequest(
        @NotBlank @Size(min = 4, max = 64)
        String parola
) {}
