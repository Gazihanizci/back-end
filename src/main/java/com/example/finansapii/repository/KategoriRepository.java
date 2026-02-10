package com.example.finansapii.repository;

import com.example.finansapii.entity.Kategori;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KategoriRepository extends JpaRepository<Kategori, Long> {
    // KategoriRepository içine:
    java.util.Optional<com.example.finansapii.entity.Kategori> findByKategoriAdAndTip(String kategoriAd, String tip);

}



