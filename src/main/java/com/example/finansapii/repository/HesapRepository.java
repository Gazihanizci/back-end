package com.example.finansapii.repository;

import com.example.finansapii.entity.Hesap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HesapRepository extends JpaRepository<Hesap, Long> {

    // ✅ Kullanıcının tüm hesapları
    List<Hesap> findAllByKullaniciIdOrderByCreatedAtAsc(Long kullaniciId);

    // ✅ (İstersen kalsın) ilk hesabı current gibi kullanmak için
    Optional<Hesap> findFirstByKullaniciIdOrderByCreatedAtAsc(Long kullaniciId);
    Optional<Hesap> findByHesapIdAndKullaniciId(Long hesapId, Long kullaniciId);
}
