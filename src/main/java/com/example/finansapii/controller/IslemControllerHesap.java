package com.example.finansapii.controller;

import com.example.finansapii.dto.IslemRequest;
import com.example.finansapii.entity.YatirimPortfoy;
import com.example.finansapii.service.YatirimMotorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/islem")
public class IslemControllerHesap {

    private final YatirimMotorService motorService;

    public IslemControllerHesap(YatirimMotorService motorService) {
        this.motorService = motorService;
    }

    @PostMapping
    public YatirimPortfoy islemYap(@Valid @RequestBody IslemRequest request) {

        return motorService.islemYap(
                request.getVarlikTuru(),
                request.getTip(),
                request.getAdet(),
                request.getFiyat()
        );
    }
}