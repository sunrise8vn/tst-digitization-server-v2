package com.tst.models.entities;

import com.tst.models.enums.EProjectNumberBookStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(
    name = "project_number_books",
    uniqueConstraints = {
        @UniqueConstraint(name = "unique_project_registration_date_id_code", columnNames = {"project_registration_date_id", "code"})
    }
)
public class ProjectNumberBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String code;

    @ManyToOne
    @JoinColumn(name = "project_registration_date_id", nullable = false)
    private ProjectRegistrationDate projectRegistrationDate;

    @OneToOne
    @JoinColumn(name = "project_number_book_cover_id", nullable = false)
    private ProjectNumberBookCover projectNumberBookCover;

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

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private EProjectNumberBookStatus status;

    @Column(columnDefinition = "int default 0", nullable = false)
    private Integer a0;

    @Column(columnDefinition = "int default 0", nullable = false)
    private Integer a1;

    @Column(columnDefinition = "int default 0", nullable = false)
    private Integer a2;

    @Column(columnDefinition = "int default 0", nullable = false)
    private Integer a3;

    @Column(columnDefinition = "int default 0", nullable = false)
    private Integer a4;

    @Column(name = "convert_a4", columnDefinition = "int default 0", nullable = false)
    private Integer convertA4;

}
