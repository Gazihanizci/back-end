package com.example.finansapii.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "aileler")
public class AileKatilEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aile_id")
    private Long aileId;

    @Column(name = "aile_adi", nullable = false)
    private String aileAdi;

    @Column(name = "aile_uye_sayisi", nullable = false)
    private Integer aileUyeSayisi = 1;

    @Column(name = "aile_parola", nullable = false) // ✅ katı kural
    private String aileParola;

    public Long getAileId() { return aileId; }
    public void setAileId(Long aileId) { this.aileId = aileId; }

    public String getAileAdi() { return aileAdi; }
    public void setAileAdi(String aileAdi) { this.aileAdi = aileAdi; }

    public Integer getAileUyeSayisi() { return aileUyeSayisi; }
    public void setAileUyeSayisi(Integer aileUyeSayisi) { this.aileUyeSayisi = aileUyeSayisi; }

    public String getAileParola() { return aileParola; }
    public void setAileParola(String aileParola) { this.aileParola = aileParola; }
}
