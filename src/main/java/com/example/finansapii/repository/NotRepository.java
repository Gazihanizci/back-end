package com.example.finansapii.repository;

import com.example.finansapii.entity.Not;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotRepository extends JpaRepository<Not, Long> {

    List<Not> findByKullaniciIdOrderByNotIdDesc(Long kullaniciId);

    Optional<Not> findByNotIdAndKullaniciId(Long notId, Long kullaniciId);
}
