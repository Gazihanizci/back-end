package com.example.finansapii.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "yatirim_portfoy",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_user_varlik",
                        columnNames = {"kullanici_id", "varlik_turu"}
                )
        }
)
public class YatirimPortfoy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfoy_id")
    private Long portfoyId;

    @Column(name = "kullanici_id", nullable = false)
    private Long kullaniciId;

    @Column(name = "varlik_turu", nullable = false, length = 20)
    private String varlikTuru;

    @Column(name = "toplam_adet", nullable = false, precision = 18, scale = 6)
    private BigDecimal toplamAdet = BigDecimal.ZERO;

    @Column(name = "ortalama_maliyet", nullable = false, precision = 18, scale = 6)
    private BigDecimal ortalamaMaliyet = BigDecimal.ZERO;

    @Column(name = "toplam_maliyet", nullable = false, precision = 18, scale = 2)
    private BigDecimal toplamMaliyet = BigDecimal.ZERO;

    @Column(name = "realized_kar", nullable = false, precision = 18, scale = 2)
    private BigDecimal realizedKar = BigDecimal.ZERO;

    @Column(name = "unrealized_kar", nullable = false, precision = 18, scale = 2)
    private BigDecimal unrealizedKar = BigDecimal.ZERO;

    @Column(name = "guncel_deger", nullable = false, precision = 18, scale = 2)
    private BigDecimal guncelDeger = BigDecimal.ZERO;

    @Column(name = "olusturma_tarihi", nullable = false)
    private LocalDateTime olusturmaTarihi = LocalDateTime.now();

    @Column(name = "guncelleme_tarihi", nullable = false)
    private LocalDateTime guncellemeTarihi = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.guncellemeTarihi = LocalDateTime.now();
    }

    // =========================
    // GETTERS & SETTERS
    // =========================

    public Long getPortfoyId() {
        return portfoyId;
    }

    public void setPortfoyId(Long portfoyId) {
        this.portfoyId = portfoyId;
    }

    public Long getKullaniciId() {
        return kullaniciId;
    }

    public void setKullaniciId(Long kullaniciId) {
        this.kullaniciId = kullaniciId;
    }

    public String getVarlikTuru() {
        return varlikTuru;
    }

    public void setVarlikTuru(String varlikTuru) {
        this.varlikTuru = varlikTuru;
    }

    public BigDecimal getToplamAdet() {
        return toplamAdet;
    }

    public void setToplamAdet(BigDecimal toplamAdet) {
        this.toplamAdet = toplamAdet;
    }

    public BigDecimal getOrtalamaMaliyet() {
        return ortalamaMaliyet;
    }

    public void setOrtalamaMaliyet(BigDecimal ortalamaMaliyet) {
        this.ortalamaMaliyet = ortalamaMaliyet;
    }

    public BigDecimal getToplamMaliyet() {
        return toplamMaliyet;
    }

    public void setToplamMaliyet(BigDecimal toplamMaliyet) {
        this.toplamMaliyet = toplamMaliyet;
    }

    public BigDecimal getRealizedKar() {
        return realizedKar;
    }

    public void setRealizedKar(BigDecimal realizedKar) {
        this.realizedKar = realizedKar;
    }

    public BigDecimal getUnrealizedKar() {
        return unrealizedKar;
    }

    public void setUnrealizedKar(BigDecimal unrealizedKar) {
        this.unrealizedKar = unrealizedKar;
    }

    public BigDecimal getGuncelDeger() {
        return guncelDeger;
    }

    public void setGuncelDeger(BigDecimal guncelDeger) {
        this.guncelDeger = guncelDeger;
    }

    public LocalDateTime getOlusturmaTarihi() {
        return olusturmaTarihi;
    }

    public void setOlusturmaTarihi(LocalDateTime olusturmaTarihi) {
        this.olusturmaTarihi = olusturmaTarihi;
    }

    public LocalDateTime getGuncellemeTarihi() {
        return guncellemeTarihi;
    }

    public void setGuncellemeTarihi(LocalDateTime guncellemeTarihi) {
        this.guncellemeTarihi = guncellemeTarihi;
    }
}