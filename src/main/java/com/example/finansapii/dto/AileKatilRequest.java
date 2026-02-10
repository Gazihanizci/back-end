package com.example.finansapii.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AileKatilRequest {

    @NotNull
    private Long aileId;

    @NotBlank
    private String parola;

    public Long getAileId() { return aileId; }
    public void setAileId(Long aileId) { this.aileId = aileId; }

    public String getParola() { return parola; }
    public void setParola(String parola) { this.parola = parola; }
}
