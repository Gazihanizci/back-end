package com.example.finansapii.dto;

import java.math.BigDecimal;

public class YatirimGraphPointDto {
    private String label;
    private BigDecimal toplamMaliyet;
    private BigDecimal guncelDeger;
    private BigDecimal karZarar;

    public YatirimGraphPointDto(String label, BigDecimal toplamMaliyet, BigDecimal guncelDeger, BigDecimal karZarar) {
        this.label = label;
        this.toplamMaliyet = toplamMaliyet;
        this.guncelDeger = guncelDeger;
        this.karZarar = karZarar;
    }

    public String getLabel() { return label; }
    public BigDecimal getToplamMaliyet() { return toplamMaliyet; }
    public BigDecimal getGuncelDeger() { return guncelDeger; }
    public BigDecimal getKarZarar() { return karZarar; }
}
