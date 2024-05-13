package com.tst.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String code;

    private String description;

    @Column(nullable = false)
    private String folder;

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
