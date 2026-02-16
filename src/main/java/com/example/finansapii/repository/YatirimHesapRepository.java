package com.example.finansapii.repository;

import com.example.finansapii.entity.YatirimHesap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface YatirimHesapRepository extends JpaRepository<YatirimHesap, Long> {

    List<YatirimHesap> findByKullaniciId(Long kullaniciId);

    List<YatirimHesap> findByAileId(Long aileId);

    Optional<YatirimHesap> findByYatirimIdAndKullaniciId(Long yatirimId, Long kullaniciId);

    Optional<YatirimHesap> findByYatirimIdAndAileId(Long yatirimId, Long aileId);
}
