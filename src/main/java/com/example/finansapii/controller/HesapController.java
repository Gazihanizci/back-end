package com.example.finansapii.controller;

import com.example.finansapii.dto.HesapBakiyeUpdateRequest;
import com.example.finansapii.dto.HesapCreateRequest;
import com.example.finansapii.dto.HesapResponse;
import com.example.finansapii.security.CurrentUser;
import com.example.finansapii.service.HesapService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hesaplar")
public class HesapController {

    private final HesapService hesapService;

    public HesapController(HesapService hesapService) {
        this.hesapService = hesapService;
    }

    @PostMapping
    public HesapResponse create(@Valid @RequestBody HesapCreateRequest req) {
        Long kullaniciId = CurrentUser.id();
        return hesapService.create(kullaniciId, req);
    }

    @GetMapping("/current")
    public HesapResponse current() {
        Long kullaniciId = CurrentUser.id();
        return hesapService.getCurrent(kullaniciId);
    }

    @PatchMapping("/{hesapId}/bakiye")
    public HesapResponse updateBakiye(
            @PathVariable Long hesapId,
            @Valid @RequestBody HesapBakiyeUpdateRequest req
    ) {
        Long kullaniciId = CurrentUser.id();
        return hesapService.updateBakiye(kullaniciId, hesapId, req);
    }

    @GetMapping
    public List<HesapResponse> getAll() {
        Long kullaniciId = CurrentUser.id();
        return hesapService.getAll(kullaniciId);
    }
}
