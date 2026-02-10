package com.example.finansapii.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "hesaplar")
public class Hesap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hesap_id")
    private Long hesapId;

    @Column(name = "kullanici_id", nullable = false)
    private Long kullaniciId;

    @Column(name = "aile_id")
    private Long aileId;

    @Column(name = "hesap_adi", nullable = false, length = 80)
    private String hesapAdi;

    @Column(name = "para_birimi", nullable = false, length = 10)
    private String paraBirimi;

    @Column(name = "bakiye", nullable = false, precision = 19, scale = 2)
    private BigDecimal bakiye = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public Long getHesapId() { return hesapId; }
    public void setHesapId(Long hesapId) { this.hesapId = hesapId; }

    public Long getKullaniciId() { return kullaniciId; }
    public void setKullaniciId(Long kullaniciId) { this.kullaniciId = kullaniciId; }

    public Long getAileId() { return aileId; }
    public void setAileId(Long aileId) { this.aileId = aileId; }

    public String getHesapAdi() { return hesapAdi; }
    public void setHesapAdi(String hesapAdi) { this.hesapAdi = hesapAdi; }

    public String getParaBirimi() { return paraBirimi; }
    public void setParaBirimi(String paraBirimi) { this.paraBirimi = paraBirimi; }

    public BigDecimal getBakiye() { return bakiye; }
    public void setBakiye(BigDecimal bakiye) { this.bakiye = bakiye; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
