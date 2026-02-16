package com.example.finansapii.controller;

import com.example.finansapii.dto.*;
import com.example.finansapii.service.YatirimHesapService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/yatirim")
public class YatirimHesapController {

    private final YatirimHesapService service;

    public YatirimHesapController(YatirimHesapService service) {
        this.service = service;
    }

    // ✅ Yatırım hesabı oluştur
    @PostMapping
    public YatirimHesapResponse create(@RequestBody YatirimHesapCreateRequest req) {
        return service.create(req);
    }

    // ✅ Benim yatırım hesaplarım
    @GetMapping("/mine")
    public List<YatirimHesapResponse> listMy() {
        return service.listMy();
    }

    // ✅ Tek yatırım hesabı
    @GetMapping("/mine/{yatirimId}")
    public YatirimHesapResponse getMy(@PathVariable Long yatirimId) {
        return service.getMyById(yatirimId);
    }

    // ✅ Güncel fiyat güncelle
    @PutMapping("/mine/{yatirimId}/guncel-fiyat")
    public YatirimHesapResponse updateGuncelFiyat(
            @PathVariable Long yatirimId,
            @RequestBody YatirimGuncelFiyatUpdateRequest req
    ) {
        return service.updateGuncelFiyat(yatirimId, req);
    }

    // ✅ Adet artır/azalt (ekleme/çıkarma)
    @PutMapping("/mine/{yatirimId}/adet-degis")
    public YatirimHesapResponse changeAdet(
            @PathVariable Long yatirimId,
            @RequestBody YatirimAdetDegisRequest req
    ) {
        return service.changeAdet(yatirimId, req);
    }

    // ✅ Sil
    @DeleteMapping("/mine/{yatirimId}")
    public void deleteMy(@PathVariable Long yatirimId) {
        service.deleteMy(yatirimId);
    }
}
