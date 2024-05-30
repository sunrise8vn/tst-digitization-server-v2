package com.tst.models.entities;

import com.tst.models.enums.EAccessPointStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "access_points")
public class AccessPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EAccessPointStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @Column(name = "count_extract_short", nullable = false)
    private Long countExtractShort;

    @Column(name = "count_extract_full", nullable = false)
    private Long countExtractFull;

    @Column(name = "total_count", nullable = false)
    private Long totalCount;

    @Column(name = "total_success_extract_short", nullable = false)
    private Long totalSuccessExtractShort = 0L;

    @Column(name = "total_success_extract_full", nullable = false)
    private Long totalSuccessExtractFull = 0L;

    @Column(name = "total_error_extract_short", nullable = false)
    private Long totalErrorExtractShort = 0L;

    @Column(name = "total_error_extract_full", nullable = false)
    private Long totalErrorExtractFull = 0L;

    @Column(name = "total_count_revoke", nullable = false)
    private Long totalCountRevoke = 0L;

    @PrePersist
    public void prePersist() {
        this.createdBy = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedBy = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
