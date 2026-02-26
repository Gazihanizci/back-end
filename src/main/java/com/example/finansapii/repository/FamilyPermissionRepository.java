package com.example.finansapii.repository;

import com.example.finansapii.dto.PermissionInboxItem;
import com.example.finansapii.entity.FamilyPermission;
import com.example.finansapii.permissions.PermissionStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface    FamilyPermissionRepository extends JpaRepository<FamilyPermission, Long> {

    Optional<FamilyPermission> findTopByFamilyIdAndRequesterUserIdOrderByCreatedAtDesc(
            Long familyId, Long requesterUserId);

    boolean existsByFamilyIdAndRequesterUserIdAndStatus(
            Long familyId, Long requesterUserId, PermissionStatus status);

    @Query("""
      select new com.example.finansapii.dto.PermissionInboxItem(
        fp.id,
        fp.requesterUserId,
        concat(u.ad,' ',u.soyad),
        fp.createdAt
      )
      from FamilyPermission fp
      join User u on u.id = fp.requesterUserId
      where fp.familyId = :familyId and fp.status = :status
      order by fp.createdAt desc
    """)
    List<PermissionInboxItem> inbox(@Param("familyId") Long familyId,
                                    @Param("status") PermissionStatus status);
}
