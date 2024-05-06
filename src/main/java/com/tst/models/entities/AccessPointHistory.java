package com.tst.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "access_point_histories")
public class AccessPointHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "access_point", nullable = false)
    private AccessPoint accessPoint;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "count_extract_short", nullable = false)
    private Long countExtractShort;

    @Column(name = "count_extract_full", nullable = false)
    private Long countExtractFull;

    @Column(name = "total_count", nullable = false)
    private Long totalCount;

    @Column(name = "count_success_extract_short", nullable = false)
    private Long countSuccessExtractShort = 0L;

    @Column(name = "count_error_extract_short", nullable = false)
    private Long countErrorExtractShort = 0L;

    @Column(name = "count_success_extract_full", nullable = false)
    private Long countSuccessExtractFull = 0L;

    @Column(name = "count_error_extract_full", nullable = false)
    private Long countErrorExtractFull = 0L;

    @Column(name = "count_revoke_extract_short", nullable = false)
    private Long countRevokeExtractShort = 0L;

    @Column(name = "count_revoke_extract_full", nullable = false)
    private Long countRevokeExtractFull = 0L;

    @Column(name = "total_count_revoke", nullable = false)
    private Long totalCountRevoke = 0L;

    @ManyToOne
    @JoinColumn(name = "assignees", nullable = false)
    private User assignees;

    @PrePersist
    public void prePersist() {
        this.createdBy = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
