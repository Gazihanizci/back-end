package com.example.finansapii.controller;

import com.example.finansapii.dto.IslemCreateRequest;
import com.example.finansapii.dto.IslemResponse;
import com.example.finansapii.security.CurrentUser;
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
    public IslemResponse create(@Valid @RequestBody IslemCreateRequest req) {
        Long kullaniciId = CurrentUser.id();
        return islemService.create(kullaniciId, req);
    }

    // GET /api/islemler/my?page=0&size=20&sort=islemTarihi,desc
    @GetMapping("/my")
    public Page<IslemResponse> myIslemler(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "islemTarihi,desc") String sort
    ) {
        Long kullaniciId = CurrentUser.id();

        String[] s = sort.split(",");
        String field = s[0];
        Sort.Direction dir = (s.length > 1 && "asc".equalsIgnoreCase(s[1]))
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, field));
        return islemService.listByUser(kullaniciId, pageable);
    }
}
