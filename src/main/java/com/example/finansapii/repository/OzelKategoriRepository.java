package com.example.finansapii.repository;

import com.example.finansapii.entity.OzelKategori;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OzelKategoriRepository extends JpaRepository<OzelKategori, Long> {

    List<OzelKategori> findAllByKullaniciIdOrderByIdDesc(Long kullaniciId);

    boolean existsByKullaniciIdAndKategoriAdiIgnoreCase(Long kullaniciId, String kategoriAdi);

    Optional<OzelKategori> findByIdAndKullaniciId(Long id, Long kullaniciId);
}
