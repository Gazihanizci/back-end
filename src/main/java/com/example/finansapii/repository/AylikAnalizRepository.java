package com.example.finansapii.repository;
import com.example.finansapii.dto.FamilyWalletMemberMonthlyView;
import com.example.finansapii.entity.AylikAnaliz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface AylikAnalizRepository extends JpaRepository<AylikAnaliz, Long> {

    // Eski metod kalsın
    List<AylikAnaliz> findAllByKullaniciIdOrderByYilAyAsc(Long kullaniciId);

    // ✅ Son 6 ay için aralık sorgusu
    List<AylikAnaliz> findAllByKullaniciIdAndYilAyBetweenOrderByYilAyAsc(
            Long kullaniciId,
            LocalDate startInclusive,
            LocalDate endInclusive
    );
    @Query(value = """
        SELECT 
          a.kullanici_id AS kullaniciId,
          COALESCE(SUM(a.aylik_gelir),0) AS aylikGelir,
          COALESCE(SUM(a.aylik_gider),0) AS aylikGider
        FROM aylik_analiz a
        WHERE a.aile_id = :aileId
          AND a.yil_ay >= :startDate
          AND a.yil_ay <  :endDate
        GROUP BY a.kullanici_id
        ORDER BY a.kullanici_id
    """, nativeQuery = true)
    List<FamilyWalletMemberMonthlyView> sumMemberMonthlyByFamily(
            @Param("aileId") Long aileId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}
