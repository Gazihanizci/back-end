package com.example.finansapii.controller;

import com.example.finansapii.dto.SabitOdemeCreateRequest;
import com.example.finansapii.dto.SabitOdemeResponse;
import com.example.finansapii.security.CurrentUser;
import com.example.finansapii.service.SabitOdemeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sabitodemeler")
public class SabitOdemeController {

    private final SabitOdemeService service;

    public SabitOdemeController(SabitOdemeService service) {
        this.service = service;
    }

    // POST /api/sabitodemeler?aileId=3 (opsiyonel)
    @PostMapping
    public SabitOdemeResponse create(
            @RequestParam(required = false) Long aileId,
            @Valid @RequestBody SabitOdemeCreateRequest req
    ) {
        Long kullaniciId = CurrentUser.id(); // ✅ JWT'den geliyor
        return service.create(kullaniciId, aileId, req);
    }

    // GET /api/sabitodemeler
    @GetMapping
    public List<SabitOdemeResponse> listMine() {
        Long kullaniciId = CurrentUser.id(); // ✅ JWT'den geliyor
        return service.listMine(kullaniciId);
    }
}
