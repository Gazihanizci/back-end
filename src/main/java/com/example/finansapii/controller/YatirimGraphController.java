package com.example.finansapii.controller;

import com.example.finansapii.dto.YatirimGraphResponse;
import com.example.finansapii.service.YatirimGraphService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/yatirim")
public class YatirimGraphController {

    private final YatirimGraphService service;

    public YatirimGraphController(YatirimGraphService service) {
        this.service = service;
    }


    @GetMapping("/graph")
    public YatirimGraphResponse graph(@RequestParam(required = false) String groupBy) {
        return service.getGraph(groupBy);
    }
}
