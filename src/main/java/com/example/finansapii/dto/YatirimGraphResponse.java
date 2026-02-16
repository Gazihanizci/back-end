package com.example.finansapii.dto;

import java.math.BigDecimal;
import java.util.List;

public class YatirimGraphResponse {
    private BigDecimal toplamMaliyet;
    private BigDecimal toplamGuncelDeger;
    private BigDecimal toplamKarZarar;
    private List<YatirimGraphPointDto> points;

    public YatirimGraphResponse(BigDecimal toplamMaliyet,
                                BigDecimal toplamGuncelDeger,
                                BigDecimal toplamKarZarar,
                                List<YatirimGraphPointDto> points) {
        this.toplamMaliyet = toplamMaliyet;
        this.toplamGuncelDeger = toplamGuncelDeger;
        this.toplamKarZarar = toplamKarZarar;
        this.points = points;
    }

    public BigDecimal getToplamMaliyet() { return toplamMaliyet; }
    public BigDecimal getToplamGuncelDeger() { return toplamGuncelDeger; }
    public BigDecimal getToplamKarZarar() { return toplamKarZarar; }
    public List<YatirimGraphPointDto> getPoints() { return points; }
}
