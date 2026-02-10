package com.example.finansapii.repository;

import com.example.finansapii.entity.Aile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface    AileRepository extends JpaRepository<Aile, Long> {
    Optional<Aile> findByAileSahibiKullaniciId(Long kullaniciId);
}
