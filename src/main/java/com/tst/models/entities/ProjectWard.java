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
@Table(
    name = "project_wards",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "unique_code_district_id_project_id",
            columnNames = {"code", "project_district_id", "project_id"}
        )
    }
)
public class ProjectWard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "project_district_id", nullable = false)
    private ProjectDistrict projectDistrict;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private Long a0 = 0L;

    @Column(nullable = false)
    private Long a1 = 0L;

    @Column(nullable = false)
    private Long a2 = 0L;

    @Column(nullable = false)
    private Long a3 = 0L;

    @Column(nullable = false)
    private Long a4 = 0L;

    @Column(name = "convert_a4", nullable = false)
    private Long convertA4 = 0L;

    @Column(name = "total_size", nullable = false)
    private Long totalSize = 0L;

    @Column(name = "size_type", nullable = false, length = 2)
    private String sizeType = "KB";

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "boolean default false")
    private Boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id", nullable = false)
    private User createdBy;

    @PrePersist
    public void prePersist() {
        this.createdBy = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
