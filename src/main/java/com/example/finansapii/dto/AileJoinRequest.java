package com.example.finansapii.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AileJoinRequest(
        @NotNull Long aileId,
        @NotBlank @Size(min = 4, max = 64) String parola
) {}
