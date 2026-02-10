package com.example.finansapii.dto;

import java.math.BigDecimal;

public interface FamilyWalletMemberMonthlyView {
    Long getKullaniciId();
    BigDecimal getAylikGelir();
    BigDecimal getAylikGider();
}
