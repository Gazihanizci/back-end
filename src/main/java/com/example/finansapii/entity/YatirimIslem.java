package com.example.finansapii.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "yatirim_islem")
public class YatirimIslem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "islem_id")
    private Long islemId;

    @Column(name = "kullanici_id", nullable = false)
    private Long kullaniciId;

    @Column(name = "aile_id", nullable = false)
    private Long aileId;

    @Column(name = "varlik_turu", nullable = false, length = 20)
    private String varlikTuru;

    @Column(name = "islem_tipi", nullable = false, length = 10)
    private String islemTipi; // ALIS / SATIS

    @Column(name = "adet", nullable = false, precision = 18, scale = 6)
    private BigDecimal adet;

    @Column(name = "fiyat", nullable = false, precision = 18, scale = 6)
    private BigDecimal fiyat;

    @Column(name = "toplam_tutar", nullable = false, precision = 18, scale = 2)
    private BigDecimal toplamTutar;

    @Column(name = "islem_tarihi", nullable = false)
    private LocalDateTime islemTarihi = LocalDateTime.now();

    @Column(name = "olusturma_tarihi", nullable = false)
    private LocalDateTime olusturmaTarihi = LocalDateTime.now();

    // getters setters
}