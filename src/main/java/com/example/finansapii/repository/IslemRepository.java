package com.example.finansapii.repository;

import com.example.finansapii.dto.CategorySummaryView;
import com.example.finansapii.entity.Islem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IslemRepository extends JpaRepository<Islem, Long> {

    // ✅ aile_id fark etmeksizin: sadece kullanici_id + tarih aralığı
    @Query(value = """
        SELECT 
          k.kategori_id  AS kategoriId,
          k.kategori_ad  AS kategoriAd,
          k.tip          AS tip,
          SUM(i.tutar)   AS toplamTutar
        FROM islemler i
        JOIN kategoriler k ON k.kategori_id = i.kategori_id
        WHERE i.kullanici_id = :kullaniciId
          AND i.islem_tarihi >= :start
          AND i.islem_tarihi <  :end
        GROUP BY k.kategori_id, k.kategori_ad, k.tip
        ORDER BY SUM(i.tutar) DESC
    """, nativeQuery = true)
    List<CategorySummaryView> sumByCategoryUserMonthly(
            @Param("kullaniciId") Long kullaniciId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // ✅ EK: kullanıcıya göre tüm işlemleri sayfalı çekme (sıralama Pageable'dan gelecek)
    Page<Islem> findByKullaniciId(Long kullaniciId, Pageable pageable);
}
