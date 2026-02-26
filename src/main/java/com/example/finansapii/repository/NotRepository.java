package com.example.finansapii.repository;

import com.example.finansapii.entity.Not;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotRepository extends JpaRepository<Not, Long> {

    // USER notları
    List<Not> findByKullaniciIdOrderByNotIdDesc(Long kullaniciId);
    Optional<Not> findByNotIdAndKullaniciId(Long notId, Long kullaniciId);

    // FAMILY notları
    List<Not> findByAileIdOrderByNotIdDesc(Long aileId);
    Optional<Not> findByNotIdAndAileId(Long notId, Long aileId);
}