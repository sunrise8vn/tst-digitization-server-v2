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
    name = "marry_extract_short",
    indexes = {
        @Index(name = "index_status", columnList = "status")
    }
)
public class MarryExtractShort {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number", length = 20)
    private String number;

    @Column(name = "number_page", length = 10)
    private String numberPage;

    @Column(name = "registration_date", length = 10)
    private String registrationDate;

    @Column(name = "registration_type")
    private String registrationType;

    @Column(name = "marital_relationship_establishment_date ")
    private String maritalRelationshipEstablishmentDate;

    @Column(name = "signer", length = 50)
    private String signer;

    @Column(name = "signer_position", length = 50)
    private String signerPosition;

    @Column(name = "implementer", length = 50)
    private String implementer;

    @Column(name = "note")
    private String note;

    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(name = "husband_birthday", length = 10)
    private String husbandBirthday;

    @Column(name = "husband_nation", length = 50)
    private String husbandNation;

    @Column(name = "husband_nationality", length = 100)
    private String husbandNationality;

    @Column(name = "husband_residence_type")
    private String husbandResidenceType;

    @Column(name = "husband_identification_type")
    private String husbandIdentificationType;

    @Column(name = "husband_identification_number", length = 50)
    private String husbandIdentificationNumber;

    @Column(name = "husband_identification_issuance_date", length = 10)
    private String husbandIdentificationIssuanceDate;

    @Column(name = "wife_full_name", length = 50)
    private String wifeFullName;

    @Column(name = "wife_birthday", length = 10)
    private String wifeBirthday;

    @Column(name = "wife_nation", length = 50)
    private String wifeNation;

    @Column(name = "wife_nationality", length = 100)
    private String wifeNationality;

    @Column(name = "wife_residence_type")
    private String wifeResidenceType;

    @Column(name = "wife_identification_type")
    private String wifeIdentificationType;

    @Column(name = "wife_identification_number", length = 50)
    private String wifeIdentificationNumber;

    @Column(name = "wife_identification_issuance_date", length = 10)
    private String wifeIdentificationIssuanceDate;

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

    @Column(name = "checked_at")
    private LocalDateTime checkedAt;

    @ManyToOne
    @JoinColumn(name = "checker")
    private User checker;

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
