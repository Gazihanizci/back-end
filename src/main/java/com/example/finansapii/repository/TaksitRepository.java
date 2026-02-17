package com.example.finansapii.repository;
import java.util.Optional;

import com.example.finansapii.entity.Taksit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaksitRepository extends JpaRepository<Taksit, Long> {
    List<Taksit> findAllByKullaniciId(Long kullaniciId);
    List<Taksit> findAllByBittiMiFalse();
    Optional<Taksit> findByIdAndKullaniciId(Long id, Long kullaniciId);

}
