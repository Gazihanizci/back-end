package com.example.finansapii.repository;

import com.example.finansapii.dto.YatirimGraphPointDto;
import com.example.finansapii.entity.YatirimHesap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface YatirimHesapInfoRepository extends JpaRepository<YatirimHesap, Long> {

    @Query("""
        select new com.example.finansapii.dto.YatirimGraphPointDto(
            y.hesapAdi,
            sum(y.toplamMaliyet),
            sum(y.guncelDeger),
            sum(y.karZarar)
        )
        from YatirimHesap y
        where y.kullaniciId = :kullaniciId
        group by y.hesapAdi
        order by sum(y.karZarar) desc
    """)
    List<YatirimGraphPointDto> graphByHesap(Long kullaniciId);

    @Query("""
        select new com.example.finansapii.dto.YatirimGraphPointDto(
            y.varlikTuru,
            sum(y.toplamMaliyet),
            sum(y.guncelDeger),
            sum(y.karZarar)
        )
        from YatirimHesap y
        where y.kullaniciId = :kullaniciId
        group by y.varlikTuru
        order by sum(y.karZarar) desc
    """)
    List<YatirimGraphPointDto> graphByVarlik(Long kullaniciId);

    @Query("""
        select coalesce(sum(y.toplamMaliyet),0),
               coalesce(sum(y.guncelDeger),0),
               coalesce(sum(y.karZarar),0)
        from YatirimHesap y
        where y.kullaniciId = :kullaniciId
    """)
    Object[] totals(Long kullaniciId);
}
