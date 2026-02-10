package com.example.finansapii.dto;

import java.math.BigDecimal;

public interface FamilyWalletMemberCategoryView {
    Long getKullaniciId();
    Long getKategoriId();
    String getKategoriAd();
    String getTip();          // GELIR / GIDER
    BigDecimal getToplamTutar();
}
