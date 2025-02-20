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
    name = "birth_extract_short",
    indexes = {
        @Index(name = "index_status", columnList = "status")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "unique_project_number_book_file_id", columnNames = {"project_number_book_file_id"})
    }
)
public class BirthExtractShort {
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

    @Column(name = "birther_full_name", length = 50)
    private String birtherFullName;

    @Column(name = "birther_gender")
    private String birtherGender;

    @Column(name = "birther_birthday", length = 10)
    private String birtherBirthday;

    @Column(name = "birther_nation", length = 50)
    private String birtherNation;

    @Column(name = "birther_nationality", length = 100)
    private String birtherNationality;

    @Column(name = "birth_certificate_type")
    private String birthCertificateType;

    @Column(name = "mom_full_name", length = 50)
    private String momFullName;

    @Column(name = "mom_birthday", length = 10)
    private String momBirthday;

    @Column(name = "mom_nation", length = 50)
    private String momNation;

    @Column(name = "mom_nationality", length = 100)
    private String momNationality;

    @Column(name = "mom_other_nationality", length = 100)
    private String momOtherNationality;

    @Column(name = "mom_residence_type")
    private String momResidenceType;

    @Column(name = "mom_identification_type")
    private String momIdentificationType;

    @Column(name = "mom_identification_number", length = 50)
    private String momIdentificationNumber;

    @Column(name = "dad_full_name", length = 50)
    private String dadFullName;

    @Column(name = "dad_birthday", length = 10)
    private String dadBirthday;

    @Column(name = "dad_nation", length = 50)
    private String dadNation;

    @Column(name = "dad_nationality", length = 100)
    private String dadNationality;

    @Column(name = "dad_other_nationality", length = 100)
    private String dadOtherNationality;

    @Column(name = "dad_residence_type")
    private String dadResidenceType;

    @Column(name = "dad_identification_type")
    private String dadIdentificationType;

    @Column(name = "dad_identification_number", length = 50)
    private String dadIdentificationNumber;

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
