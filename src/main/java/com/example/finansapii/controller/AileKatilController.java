package com.example.finansapii.controller;

import com.example.finansapii.dto.AileKatilRequest;
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
    public void katil(
            @RequestHeader("X-USER-ID") Long kullaniciId,
            @Valid @RequestBody AileKatilRequest req
    ) {
        aileKatilService.katil(kullaniciId, req);
    }
}
