package com.example.finansapii.entity;

import com.example.finansapii.permissions.PermissionStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "family_note_permissions",
        indexes = {
                @Index(name = "idx_fnp_family", columnList = "family_id"),
                @Index(name = "idx_fnp_user", columnList = "requester_user_id"),
                @Index(name = "idx_fnp_status", columnList = "status")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_family_user", columnNames = {"family_id", "requester_user_id"})
        }
)
public class FamilyNotePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="family_id", nullable = false)
    private Long familyId;

    @Column(name="requester_user_id", nullable = false)
    private Long requesterUserId;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false, length = 20)
    private PermissionStatus status = PermissionStatus.PENDING;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name="resolved_by_user_id")
    private Long resolvedByUserId;

    @Column(name="resolved_at")
    private LocalDateTime resolvedAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (status == null) status = PermissionStatus.PENDING;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getFamilyId() { return familyId; }
    public void setFamilyId(Long familyId) { this.familyId = familyId; }

    public Long getRequesterUserId() { return requesterUserId; }
    public void setRequesterUserId(Long requesterUserId) { this.requesterUserId = requesterUserId; }

    public PermissionStatus getStatus() { return status; }
    public void setStatus(PermissionStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Long getResolvedByUserId() { return resolvedByUserId; }
    public void setResolvedByUserId(Long resolvedByUserId) { this.resolvedByUserId = resolvedByUserId; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
}