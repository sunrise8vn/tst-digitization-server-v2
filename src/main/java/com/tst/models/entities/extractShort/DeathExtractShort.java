package com.tst.models.entities.extractShort;

import com.tst.models.entities.*;
import com.tst.models.enums.EInputStatus;
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
    name = "death_extract_short",
    indexes = {
        @Index(name = "index_status", columnList = "status")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "unique_project_number_book_file_id", columnNames = {"project_number_book_file_id"})
    }
)
public class DeathExtractShort {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number", length = 20)
    private String number;

    @Column(name = "number_book", length = 15)
    private String numberBook;

    @Column(name = "number_page", length = 10)
    private String numberPage;

    @Column(name = "registration_date", length = 10)
    private String registrationDate;

    @Column(name = "registration_type")
    private String registrationType;

    @Column(name = "signer", length = 50)
    private String signer;

    @Column(name = "signer_position", length = 50)
    private String signerPosition;

    @Column(name = "implementer", length = 50)
    private String implementer;

    @Column(name = "dead_man_full_name", length = 50)
    private String deadManFullName;

    @Column(name = "dead_man_gender")
    private String deadManGender;

    @Column(name = "dead_man_birthday", length = 10)
    private String deadManBirthday;

    @Column(name = "dead_man_nation", length = 50)
    private String deadManNation;

    @Column(name = "dead_man_nationality", length = 100)
    private String deadManNationality;

    @Column(name = "dead_man_residence_type")
    private String deadManResidenceType;

    @Column(name = "dead_man_identification_type")
    private String deadManIdentificationType;

    @Column(name = "dead_man_identification_number", length = 50)
    private String deadManIdentificationNumber;

    @Column(name = "dead_man_identification_issuance_date", length = 10)
    private String deadManIdentificationIssuanceDate;

    @Column(name = "dead_man_dead_date", length = 10)
    private String deadManDeadDate;

    @Column(name = "dead_man_dead_time", length = 7)
    private String deadManDeadTime;

    @Column(name = "death_notice_type")
    private String deathNoticeType;

    @Column(name = "death_notice_number")
    private String deathNoticeNumber;

    @Column(name = "death_notice_date", length = 10)
    private String deathNoticeDate;

    @Column(name = "petitioner_full_name", length = 50)
    private String petitionerFullName;

    @Column(name = "petitioner_relationship")
    private String petitionerRelationship;

    @Column(name = "petitioner_identification_type")
    private String petitionerIdentificationType;

    @Column(name = "petitioner_identification_number", length = 50)
    private String petitionerIdentificationNumber;

    @Column(name = "petitioner_identification_issuance_date", length = 10)
    private String petitionerIdentificationIssuanceDate;

    @ManyToOne
    @JoinColumn(name = "access_point_id")
    private AccessPoint accessPoint;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EInputStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "imported_at")
    private LocalDateTime importedAt;

    @ManyToOne
    @JoinColumn(name = "importer")
    private User importer;

    @Column(name = "compare_checked_at")
    private LocalDateTime compareCheckedAt;

    @ManyToOne
    @JoinColumn(name = "compare_checker")
    private User compareChecker;

    @Column(name = "final_checked_at")
    private LocalDateTime finalCheckedAt;

    @ManyToOne
    @JoinColumn(name = "final_checker")
    private User finalChecker;

    @Column(name = "released_at")
    private LocalDateTime releasedAt;

    @ManyToOne
    @JoinColumn(name = "releaser")
    private User releaser;

    @ManyToOne
    @JoinColumn(name = "project_number_book_file_id", nullable = false)
    private ProjectNumberBookFile projectNumberBookFile;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(columnDefinition = "boolean default false")
    private Boolean deleted = false;

    @PrePersist
    public void prePersist() {
        this.createdBy = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
