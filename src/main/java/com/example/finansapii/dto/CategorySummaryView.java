package com.example.finansapii.dto;

import java.math.BigDecimal;

public interface CategorySummaryView {
    Long getKategoriId();
    String getKategoriAd();
    String getTip();          // GELIR / GIDER
    BigDecimal getToplamTutar();
}
