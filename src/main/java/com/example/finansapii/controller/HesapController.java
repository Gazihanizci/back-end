package com.example.finansapii.controller;
import com.example.finansapii.dto.HesapBakiyeUpdateRequest;

import com.example.finansapii.dto.HesapCreateRequest;
import com.example.finansapii.dto.HesapResponse;
import com.example.finansapii.service.HesapService;
import jakarta.validation.Valid;
import java.util.List;

import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/hesaplar")
public class HesapController {

    private final HesapService hesapService;

    public HesapController(HesapService hesapService) {
        this.hesapService = hesapService;
    }

    @PostMapping
    public HesapResponse create(
            @RequestHeader("X-USER-ID") Long kullaniciId,
            @Valid @RequestBody HesapCreateRequest req
    ) {
        return hesapService.create(kullaniciId, req);
    }

    @GetMapping("/current")
    public HesapResponse current(@RequestHeader("X-USER-ID") Long kullaniciId) {
        return hesapService.getCurrent(kullaniciId);
    }
    @PatchMapping("/{hesapId}/bakiye")
    public HesapResponse updateBakiye(
            @RequestHeader("X-USER-ID") Long kullaniciId,
            @PathVariable Long hesapId,
            @Valid @RequestBody HesapBakiyeUpdateRequest req
    ) {
        return hesapService.updateBakiye(kullaniciId, hesapId, req);
    }

    // ✅ YENİ ENDPOINT (TÜM HESAPLAR)
    @GetMapping
    public List<HesapResponse> getAll(@RequestHeader("X-USER-ID") Long kullaniciId) {
        return hesapService.getAll(kullaniciId);
    }
}

