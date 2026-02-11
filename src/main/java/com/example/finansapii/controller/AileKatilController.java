package com.example.finansapii.controller;

import com.example.finansapii.dto.AileKatilRequest;
import com.example.finansapii.security.CurrentUser;
import com.example.finansapii.service.AileKatilService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ailekatil")
public class AileKatilController {

    private final AileKatilService aileKatilService;

    public AileKatilController(AileKatilService aileKatilService) {
        this.aileKatilService = aileKatilService;
    }

    @PostMapping
    public void katil(@Valid @RequestBody AileKatilRequest req) {
        Long kullaniciId = CurrentUser.id();
        aileKatilService.katil(kullaniciId, req);
    }
}
