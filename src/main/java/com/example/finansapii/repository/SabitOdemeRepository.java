package com.example.finansapii.repository;

import com.example.finansapii.entity.SabitOdeme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface SabitOdemeRepository extends JpaRepository<SabitOdeme, Long> {
    List<SabitOdeme> findByKullaniciIdOrderBySonOdemeGunuAsc(Long kullaniciId);
    Optional<SabitOdeme> findByOdemeIdAndKullaniciId(Long odemeId, Long kullaniciId);

    @Modifying
    @Query("update SabitOdeme s set s.aktif = :aktif where s.odemeId = :odemeId and s.kullaniciId = :kullaniciId")
    int setAktif(@Param("odemeId") Long odemeId,
                 @Param("kullaniciId") Long kullaniciId,
                 @Param("aktif") Boolean aktif);

}

