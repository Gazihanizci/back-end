package com.example.finansapii.controller;

import com.example.finansapii.dto.IslemCreateRequest;
import com.example.finansapii.dto.IslemResponse;
import com.example.finansapii.service.IslemService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/islemler")
public class IslemController {

    private final IslemService islemService;

    public IslemController(IslemService islemService) {
        this.islemService = islemService;
    }

    @PostMapping
    public IslemResponse create(
            @RequestHeader("X-USER-ID") Long kullaniciId,
            @Valid @RequestBody IslemCreateRequest req
    ) {
        return islemService.create(kullaniciId, req);
    }

    // ✅ EK: Kullanıcıya göre tüm işlemleri çek (sayfalı + sıralı)
    // Örnek:
    // GET /api/islemler/my?page=0&size=20&sort=islemTarihi,desc
    // GET /api/islemler/my?page=0&size=20&sort=tutar,desc
    @GetMapping("/my")
    public Page<IslemResponse> myIslemler(
            @RequestHeader("X-USER-ID") Long kullaniciId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "islemTarihi,desc") String sort
    ) {
        String[] s = sort.split(",");
        String field = s[0];
        Sort.Direction dir = (s.length > 1 && "asc".equalsIgnoreCase(s[1]))
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, field));
        return islemService.listByUser(kullaniciId, pageable);
    }
}
