package com.example.finansapii.entity;

import com.example.finansapii.dto.NotTuru;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notlar",
        indexes = {
                @Index(name = "idx_notlar_user", columnList = "kullanici_id"),
                @Index(name = "idx_notlar_aile", columnList = "aile_id")
        })
public class Not {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "not_id")
    private Long notId;

    @Column(name = "kullanici_id", nullable = false)
    private Long kullaniciId;

    @Column(name = "aile_id")
    private Long aileId; // USER notlarında null olabilir, FAMILY notlarında dolu

    @Column(name = "not_metini", nullable = false, length = 2000)
    private String notMetini;

    @Enumerated(EnumType.STRING)
    @Column(name = "not_turu", nullable = false, length = 10)
    private NotTuru notTuru = NotTuru.USER;

    // DB kolon adı created_ad (sende böyle)
    @Column(name = "created_ad", nullable = false)
    private LocalDateTime createdAd;

    @PrePersist
    public void prePersist() {
        if (this.createdAd == null) this.createdAd = LocalDateTime.now();
        if (this.notTuru == null) this.notTuru = NotTuru.USER;
    }

    public Long getNotId() { return notId; }

    public Long getKullaniciId() { return kullaniciId; }
    public void setKullaniciId(Long kullaniciId) { this.kullaniciId = kullaniciId; }

    public Long getAileId() { return aileId; }
    public void setAileId(Long aileId) { this.aileId = aileId; }

    public String getNotMetini() { return notMetini; }
    public void setNotMetini(String notMetini) { this.notMetini = notMetini; }

    public NotTuru getNotTuru() { return notTuru; }
    public void setNotTuru(NotTuru notTuru) { this.notTuru = notTuru; }

    public LocalDateTime getCreatedAd() { return createdAd; }
    public void setCreatedAd(LocalDateTime createdAd) { this.createdAd = createdAd; }
}