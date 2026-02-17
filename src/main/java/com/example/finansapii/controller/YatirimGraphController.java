package com.example.finansapii.controller;

import com.example.finansapii.dto.YatirimGraphResponse;
import com.example.finansapii.dto.YatirimGroupBy;
import com.example.finansapii.service.YatirimGraphService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/yatirim")
public class YatirimGraphController {

    private final YatirimGraphService service;

    public YatirimGraphController(YatirimGraphService service) {
        this.service = service;
    }

    // GET /api/yatirim/graph?groupBy=HESAP|VARLIK
    @GetMapping("/graph")
    public YatirimGraphResponse graph(@RequestParam(required = false) YatirimGroupBy groupBy) {
        return service.getGraph(groupBy);
    }
}
