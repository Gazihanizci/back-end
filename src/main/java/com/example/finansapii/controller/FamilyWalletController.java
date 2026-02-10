package com.example.finansapii.controller;

import com.example.finansapii.dto.FamilyWalletMonthlyResponse;
import com.example.finansapii.entity.User;
import com.example.finansapii.repository.UserRepository;
import com.example.finansapii.service.FamilyWalletService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/familywallet")
public class FamilyWalletController {

    private final FamilyWalletService familyWalletService;
    private final UserRepository userRepository;

    public FamilyWalletController(FamilyWalletService familyWalletService, UserRepository userRepository) {
        this.familyWalletService = familyWalletService;
        this.userRepository = userRepository;
    }

    // Örnek:
    // GET /api/familywallet/monthly?yilAy=2026-02
    // Header: X-USER-ID: 7
    @GetMapping("/monthly")
    public FamilyWalletMonthlyResponse monthly(
            @RequestHeader("X-USER-ID") Long kullaniciId,
            @RequestParam String yilAy
    ) {
        User u = userRepository.findById(kullaniciId)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı"));

        Long aileId = u.getAileId();
        if (aileId == null) {
            throw new IllegalArgumentException("Kullanıcının aile_id değeri yok");
        }

        return familyWalletService.getMonthly(aileId, yilAy);
    }

    // İstersen direkt aileId ile de çağırmak için opsiyonel endpoint:
    // GET /api/familywallet/monthlyByFamily?aileId=3&yilAy=2026-02
    @GetMapping("/monthlyByFamily")
    public FamilyWalletMonthlyResponse monthlyByFamily(
            @RequestParam Long aileId,
            @RequestParam String yilAy
    ) {
        return familyWalletService.getMonthly(aileId, yilAy);
    }
}
