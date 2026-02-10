package com.example.finansapii.controller;

import com.example.finansapii.service.FamilyMemberService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/aileler")
public class FamilyMemberController {

    private final FamilyMemberService familyMemberService;

    public FamilyMemberController(FamilyMemberService familyMemberService) {
        this.familyMemberService = familyMemberService;
    }

    // POST /api/aileler/{aileId}/uyeler/{targetUserId}/cikar
    @PostMapping("/{aileId}/uyeler/{targetUserId}/cikar")
    public void removeMember(
            @RequestHeader("X-USER-ID") Long requestUserId,
            @PathVariable Long aileId,
            @PathVariable Long targetUserId
    ) {
        familyMemberService.removeMember(requestUserId, aileId, targetUserId);
    }
}
