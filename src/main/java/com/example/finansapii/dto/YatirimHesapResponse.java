package com.example.finansapii.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class YatirimHesapResponse {
    private Long yatirimId;
    private Long kullaniciId;
    private Long aileId;

    private String hesapAdi;
    private String varlikTuru;

    private BigDecimal adet;
    private BigDecimal ilkAlisFiyati;
    private BigDecimal guncelFiyat;

    private BigDecimal toplamMaliyet;
    private BigDecimal guncelDeger;
    private BigDecimal karZarar;

    private LocalDateTime olusturmaTarihi;
    private LocalDateTime guncellemeTarihi;

    // getters/setters
    public Long getYatirimId() { return yatirimId; }
    public void setYatirimId(Long yatirimId) { this.yatirimId = yatirimId; }

    public Long getKullaniciId() { return kullaniciId; }
    public void setKullaniciId(Long kullaniciId) { this.kullaniciId = kullaniciId; }

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

    public BigDecimal getToplamMaliyet() { return toplamMaliyet; }
    public void setToplamMaliyet(BigDecimal toplamMaliyet) { this.toplamMaliyet = toplamMaliyet; }

    public BigDecimal getGuncelDeger() { return guncelDeger; }
    public void setGuncelDeger(BigDecimal guncelDeger) { this.guncelDeger = guncelDeger; }

    public BigDecimal getKarZarar() { return karZarar; }
    public void setKarZarar(BigDecimal karZarar) { this.karZarar = karZarar; }

    public LocalDateTime getOlusturmaTarihi() { return olusturmaTarihi; }
    public void setOlusturmaTarihi(LocalDateTime olusturmaTarihi) { this.olusturmaTarihi = olusturmaTarihi; }

    public LocalDateTime getGuncellemeTarihi() { return guncellemeTarihi; }
    public void setGuncellemeTarihi(LocalDateTime guncellemeTarihi) { this.guncellemeTarihi = guncellemeTarihi; }
}
