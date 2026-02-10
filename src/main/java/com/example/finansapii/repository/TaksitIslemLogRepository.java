package com.example.finansapii.repository;

import com.example.finansapii.entity.TaksitIslemLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface TaksitIslemLogRepository extends JpaRepository<TaksitIslemLog, Long> {
    boolean existsByTaksitIdAndVadeTarihi(Long taksitId, LocalDate vadeTarihi);
}
