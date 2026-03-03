package com.example.finansapii.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class IslemRequest {
    @NotBlank
    private String varlikTuru;

    @NotBlank
    private String tip;

    @NotNull
    @Positive
    private BigDecimal adet;

    @NotNull
    @Positive
    private BigDecimal fiyat;
    public String getVarlikTuru() { return varlikTuru; }
    public void setVarlikTuru(String varlikTuru) { this.varlikTuru = varlikTuru; }

    public String getTip() { return tip; }
    public void setTip(String tip) { this.tip = tip; }

    public BigDecimal getAdet() { return adet; }
    public void setAdet(BigDecimal adet) { this.adet = adet; }

    public BigDecimal getFiyat() { return fiyat; }
    public void setFiyat(BigDecimal fiyat) { this.fiyat = fiyat; }
}