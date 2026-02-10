package com.example.finansapii.controller;

import com.example.finansapii.dto.TaksitCreateRequest;
import com.example.finansapii.dto.TaksitResponse;
import com.example.finansapii.service.TaksitService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taksitler")
public class TaksitController {

    private final TaksitService taksitService;

    public TaksitController(TaksitService taksitService) {
        this.taksitService = taksitService;
    }

    @PostMapping
    public TaksitResponse create(
            @RequestHeader("X-USER-ID") Long kullaniciId,
            @Valid @RequestBody TaksitCreateRequest req
    ) {
        return taksitService.create(kullaniciId, req);
    }

    @GetMapping("/my")
    public List<TaksitResponse> my(
            @RequestHeader("X-USER-ID") Long kullaniciId
    ) {
        return taksitService.my(kullaniciId);
    }
}
