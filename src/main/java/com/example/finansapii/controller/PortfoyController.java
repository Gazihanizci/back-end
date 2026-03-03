package com.example.finansapii.controller;

import com.example.finansapii.entity.YatirimPortfoy;
import com.example.finansapii.repository.YatirimPortfoyRepository;
import com.example.finansapii.security.CurrentUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfoy")
public class PortfoyController {

    private final YatirimPortfoyRepository portfoyRepository;

    public PortfoyController(YatirimPortfoyRepository portfoyRepository) {
        this.portfoyRepository = portfoyRepository;
    }

    @GetMapping("/mine")
    public List<YatirimPortfoy> getMyPortfolio() {
        Long userId = CurrentUser.id();
        return portfoyRepository.findAll()
                .stream()
                .filter(p -> p.getKullaniciId().equals(userId))
                .toList();
    }
}