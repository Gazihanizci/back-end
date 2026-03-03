package com.example.finansapii.repository;

import com.example.finansapii.entity.YatirimPortfoy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface YatirimPortfoyRepository extends JpaRepository<YatirimPortfoy, Long> {

    Optional<YatirimPortfoy> findByKullaniciIdAndVarlikTuru(Long kullaniciId, String varlikTuru);
}