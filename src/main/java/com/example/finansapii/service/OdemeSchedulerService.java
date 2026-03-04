package com.example.finansapii.service;

import com.example.finansapii.repository.SabitOdemeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OdemeSchedulerService {

    private final SabitOdemeRepository repo;

    public OdemeSchedulerService(SabitOdemeRepository repo) {
        this.repo = repo;
    }

    @Transactional
    @Scheduled(cron = "0 50 10 * * *", zone = "Europe/Istanbul") // her gün saat 01:00
    public void sonTarihiGecenleriPasifYap() {

        int guncellenen = repo.pasifYapSonTarihiGecenler();

        System.out.println("Pasif yapılan sabit ödeme sayısı: " + guncellenen);
    }
}