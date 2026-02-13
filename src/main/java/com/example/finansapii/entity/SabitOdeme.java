package com.example.finansapii.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "sabit_odemeler")
public class SabitOdeme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "odeme_id")
    private Long odemeId;

    @Column(name = "kullanici_id", nullable = false)
    private Long kullaniciId;

    @Column(name = "aile_id")
    private Long aileId;

    @Column(name = "odeme_adi", nullable = false, length = 150)
    private String odemeAdi;

    @Column(name = "son_odeme_gunu", nullable = false)
    private LocalDate sonOdemeGunu;

    @Column(name = "aktif", nullable = false)
    private Boolean aktif = true;

    @Column(name = "aciklama", length = 255)
    private String aciklama;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (aktif == null) aktif = true;
        createdAt = LocalDateTime.now();
    }

    // --- getter/setter ---
    public Long getOdemeId() { return odemeId; }
    public void setOdemeId(Long odemeId) { this.odemeId = odemeId; }

    public Long getKullaniciId() { return kullaniciId; }
    public void setKullaniciId(Long kullaniciId) { this.kullaniciId = kullaniciId; }

    public Long getAileId() { return aileId; }
    public void setAileId(Long aileId) { this.aileId = aileId; }

    public String getOdemeAdi() { return odemeAdi; }
    public void setOdemeAdi(String odemeAdi) { this.odemeAdi = odemeAdi; }

    public LocalDate getSonOdemeGunu() { return sonOdemeGunu; }
    public void setSonOdemeGunu(LocalDate sonOdemeGunu) { this.sonOdemeGunu = sonOdemeGunu; }

    public Boolean getAktif() { return aktif; }
    public void setAktif(Boolean aktif) { this.aktif = aktif; }

    public String getAciklama() { return aciklama; }
    public void setAciklama(String aciklama) { this.aciklama = aciklama; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
