package com.example.finansapii.service;

import com.example.finansapii.dto.*;
import com.example.finansapii.repository.YatirimGraphRepository;
import com.example.finansapii.security.CurrentUser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class YatirimGraphService {

    private final YatirimGraphRepository repo;

    public YatirimGraphService(YatirimGraphRepository repo) {
        this.repo = repo;
    }

    public YatirimGraphResponse getGraph(YatirimGroupBy groupBy) {
        Long kullaniciId = CurrentUser.id();

        // default
        YatirimGroupBy gb = (groupBy == null) ? YatirimGroupBy.HESAP : groupBy;

        List<YatirimGraphPointDto> points = switch (gb) {
            case HESAP -> repo.graphByHesap(kullaniciId);
            case VARLIK -> repo.graphByVarlik(kullaniciId);
        };

        Object[] t = repo.totals(kullaniciId);
        if (t == null || t.length < 3) {
            // Çok uç durum; yine de boş döndür
            return new YatirimGraphResponse(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, points);
        }

        BigDecimal toplamMaliyet = safeBigDecimal(t[0]);
        BigDecimal toplamGuncelDeger = safeBigDecimal(t[1]);
        BigDecimal toplamKarZarar = safeBigDecimal(t[2]);

        return new YatirimGraphResponse(toplamMaliyet, toplamGuncelDeger, toplamKarZarar, points);
    }

    private BigDecimal safeBigDecimal(Object o) {
        if (o == null) return BigDecimal.ZERO;
        if (o instanceof BigDecimal bd) return bd;
        try {
            return new BigDecimal(o.toString());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Toplamlar hesaplanamadı");
        }
    }
}
