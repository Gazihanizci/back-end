package com.example.finansapii.scheduler;

import com.example.finansapii.service.TaksitService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TaksitScheduler {

    private final TaksitService taksitService;

    public TaksitScheduler(TaksitService taksitService) {
        this.taksitService = taksitService;
    }

    // Her gün 00:05'te çalışır (Türkiye saati server ayarına bağlı)
    @Scheduled(cron = "0 5 0 * * *")
    public void createDueInstallments() {
        taksitService.runDueInstallments(LocalDate.now());
    }
}
