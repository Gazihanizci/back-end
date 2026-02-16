package com.example.finansapii.dto;

import java.math.BigDecimal;

public class YatirimGuncelFiyatUpdateRequest {
    private BigDecimal guncelFiyat;

    public BigDecimal getGuncelFiyat() { return guncelFiyat; }
    public void setGuncelFiyat(BigDecimal guncelFiyat) { this.guncelFiyat = guncelFiyat; }
}
