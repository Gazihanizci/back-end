package com.example.finansapii.service;

import com.example.finansapii.entity.YatirimPortfoy;
import com.example.finansapii.repository.PiyasaFiyatRepository;
import com.example.finansapii.repository.YatirimPortfoyRepository;
import com.example.finansapii.security.CurrentUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class YatirimMotorService {

    private final YatirimPortfoyRepository portfoyRepo;
    private final PiyasaFiyatRepository piyasaRepo;

    public YatirimMotorService(YatirimPortfoyRepository portfoyRepo,
                               PiyasaFiyatRepository piyasaRepo) {
        this.portfoyRepo = portfoyRepo;
        this.piyasaRepo = piyasaRepo;
    }

    @Transactional
    public YatirimPortfoy islemYap(String varlikTuru,
                                   String tip,
                                   BigDecimal adet,
                                   BigDecimal fiyat) {

        // ======================
        // VALIDATION
        // ======================
        if (varlikTuru == null || tip == null)
            throw new IllegalArgumentException("Varlık türü ve tip zorunludur.");

        if (adet == null || fiyat == null)
            throw new IllegalArgumentException("Adet ve fiyat zorunludur.");

        if (adet.compareTo(BigDecimal.ZERO) <= 0 ||
                fiyat.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Adet ve fiyat pozitif olmalıdır.");

        Long userId = CurrentUser.id();

        // ======================
        // PORTFÖY BUL / OLUŞTUR
        // ======================
        YatirimPortfoy portfoy = portfoyRepo
                .findByKullaniciIdAndVarlikTuru(userId, varlikTuru)
                .orElseGet(() -> {
                    YatirimPortfoy p = new YatirimPortfoy();
                    p.setKullaniciId(userId);
                    p.setVarlikTuru(varlikTuru);
                    p.setToplamAdet(BigDecimal.ZERO);
                    p.setToplamMaliyet(BigDecimal.ZERO);
                    p.setOrtalamaMaliyet(BigDecimal.ZERO);
                    p.setRealizedKar(BigDecimal.ZERO);
                    p.setUnrealizedKar(BigDecimal.ZERO);
                    p.setGuncelDeger(BigDecimal.ZERO);
                    return p;
                });

        // ======================
        // ALIŞ
        // ======================
        if (tip.equalsIgnoreCase("ALIS")) {

            BigDecimal newToplamAdet =
                    portfoy.getToplamAdet().add(adet);

            BigDecimal newToplamMaliyet =
                    portfoy.getToplamMaliyet().add(adet.multiply(fiyat));

            BigDecimal newOrtalama =
                    newToplamMaliyet.divide(newToplamAdet, 6, RoundingMode.HALF_UP);

            portfoy.setToplamAdet(newToplamAdet);
            portfoy.setToplamMaliyet(newToplamMaliyet);
            portfoy.setOrtalamaMaliyet(newOrtalama);
        }

        // ======================
        // SATIŞ
        // ======================
        else if (tip.equalsIgnoreCase("SATIS")) {

            if (adet.compareTo(portfoy.getToplamAdet()) > 0)
                throw new IllegalArgumentException("Satış miktarı mevcut adetten büyük olamaz.");

            BigDecimal realized =
                    fiyat.subtract(portfoy.getOrtalamaMaliyet())
                            .multiply(adet);

            portfoy.setRealizedKar(
                    portfoy.getRealizedKar().add(realized));

            BigDecimal newAdet =
                    portfoy.getToplamAdet().subtract(adet);

            BigDecimal newToplamMaliyet =
                    portfoy.getOrtalamaMaliyet().multiply(newAdet);

            portfoy.setToplamAdet(newAdet);
            portfoy.setToplamMaliyet(newToplamMaliyet);
        }

        else {
            throw new IllegalArgumentException("Tip ALIS veya SATIS olmalıdır.");
        }

        // ======================
        // MARKET GÜNCELLEME
        // ======================
        updateMarketProfit(portfoy);

        return portfoyRepo.save(portfoy);
    }

    private void updateMarketProfit(YatirimPortfoy portfoy) {

        BigDecimal guncelFiyat =
                piyasaRepo.findLatestPrice(portfoy.getVarlikTuru());

        if (guncelFiyat == null)
            guncelFiyat = BigDecimal.ZERO;

        BigDecimal guncelDeger =
                portfoy.getToplamAdet().multiply(guncelFiyat);

        BigDecimal unrealized =
                guncelDeger.subtract(portfoy.getToplamMaliyet());

        portfoy.setGuncelDeger(guncelDeger.setScale(2, RoundingMode.HALF_UP));
        portfoy.setUnrealizedKar(unrealized.setScale(2, RoundingMode.HALF_UP));
    }
}