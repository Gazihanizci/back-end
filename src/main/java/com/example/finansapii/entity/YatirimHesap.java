package com.example.finansapii.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "yatirim_hesaplari",
        indexes = {
                @Index(name = "idx_yatirim_user", columnList = "kullanici_id"),
                @Index(name = "idx_yatirim_aile", columnList = "aile_id")
        })
public class YatirimHesap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "yatirim_id")
    private Long yatirimId;

    @Column(name = "kullanici_id", nullable = false)
    private Long kullaniciId;

    @Column(name = "aile_id", nullable = false)
    private Long aileId;

    @Column(name = "hesap_adi", nullable = false, length = 100)
    private String hesapAdi;

    @Column(name = "varlik_turu", nullable = false, length = 20)
    private String varlikTuru; // "USD", "EUR", "ALTIN" ...

    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal adet; // kaç birim

    @Column(name = "ilk_alis_fiyati", nullable = false, precision = 18, scale = 6)
    private BigDecimal ilkAlisFiyati; // ortalama maliyet

    @Column(name = "guncel_fiyat", nullable = false, precision = 18, scale = 6)
    private BigDecimal guncelFiyat;

    @Column(name = "toplam_maliyet", nullable = false, precision = 18, scale = 2)
    private BigDecimal toplamMaliyet;

    @Column(name = "guncel_deger", nullable = false, precision = 18, scale = 2)
    private BigDecimal guncelDeger;

    @Column(name = "kar_zarar", nullable = false, precision = 18, scale = 2)
    private BigDecimal karZarar;

    @Column(name = "olusturma_tarihi", nullable = false)
    private LocalDateTime olusturmaTarihi = LocalDateTime.now();

    @Column(name = "guncelleme_tarihi", nullable = false)
    private LocalDateTime guncellemeTarihi = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.guncellemeTarihi = LocalDateTime.now();
    }

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
