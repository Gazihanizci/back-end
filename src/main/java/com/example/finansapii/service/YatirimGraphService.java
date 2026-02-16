package com.example.finansapii.service;

import com.example.finansapii.dto.YatirimGraphResponse;
import com.example.finansapii.dto.YatirimGraphPointDto;
import com.example.finansapii.repository.YatirimHesapInfoRepository;
import com.example.finansapii.security.CurrentUser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class YatirimGraphService {

    private final YatirimHesapInfoRepository repo;

    public YatirimGraphService(YatirimHesapInfoRepository repo) {
        this.repo = repo;
    }

    public YatirimGraphResponse getGraph(String groupBy) {
        Long kullaniciId = CurrentUser.id();

        List<YatirimGraphPointDto> points = switch (groupBy == null ? "HESAP" : groupBy.toUpperCase()) {
            case "VARLIK" -> repo.graphByVarlik(kullaniciId);
            case "HESAP" -> repo.graphByHesap(kullaniciId);
            default -> throw new IllegalArgumentException("groupBy sadece HESAP veya VARLIK olabilir");
        };

        Object[] t = repo.totals(kullaniciId);
        BigDecimal toplamMaliyet = (BigDecimal) t[0];
        BigDecimal toplamGuncelDeger = (BigDecimal) t[1];
        BigDecimal toplamKarZarar = (BigDecimal) t[2];

        return new YatirimGraphResponse(toplamMaliyet, toplamGuncelDeger, toplamKarZarar, points);
    }
}
