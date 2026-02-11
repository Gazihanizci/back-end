package com.example.finansapii.controller;

import com.example.finansapii.dto.FamilyWalletMonthlyResponse;
import com.example.finansapii.entity.User;
import com.example.finansapii.repository.UserRepository;
import com.example.finansapii.security.CurrentUser;
import com.example.finansapii.security.FamilyAccess;
import com.example.finansapii.service.FamilyWalletService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/familywallet")
public class FamilyWalletController {

    private final FamilyWalletService familyWalletService;
    private final UserRepository userRepository;
    private final FamilyAccess familyAccess;

    public FamilyWalletController(
            FamilyWalletService familyWalletService,
            UserRepository userRepository,
            FamilyAccess familyAccess
    ) {
        this.familyWalletService = familyWalletService;
        this.userRepository = userRepository;
        this.familyAccess = familyAccess;
    }

    // GET /api/familywallet/monthly?yilAy=2026-02
    @GetMapping("/monthly")
    public FamilyWalletMonthlyResponse monthly(@RequestParam String yilAy) {
        Long kullaniciId = CurrentUser.id();

        User u = userRepository.findById(kullaniciId)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı"));

        Long aileId = u.getAileId();
        if (aileId == null) {
            throw new IllegalArgumentException("Kullanıcının aile_id değeri yok");
        }

        return familyWalletService.getMonthly(aileId, yilAy);
    }

    // ✅ Güvenli versiyon: aileId ile çağıracaksan membership kontrol şart
    // GET /api/familywallet/monthlyByFamily?aileId=3&yilAy=2026-02
    @GetMapping("/monthlyByFamily")
    public FamilyWalletMonthlyResponse monthlyByFamily(
            @RequestParam Long aileId,
            @RequestParam String yilAy
    ) {
        Long kullaniciId = CurrentUser.id();

        // ✅ kritik kontrol: bu user bu aileye ait mi?
        familyAccess.assertUserInFamily(kullaniciId, aileId);

        return familyWalletService.getMonthly(aileId, yilAy);
    }
}
