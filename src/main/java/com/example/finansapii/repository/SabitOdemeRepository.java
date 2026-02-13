package com.example.finansapii.repository;

import com.example.finansapii.entity.SabitOdeme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SabitOdemeRepository extends JpaRepository<SabitOdeme, Long> {
    List<SabitOdeme> findByKullaniciIdOrderBySonOdemeGunuAsc(Long kullaniciId);

}

