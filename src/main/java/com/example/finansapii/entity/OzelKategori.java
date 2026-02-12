package com.example.finansapii.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "ozel_kategoriler",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_user_category", columnNames = {"kullanici_id", "kategori_adi"})
        }
)
public class OzelKategori {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ozel_kategori_id")
    private Long id;

    @Column(name = "kullanici_id", nullable = false)
    private Long kullaniciId;

    @Column(name = "kategori_adi", nullable = false, length = 80)
    private String kategoriAdi;

    @Enumerated(EnumType.STRING)
    @Column(name = "tip", nullable = false, length = 10)
    private Tip tip;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Tip { GELIR, GIDER }

    // getters / setters
    public Long getId() { return id; }

    public Long getKullaniciId() { return kullaniciId; }
    public void setKullaniciId(Long kullaniciId) { this.kullaniciId = kullaniciId; }

    public String getKategoriAdi() { return kategoriAdi; }
    public void setKategoriAdi(String kategoriAdi) { this.kategoriAdi = kategoriAdi; }

    public Tip getTip() { return tip; }
    public void setTip(Tip tip) { this.tip = tip; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
