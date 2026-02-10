package com.example.finansapii.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "taksitler")
public class Taksit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taksit_id")
    private Long id;

    @Column(name = "kullanici_id", nullable = false)
    private Long kullaniciId;

    @Column(name = "aile_id")
    private Long aileId;

    @Column(name = "taksit_basligi", nullable = false, length = 160)
    private String taksitBasligi;

    @Column(name = "tutar", nullable = false, precision = 19, scale = 2)
    private BigDecimal tutar;

    @Column(name = "para_birimi", nullable = false, length = 10)
    private String paraBirimi;

    @Column(name = "baslangic_tarihi", nullable = false)
    private LocalDate baslangicTarihi;

    @Column(name = "taksit_sayisi", nullable = false)
    private Integer taksitSayisi;

    @Column(name = "bitti_mi", nullable = false)
    private Boolean bittiMi = false;

    @Column(name = "aciklama", length = 255)
    private String aciklama;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (paraBirimi == null || paraBirimi.isBlank()) paraBirimi = "TL";
        if (bittiMi == null) bittiMi = false;
    }

    // getters/setters
    public Long getId() { return id; }

    public Long getKullaniciId() { return kullaniciId; }
    public void setKullaniciId(Long kullaniciId) { this.kullaniciId = kullaniciId; }

    public Long getAileId() { return aileId; }
    public void setAileId(Long aileId) { this.aileId = aileId; }

    public String getTaksitBasligi() { return taksitBasligi; }
    public void setTaksitBasligi(String taksitBasligi) { this.taksitBasligi = taksitBasligi; }

    public BigDecimal getTutar() { return tutar; }
    public void setTutar(BigDecimal tutar) { this.tutar = tutar; }

    public String getParaBirimi() { return paraBirimi; }
    public void setParaBirimi(String paraBirimi) { this.paraBirimi = paraBirimi; }

    public LocalDate getBaslangicTarihi() { return baslangicTarihi; }
    public void setBaslangicTarihi(LocalDate baslangicTarihi) { this.baslangicTarihi = baslangicTarihi; }

    public Integer getTaksitSayisi() { return taksitSayisi; }
    public void setTaksitSayisi(Integer taksitSayisi) { this.taksitSayisi = taksitSayisi; }

    public Boolean getBittiMi() { return bittiMi; }
    public void setBittiMi(Boolean bittiMi) { this.bittiMi = bittiMi; }

    public String getAciklama() { return aciklama; }
    public void setAciklama(String aciklama) { this.aciklama = aciklama; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
