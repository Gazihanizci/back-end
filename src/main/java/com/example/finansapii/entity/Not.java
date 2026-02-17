package com.example.finansapii.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notlar")
public class Not {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "not_id")
    private Long notId;

    @Column(name = "kullanici_id", nullable = false)
    private Long kullaniciId;

    @Column(name = "not_metini", nullable = false, length = 2000)
    private String notMetini;

    // DB kolon adı created_ad (sende böyle)
    @Column(name = "created_ad", nullable = false)
    private LocalDateTime createdAd;

    @PrePersist
    public void prePersist() {
        if (this.createdAd == null) this.createdAd = LocalDateTime.now();
    }

    public Long getNotId() { return notId; }

    public Long getKullaniciId() { return kullaniciId; }
    public void setKullaniciId(Long kullaniciId) { this.kullaniciId = kullaniciId; }

    public String getNotMetini() { return notMetini; }
    public void setNotMetini(String notMetini) { this.notMetini = notMetini; }

    public LocalDateTime getCreatedAd() { return createdAd; }
    public void setCreatedAd(LocalDateTime createdAd) { this.createdAd = createdAd; }
}
