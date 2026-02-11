package com.example.finansapii.controller;

import com.example.finansapii.dto.FamilyInfoResponse;
import com.example.finansapii.security.CurrentUser;
import com.example.finansapii.service.FamilyInfoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class FamilyInfoController {

    private final FamilyInfoService familyInfoService;

    public FamilyInfoController(FamilyInfoService familyInfoService) {
        this.familyInfoService = familyInfoService;
    }

    // GET /api/familyinfo
    @GetMapping("/familyinfo")
    public FamilyInfoResponse getFamilyInfo() {
        Long requestUserId = CurrentUser.id();
        return familyInfoService.getFamilyInfo(requestUserId);
    }
}
