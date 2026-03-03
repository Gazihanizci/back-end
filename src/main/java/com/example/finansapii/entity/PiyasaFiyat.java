package com.example.finansapii.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "piyasa_fiyat")
public class PiyasaFiyat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "piyasa_fiyat_id")
    private Long piyasaFiyatId;

    @Column(name = "piyasa_id", nullable = false)
    private Long piyasaId;

    @Column(name = "guncel_fiyat", nullable = false, precision = 18, scale = 6)
    private BigDecimal guncelFiyat;

    @Column(name = "para_birimi", nullable = false, length = 10)
    private String paraBirimi;

    @Column(name = "zaman", nullable = false)
    private LocalDateTime zaman;

    @Column(name = "kaynak")
    private String kaynak;

    // getters setters
}