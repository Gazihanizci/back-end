package com.example.finansapii.controller;

import com.example.finansapii.dto.AylikAnalizResponse;
import com.example.finansapii.security.CurrentUser;
import com.example.finansapii.service.AylikAnalizService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aylik-analiz")
public class AylikAnalizController {

    private final AylikAnalizService service;

    public AylikAnalizController(AylikAnalizService service) {
        this.service = service;
    }

    @GetMapping
    public List<AylikAnalizResponse> getMyAnaliz() {
        Long kullaniciId = CurrentUser.id();
        return service.getForUser(kullaniciId);
    }
}
