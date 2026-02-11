package com.example.finansapii.controller;

import com.example.finansapii.security.CurrentUser;
import com.example.finansapii.security.FamilyAccess;
import com.example.finansapii.service.FamilyMemberService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/aileler")
public class FamilyMemberController {

    private final FamilyMemberService familyMemberService;
    private final FamilyAccess familyAccess;

    public FamilyMemberController(FamilyMemberService familyMemberService, FamilyAccess familyAccess) {
        this.familyMemberService = familyMemberService;
        this.familyAccess = familyAccess;
    }

    // POST /api/aileler/{aileId}/uyeler/{targetUserId}/cikar
    @PostMapping("/{aileId}/uyeler/{targetUserId}/cikar")
    public void removeMember(
            @PathVariable Long aileId,
            @PathVariable Long targetUserId
    ) {
        Long requestUserId = CurrentUser.id();

        // ✅ en azından bu user bu aileye ait mi? (yoksa başkasının ailesine müdahale eder)
        familyAccess.assertUserInFamily(requestUserId, aileId);

        familyMemberService.removeMember(requestUserId, aileId, targetUserId);
    }
}
