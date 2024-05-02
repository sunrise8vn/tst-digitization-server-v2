package com.tst.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;


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

    @Column(name = "timestamp")
    private Long timestamp;

    @Column(name = "count_extract_short", nullable = false)
    private Long countExtractShort;

    @Column(name = "count_extract_full", nullable = false)
    private Long countExtractFull;

    @Column(name = "total_count", nullable = false)
    private Long totalCount;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        ZonedDateTime zdt = this.createdAt.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        this.timestamp = zdt.toInstant().toEpochMilli();
        this.createdBy = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedBy = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
