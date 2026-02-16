package com.example.finansapii.dto;

import java.math.BigDecimal;

public class YatirimHesapCreateRequest {
    private Long aileId;
    private String hesapAdi;
    private String varlikTuru;       // "USD", "EUR", "ALTIN"
    private BigDecimal adet;         // 100.0
    private BigDecimal ilkAlisFiyati; // 30.50
    private BigDecimal guncelFiyat;   // oluştururken istersen aynı gir

    public Long getAileId() { return aileId; }
    public void setAileId(Long aileId) { this.aileId = aileId; }

    public String getHesapAdi() { return hesapAdi; }
    public void setHesapAdi(String hesapAdi) { this.hesapAdi = hesapAdi; }

    public String getVarlikTuru() { return varlikTuru; }
    public void setVarlikTuru(String varlikTuru) { this.varlikTuru = varlikTuru; }

    public BigDecimal getAdet() { return adet; }
    public void setAdet(BigDecimal adet) { this.adet = adet; }

    public BigDecimal getIlkAlisFiyati() { return ilkAlisFiyati; }
    public void setIlkAlisFiyati(BigDecimal ilkAlisFiyati) { this.ilkAlisFiyati = ilkAlisFiyati; }

    public BigDecimal getGuncelFiyat() { return guncelFiyat; }
    public void setGuncelFiyat(BigDecimal guncelFiyat) { this.guncelFiyat = guncelFiyat; }
}
