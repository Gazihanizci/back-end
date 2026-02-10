package com.example.finansapii.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "islemler")
public class Islem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "islem_id")
    private Long id;

    @Column(name = "kullanici_id", nullable = false)
    private Long kullaniciId;

    @Column(name = "aile_id")
    private Long aileId;

    @Column(name = "kategori_id", nullable = false)
    private Long kategoriId;

    @Column(name = "kategori_adi_snapshot", nullable = false, length = 120)
    private String kategoriAdiSnapshot;

    @Column(name = "tutar", nullable = false, precision = 19, scale = 2)
    private BigDecimal tutar;

    @Column(name = "para_birimi", nullable = false, length = 10)
    private String paraBirimi;

    @Column(name = "islem_tarihi", nullable = false)
    private LocalDateTime islemTarihi;

    @Column(name = "aciklama", length = 255)
    private String aciklama;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) createdAt = now;
        if (islemTarihi == null) islemTarihi = now;
        if (paraBirimi == null || paraBirimi.isBlank()) paraBirimi = "TL";
    }

    // getters / setters
    public Long getId() { return id; }

    public Long getKullaniciId() { return kullaniciId; }
    public void setKullaniciId(Long kullaniciId) { this.kullaniciId = kullaniciId; }

    public Long getAileId() { return aileId; }
    public void setAileId(Long aileId) { this.aileId = aileId; }

    public Long getKategoriId() { return kategoriId; }
    public void setKategoriId(Long kategoriId) { this.kategoriId = kategoriId; }

    public String getKategoriAdiSnapshot() { return kategoriAdiSnapshot; }
    public void setKategoriAdiSnapshot(String kategoriAdiSnapshot) { this.kategoriAdiSnapshot = kategoriAdiSnapshot; }

    public BigDecimal getTutar() { return tutar; }
    public void setTutar(BigDecimal tutar) { this.tutar = tutar; }

    public String getParaBirimi() { return paraBirimi; }
    public void setParaBirimi(String paraBirimi) { this.paraBirimi = paraBirimi; }

    public LocalDateTime getIslemTarihi() { return islemTarihi; }
    public void setIslemTarihi(LocalDateTime islemTarihi) { this.islemTarihi = islemTarihi; }

    public String getAciklama() { return aciklama; }
    public void setAciklama(String aciklama) { this.aciklama = aciklama; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
