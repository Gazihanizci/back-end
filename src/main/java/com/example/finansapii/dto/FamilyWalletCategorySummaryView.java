package com.example.finansapii.dto;

import java.math.BigDecimal;

public interface FamilyWalletCategorySummaryView {
    Long getKategoriId();
    String getKategoriAd();
    String getTip();          // GELIR / GIDER
    BigDecimal getToplamTutar();
}
