package com.example.finansapii.repository;

import com.example.finansapii.dto.FamilyWalletCategorySummaryView;
import com.example.finansapii.dto.FamilyWalletMemberCategoryView;
import com.example.finansapii.entity.Islem;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FamilyWalletIslemRepository extends JpaRepository<Islem, Long> {

    // ✅ Aile bazında kategori özet (hangi kategoriye ne kadar)
    @Query(value = """
        SELECT
          k.kategori_id AS kategoriId,
          k.kategori_ad AS kategoriAd,
          k.tip         AS tip,
          COALESCE(SUM(i.tutar),0) AS toplamTutar
        FROM islemler i
        JOIN kategoriler k ON k.kategori_id = i.kategori_id
        WHERE i.aile_id = :aileId
          AND i.islem_tarihi >= :startDt
          AND i.islem_tarihi <  :endDt
        GROUP BY k.kategori_id, k.kategori_ad, k.tip
        ORDER BY COALESCE(SUM(i.tutar),0) DESC
    """, nativeQuery = true)
    List<FamilyWalletCategorySummaryView> sumByCategoryFamilyMonthly(
            @Param("aileId") Long aileId,
            @Param("startDt") LocalDateTime startDt,
            @Param("endDt") LocalDateTime endDt
    );

    // ✅ Üye + kategori (kim hangi kategoriye ne kadar)
    @Query(value = """
        SELECT
          i.kullanici_id AS kullaniciId,
          k.kategori_id  AS kategoriId,
          k.kategori_ad  AS kategoriAd,
          k.tip          AS tip,
          COALESCE(SUM(i.tutar),0) AS toplamTutar
        FROM islemler i
        JOIN kategoriler k ON k.kategori_id = i.kategori_id
        WHERE i.aile_id = :aileId
          AND i.islem_tarihi >= :startDt
          AND i.islem_tarihi <  :endDt
        GROUP BY i.kullanici_id, k.kategori_id, k.kategori_ad, k.tip
        ORDER BY i.kullanici_id ASC, COALESCE(SUM(i.tutar),0) DESC
    """, nativeQuery = true)
    List<FamilyWalletMemberCategoryView> sumMemberByCategoryFamilyMonthly(
            @Param("aileId") Long aileId,
            @Param("startDt") LocalDateTime startDt,
            @Param("endDt") LocalDateTime endDt
    );
}
