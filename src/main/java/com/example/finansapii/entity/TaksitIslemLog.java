package com.example.finansapii.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "taksit_islem_log",
        uniqueConstraints = @UniqueConstraint(name = "uq_taksit_vade", columnNames = {"taksit_id", "vade_tarihi"}))
public class TaksitIslemLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long id;

    @Column(name = "taksit_id", nullable = false)
    private Long taksitId;

    @Column(name = "vade_tarihi", nullable = false)
    private LocalDate vadeTarihi;

    @Column(name = "taksit_no", nullable = false)
    private Integer taksitNo; // 1..N

    @Column(name = "islem_id")
    private Long islemId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    // getters/setters
    public Long getId() { return id; }

    public Long getTaksitId() { return taksitId; }
    public void setTaksitId(Long taksitId) { this.taksitId = taksitId; }

    public LocalDate getVadeTarihi() { return vadeTarihi; }
    public void setVadeTarihi(LocalDate vadeTarihi) { this.vadeTarihi = vadeTarihi; }

    public Integer getTaksitNo() { return taksitNo; }
    public void setTaksitNo(Integer taksitNo) { this.taksitNo = taksitNo; }

    public Long getIslemId() { return islemId; }
    public void setIslemId(Long islemId) { this.islemId = islemId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
