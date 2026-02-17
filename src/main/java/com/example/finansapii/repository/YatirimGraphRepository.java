package com.example.finansapii.repository;

import com.example.finansapii.dto.YatirimGraphPointDto;
import com.example.finansapii.entity.YatirimHesap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface YatirimGraphRepository extends JpaRepository<YatirimHesap, Long> {

    // HESAP'a göre grupla
    @Query("""
        select new com.example.finansapii.dto.YatirimGraphPointDto(
            y.hesapAdi,
            coalesce(sum(y.toplamMaliyet), 0),
            coalesce(sum(y.guncelDeger), 0),
            coalesce(sum(y.karZarar), 0)
        )
        from YatirimHesap y
        where y.kullaniciId = :kullaniciId
        group by y.hesapAdi
        order by coalesce(sum(y.karZarar), 0) desc
    """)
    List<YatirimGraphPointDto> graphByHesap(Long kullaniciId);

    // VARLIK'a göre grupla
    @Query("""
        select new com.example.finansapii.dto.YatirimGraphPointDto(
            y.varlikTuru,
            coalesce(sum(y.toplamMaliyet), 0),
            coalesce(sum(y.guncelDeger), 0),
            coalesce(sum(y.karZarar), 0)
        )
        from YatirimHesap y
        where y.kullaniciId = :kullaniciId
        group by y.varlikTuru
        order by coalesce(sum(y.karZarar), 0) desc
    """)
    List<YatirimGraphPointDto> graphByVarlik(Long kullaniciId);

    // Totals
    @Query("""
        select
            coalesce(sum(y.toplamMaliyet), 0),
            coalesce(sum(y.guncelDeger), 0),
            coalesce(sum(y.karZarar), 0)
        from YatirimHesap y
        where y.kullaniciId = :kullaniciId
    """)
    Object[] totals(Long kullaniciId);

    // (Opsiyonel) hiç kayıt var mı
    boolean existsByKullaniciId(Long kullaniciId);
}
