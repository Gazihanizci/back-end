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
    // ✅ Kullanıcı kendi ailesinden ayrılır
// POST /api/aileler/{aileId}/ayril
    @PostMapping("/{aileId}/ayril")
    public void leaveFamily(@PathVariable Long aileId) {
        Long userId = CurrentUser.id();

        // ✅ kullanıcı bu aileye ait mi?
        familyAccess.assertUserInFamily(userId, aileId);

        familyMemberService.leaveFamily(userId, aileId);
    }
    // ✅ Aile sahipliğini devret
// POST /api/aileler/{aileId}/sahiplik-devret/{newOwnerUserId}
    @PostMapping("/{aileId}/sahiplik-devret/{newOwnerUserId}")
    public void transferOwnership(
            @PathVariable Long aileId,
            @PathVariable Long newOwnerUserId
    ) {
        Long requestUserId = CurrentUser.id();

        // ✅ en azından bu user bu aileye ait mi?
        familyAccess.assertUserInFamily(requestUserId, aileId);

        familyMemberService.transferOwnership(requestUserId, aileId, newOwnerUserId);
    }


}
