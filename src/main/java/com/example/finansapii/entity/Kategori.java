package com.example.finansapii.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "kategoriler")
public class Kategori {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kategori_id")
    private Long id;

    @Column(name = "kategori_ad", nullable = false, length = 120)
    private String kategoriAd;

    // GELIR / GIDER (DB'de string)
    @Column(name = "tip", nullable = false, length = 10)
    private String tip;

    public Long getId() { return id; }

    // ✅ Service bunu çağırıyor
    public String getKategoriAd() { return kategoriAd; }

    public String getTip() { return tip; }
}
