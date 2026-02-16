package com.example.finansapii.dto;

import java.math.BigDecimal;

/**
 * adetDegisim:
 *  +10.0 => ekleme
 *  -5.0  => çıkarma
 *
 * alisFiyati:
 *  ekleme yapıyorsan zorunlu (ortalama maliyet güncellemek için)
 *  çıkarma yapıyorsan null olabilir
 */
public class YatirimAdetDegisRequest {
    private BigDecimal adetDegisim;
    private BigDecimal alisFiyati; // sadece ekleme için

    public BigDecimal getAdetDegisim() { return adetDegisim; }
    public void setAdetDegisim(BigDecimal adetDegisim) { this.adetDegisim = adetDegisim; }

    public BigDecimal getAlisFiyati() { return alisFiyati; }
    public void setAlisFiyati(BigDecimal alisFiyati) { this.alisFiyati = alisFiyati; }
}
