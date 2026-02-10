package com.example.finansapii.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "aileler")
public class AileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aile_id")
    private Long aileId;

    @Column(name = "aile_adi", nullable = false)
    private String aileAdi;

    // DB’de NOT NULL ise nullable=false yap
    @Column(name = "aile_parola", nullable = false)
    private String aileParola;

    @Column(name = "aile_uye_sayisi", nullable = false)
    private Integer aileUyeSayisi;

    @Column(name = "aile_sahibi_kullanici_id", nullable = false)
    private Long aileSahibiKullaniciId;

    // --- getters/setters ---
    public Long getAileId() { return aileId; }
    public void setAileId(Long aileId) { this.aileId = aileId; }

    public String getAileAdi() { return aileAdi; }
    public void setAileAdi(String aileAdi) { this.aileAdi = aileAdi; }

    public String getAileParola() { return aileParola; }
    public void setAileParola(String aileParola) { this.aileParola = aileParola; }

    public Integer getAileUyeSayisi() { return aileUyeSayisi; }
    public void setAileUyeSayisi(Integer aileUyeSayisi) { this.aileUyeSayisi = aileUyeSayisi; }

    public Long getAileSahibiKullaniciId() { return aileSahibiKullaniciId; }
    public void setAileSahibiKullaniciId(Long aileSahibiKullaniciId) { this.aileSahibiKullaniciId = aileSahibiKullaniciId; }
}
