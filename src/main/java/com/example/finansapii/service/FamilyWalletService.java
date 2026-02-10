package com.example.finansapii.service;

import com.example.finansapii.dto.*;
import com.example.finansapii.entity.User;
import com.example.finansapii.repository.AylikAnalizRepository;
import com.example.finansapii.repository.FamilyWalletIslemRepository;
import com.example.finansapii.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FamilyWalletService {

    private final AylikAnalizRepository aylikAnalizRepository;
    private final FamilyWalletIslemRepository familyWalletIslemRepository;
    private final UserRepository userRepository;

    public FamilyWalletService(
            AylikAnalizRepository aylikAnalizRepository,
            FamilyWalletIslemRepository familyWalletIslemRepository,
            UserRepository userRepository
    ) {
        this.aylikAnalizRepository = aylikAnalizRepository;
        this.familyWalletIslemRepository = familyWalletIslemRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public FamilyWalletMonthlyResponse getMonthly(Long aileId, String yilAy) {
        // "2026-02"
        YearMonth ym = YearMonth.parse(yilAy);

        // aylik_analiz.yil_ay alanı DATE olduğu için LocalDate aralığı
        LocalDate startDate = ym.atDay(1);
        LocalDate endDate = ym.plusMonths(1).atDay(1);

        // islemler.islem_tarihi DATETIME olduğu için LocalDateTime aralığı
        LocalDateTime startDt = startDate.atStartOfDay();
        LocalDateTime endDt = endDate.atStartOfDay();

        // ✅ SENİN REPO'YA GÖRE: findAllByAileId
        List<User> members = userRepository.findAllByAileId(aileId);

        Map<Long, String> nameByUserId = members.stream()
                .collect(Collectors.toMap(
                        User::getId,
                        u -> (safe(u.getAd()) + " " + safe(u.getSoyad())).trim(),
                        (a, b) -> a
                ));

        // ✅ 1) aylik_analiz -> üye bazında gelir/gider
        List<FamilyWalletMemberMonthlyView> memberMonthly =
                aylikAnalizRepository.sumMemberMonthlyByFamily(aileId, startDate, endDate);

        BigDecimal aileToplamGelir = BigDecimal.ZERO;
        BigDecimal aileToplamGider = BigDecimal.ZERO;

        List<FamilyWalletMonthlyResponse.MemberMonthly> memberMonthlyOut = new ArrayList<>();
        for (FamilyWalletMemberMonthlyView v : memberMonthly) {
            BigDecimal gelir = nz(v.getAylikGelir());
            BigDecimal gider = nz(v.getAylikGider());
            BigDecimal net = gelir.subtract(gider);

            aileToplamGelir = aileToplamGelir.add(gelir);
            aileToplamGider = aileToplamGider.add(gider);

            memberMonthlyOut.add(new FamilyWalletMonthlyResponse.MemberMonthly(
                    v.getKullaniciId(),
                    nameByUserId.getOrDefault(v.getKullaniciId(), "Üye " + v.getKullaniciId()),
                    gelir,
                    gider,
                    net
            ));
        }

        // ✅ 2) islemler + kategoriler -> aile kategori özeti
        List<FamilyWalletCategorySummaryView> categorySum =
                familyWalletIslemRepository.sumByCategoryFamilyMonthly(aileId, startDt, endDt);

        List<FamilyWalletMonthlyResponse.CategorySummary> categoryOut = categorySum.stream()
                .map(v -> new FamilyWalletMonthlyResponse.CategorySummary(
                        v.getKategoriId(),
                        v.getKategoriAd(),
                        v.getTip(),
                        nz(v.getToplamTutar())
                ))
                .toList();

        // ✅ 3) islemler + kategoriler -> üye + kategori dağılımı (kim nereye ne kadar)
        List<FamilyWalletMemberCategoryView> memberCategory =
                familyWalletIslemRepository.sumMemberByCategoryFamilyMonthly(aileId, startDt, endDt);

        List<FamilyWalletMonthlyResponse.MemberCategorySummary> memberCategoryOut = memberCategory.stream()
                .map(v -> new FamilyWalletMonthlyResponse.MemberCategorySummary(
                        v.getKullaniciId(),
                        nameByUserId.getOrDefault(v.getKullaniciId(), "Üye " + v.getKullaniciId()),
                        v.getKategoriId(),
                        v.getKategoriAd(),
                        v.getTip(),
                        nz(v.getToplamTutar())
                ))
                .toList();

        BigDecimal aileNet = aileToplamGelir.subtract(aileToplamGider);

        return new FamilyWalletMonthlyResponse(
                aileId,
                yilAy,
                aileToplamGelir,
                aileToplamGider,
                aileNet,
                memberMonthlyOut,
                categoryOut,
                memberCategoryOut
        );
    }

    private static BigDecimal nz(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}
