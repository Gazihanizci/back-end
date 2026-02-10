package com.example.finansapii.service;

import com.example.finansapii.dto.AylikAnalizResponse;
import com.example.finansapii.entity.AylikAnaliz;
import com.example.finansapii.repository.AylikAnalizRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Service
public class AylikAnalizService {

    private final AylikAnalizRepository repo;

    public AylikAnalizService(AylikAnalizRepository repo) {
        this.repo = repo;
    }

    public List<AylikAnalizResponse> getForUser(Long kullaniciId) {
        YearMonth now = YearMonth.now();          // örn 2026-02
        YearMonth startYm = now.minusMonths(5);   // son 6 ayın başlangıcı

        // yilAy LocalDate tutulduğu için ayın 1’i gibi çalışıyoruz
        LocalDate start = startYm.atDay(1);
        LocalDate end = now.atEndOfMonth();

        List<AylikAnaliz> rows =
                repo.findAllByKullaniciIdAndYilAyBetweenOrderByYilAyAsc(kullaniciId, start, end);

        // DB’den gelenleri YearMonth’a map’le
        Map<YearMonth, AylikAnaliz> map = new HashMap<>();
        for (AylikAnaliz a : rows) {
            YearMonth ym = YearMonth.from(a.getYilAy()); // yilAy -> 2026-02
            map.put(ym, a);
        }

        // ✅ Tam 6 ay üret, yoksa 0 yaz
        List<AylikAnalizResponse> out = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            YearMonth ym = startYm.plusMonths(i);

            AylikAnaliz found = map.get(ym);

            BigDecimal gelir = (found == null || found.getAylikGelir() == null)
                    ? BigDecimal.ZERO
                    : found.getAylikGelir();

            BigDecimal gider = (found == null || found.getAylikGider() == null)
                    ? BigDecimal.ZERO
                    : found.getAylikGider();

            out.add(new AylikAnalizResponse(
                    ym.toString(), // "2026-02"
                    gelir,
                    gider
            ));
        }

        return out;
    }
}
