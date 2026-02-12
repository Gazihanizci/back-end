package com.example.finansapii.controller;

import com.example.finansapii.dto.OzelKategoriCreateRequest;
import com.example.finansapii.dto.OzelKategoriResponse;
import com.example.finansapii.security.CurrentUser;
import com.example.finansapii.service.OzelKategoriService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ozel-kategori")
public class OzelKategoriController {

    private final OzelKategoriService service;

    public OzelKategoriController(OzelKategoriService service) {
        this.service = service;
    }

    // POST /api/ozel-kategori
    @PostMapping
    public OzelKategoriResponse create(@Valid @RequestBody OzelKategoriCreateRequest req) {
        Long userId = CurrentUser.id();
        return service.create(userId, req);
    }

    // GET /api/ozel-kategori
    @GetMapping
    public List<OzelKategoriResponse> mine() {
        Long userId = CurrentUser.id();
        return service.listMine(userId);
    }

    // DELETE /api/ozel-kategori/{id}
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Long userId = CurrentUser.id();
        service.deleteMine(userId, id);
    }
}
