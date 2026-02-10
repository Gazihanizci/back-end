package com.example.finansapii.dto;

import java.math.BigDecimal;
import java.util.List;

public record FamilyWalletMonthlyResponse(
        Long aileId,
        String yilAy, // "2026-02"
        BigDecimal aileToplamGelir,
        BigDecimal aileToplamGider,
        BigDecimal aileNet,

        List<MemberMonthly> uyelerAylik,
        List<CategorySummary> kategoriOzet,
        List<MemberCategorySummary> uyeKategoriDagilim // bonus
) {
    public record MemberMonthly(
            Long kullaniciId,
            String adSoyad,
            BigDecimal aylikGelir,
            BigDecimal aylikGider,
            BigDecimal net
    ) {}

    public record CategorySummary(
            Long kategoriId,
            String kategoriAd,
            String tip,
            BigDecimal toplamTutar
    ) {}

    public record MemberCategorySummary(
            Long kullaniciId,
            String adSoyad,
            Long kategoriId,
            String kategoriAd,
            String tip,
            BigDecimal toplamTutar
    ) {}
}
