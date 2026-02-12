package com.example.finansapii.controller;

import com.example.finansapii.dto.OzelIslemCreateRequest;
import com.example.finansapii.service.OzelIslemService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
    @RequestMapping("/api/ozelislemler")
public class OzelIslemController {

    private final OzelIslemService service;

    public OzelIslemController(OzelIslemService service) {
        this.service = service;
    }

    // POST /api/ozelislemler
    @PostMapping
    public Long create(@Valid @RequestBody OzelIslemCreateRequest req) {
        return service.create(req);
    }
}
