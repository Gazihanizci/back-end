package com.example.finansapii.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "aylik_analiz")
public class AylikAnaliz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analiz_id")
    private Long analizId;

    @Column(name = "kullanici_id", nullable = false)
    private Long kullaniciId;
    @Column(name = "aile_id")
    private Long aileId;

    public Long getAileId() { return aileId; }
    public void setAileId(Long aileId) { this.aileId = aileId; }

    @Column(name = "yil_ay", nullable = false)
    private LocalDate yilAy;

    @Column(name = "aylik_gelir", nullable = false)
    private BigDecimal aylikGelir;

    @Column(name = "aylik_gider", nullable = false)
    private BigDecimal aylikGider;

    @PrePersist
    public void prePersist() {
        if (yilAy == null) yilAy = LocalDate.now().withDayOfMonth(1);
        if (aylikGelir == null) aylikGelir = BigDecimal.ZERO;
        if (aylikGider == null) aylikGider = BigDecimal.ZERO;
    }

    // --- getters ---
    public Long getAnalizId() { return analizId; }
    public Long getKullaniciId() { return kullaniciId; }
    public LocalDate getYilAy() { return yilAy; }
    public BigDecimal getAylikGelir() { return aylikGelir; }
    public BigDecimal getAylikGider() { return aylikGider; }

    // ✅ setters (kayıt atabilmek için gerekli)
    public void setKullaniciId(Long kullaniciId) { this.kullaniciId = kullaniciId; }
    public void setYilAy(LocalDate yilAy) { this.yilAy = yilAy; }
    public void setAylikGelir(BigDecimal aylikGelir) { this.aylikGelir = aylikGelir; }
    public void setAylikGider(BigDecimal aylikGider) { this.aylikGider = aylikGider; }
}
