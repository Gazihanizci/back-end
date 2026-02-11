package com.example.finansapii.controller;

import com.example.finansapii.dto.AileCreateRequest;
import com.example.finansapii.dto.AileResponse;
import com.example.finansapii.security.CurrentUser;
import com.example.finansapii.service.AileService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/aileler")
public class AileController {

    private final AileService aileService;

    public AileController(AileService aileService) {
        this.aileService = aileService;
    }

    // ✅ Aile Hesabı Oluştur + Parola aynı anda
    @PostMapping
    public AileResponse create(@Valid @RequestBody AileCreateRequest req) {
        Long kullaniciId = CurrentUser.id();
        return aileService.create(kullaniciId, req);
    }
}
