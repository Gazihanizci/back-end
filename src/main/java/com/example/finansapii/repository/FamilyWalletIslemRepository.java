package com.example.finansapii.repository;

import com.example.finansapii.dto.FamilyWalletCategorySummaryView;
import com.example.finansapii.dto.FamilyWalletMemberCategoryView;
import com.example.finansapii.entity.Islem;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FamilyWalletIslemRepository extends JpaRepository<Islem, Long> {

    // ✅ Aile bazında kategori özet (normal + özel)
    @Query(value = """
        SELECT
          i.kategori_id AS kategoriId,
          COALESCE(k.kategori_ad, ok.kategori_adi, i.kategori_adi_snapshot) AS kategoriAd,
          COALESCE(k.tip, ok.tip, 'UNKNOWN') AS tip,
          COALESCE(SUM(i.tutar),0) AS toplamTutar
        FROM islemler i
        LEFT JOIN kategoriler k
               ON k.kategori_id = i.kategori_id
        LEFT JOIN ozel_kategoriler ok
               ON ok.ozel_kategori_id = i.kategori_id
        WHERE i.aile_id = :aileId
          AND i.islem_tarihi >= :startDt
          AND i.islem_tarihi <  :endDt
        GROUP BY
          i.kategori_id,
          COALESCE(k.kategori_ad, ok.kategori_adi, i.kategori_adi_snapshot),
          COALESCE(k.tip, ok.tip, 'UNKNOWN')
        ORDER BY COALESCE(SUM(i.tutar),0) DESC
    """, nativeQuery = true)
    List<FamilyWalletCategorySummaryView> sumByCategoryFamilyMonthly(
            @Param("aileId") Long aileId,
            @Param("startDt") LocalDateTime startDt,
            @Param("endDt") LocalDateTime endDt
    );

    // ✅ Üye + kategori (normal + özel)
    @Query(value = """
        SELECT
          i.kullanici_id AS kullaniciId,
          i.kategori_id  AS kategoriId,
          COALESCE(k.kategori_ad, ok.kategori_adi, i.kategori_adi_snapshot) AS kategoriAd,
          COALESCE(k.tip, ok.tip, 'UNKNOWN') AS tip,
          COALESCE(SUM(i.tutar),0) AS toplamTutar
        FROM islemler i
        LEFT JOIN kategoriler k
               ON k.kategori_id = i.kategori_id
        LEFT JOIN ozel_kategoriler ok
               ON ok.ozel_kategori_id = i.kategori_id
        WHERE i.aile_id = :aileId
          AND i.islem_tarihi >= :startDt
          AND i.islem_tarihi <  :endDt
        GROUP BY
          i.kullanici_id,
          i.kategori_id,
          COALESCE(k.kategori_ad, ok.kategori_adi, i.kategori_adi_snapshot),
          COALESCE(k.tip, ok.tip, 'UNKNOWN')
        ORDER BY i.kullanici_id ASC, COALESCE(SUM(i.tutar),0) DESC
    """, nativeQuery = true)
    List<FamilyWalletMemberCategoryView> sumMemberByCategoryFamilyMonthly(
            @Param("aileId") Long aileId,
            @Param("startDt") LocalDateTime startDt,
            @Param("endDt") LocalDateTime endDt
    );
}
