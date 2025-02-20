package com.tst.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(
    name = "births",
    uniqueConstraints = {
        @UniqueConstraint(name = "unique_project_number_book_file_id", columnNames = {"project_number_book_file_id"})
    },
    indexes = {
        @Index(name = "index_project_ward_numberBook", columnList = "project_ward_id,number_book")
    }
)
public class Birth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number", length = 20)
    private String number;

    @Column(name = "number_book", length = 10)
    private String numberBook;

    @Column(name = "number_page", length = 10)
    private String numberPage;

    @Column(name = "registration_date", length = 10)
    private String registrationDate;

    @Column(name = "registration_type")
    private String registrationType;

    @Column(name = "registration_place")
    private String registrationPlace;

    @Column(name = "signer", length = 50)
    private String signer;

    @Column(name = "signer_position", length = 50)
    private String signerPosition;

    @Column(name = "implementer", length = 50)
    private String implementer;

    @Column(name = "note")
    private String note;

    @Column(name = "birther_full_name", length = 50)
    private String birtherFullName;

    @Column(name = "birther_gender")
    private String birtherGender;

    @Column(name = "birther_birthday", length = 10)
    private String birtherBirthday;

    @Column(name = "birther_birthday_word")
    private String birtherBirthdayWord;

    @Column(name = "birther_birth_place")
    private String birtherBirthPlace;

    @Column(name = "birther_birth_place_administrative_unit")
    private String birtherBirthPlaceAdministrativeUnit;

    @Column(name = "birther_home_town")
    private String birtherHomeTown;

    @Column(name = "birther_nation", length = 50)
    private String birtherNation;

    @Column(name = "birther_nationality", length = 100)
    private String birtherNationality;

    @Column(name = "birther_other_nationality", length = 100)
    private String birtherOtherNationality;

    @Column(name = "birther_birth_certificate_type", length = 100)
    private String birtherBirthCertificateType;

    @Column(name = "birther_missing")
    private String birtherMissing;

    @Column(name = "birther_missing_statement_note_date", length = 10)
    private String birtherMissingStatementNoteDate;

    @Column(name = "birther_missing_statement_base")
    private String birtherMissingStatementBase;

    @Column(name = "birther_missing_cancel_statement_note_date", length = 10)
    private String birtherMissingCancelStatementNoteDate;

    @Column(name = "birther_missing_cancel_statement_base", length = 50)
    private String birtherMissingCancelStatementBase;

    @Column(name = "birther_limited_behavioral_capacity")
    private String birtherLimitedBehavioralCapacity;

    @Column(name = "birther_limited_behavioral_capacity_statement_note_date", length = 10)
    private String birtherLimitedBehavioralCapacityStatementNoteDate;

    @Column(name = "birther_limited_behavioral_capacity_statement_base")
    private String birtherLimitedBehavioralCapacityStatementBase;

    @Column(name = "birther_limited_behavioral_capacity_cancel_statement_note_date", length = 10)
    private String birtherLimitedBehavioralCapacityCancelStatementNoteDate;

    @Column(name = "birther_limited_behavioral_capacity_cancel_statement_base_date", length = 10)
    private String birtherLimitedBehavioralCapacityCancelStatementBaseDate;

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

    @Column(name = "mom_residence")
    private String momResidence;

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

    @Column(name = "dad_residence")
    private String dadResidence;

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

    @Column(name = "petitioner_other_document")
    private String petitionerOtherDocument;

    @Column(name = "petitioner_identification_number", length = 50)
    private String petitionerIdentificationNumber;

    @Column(name = "petitioner_identification_issuance_date", length = 10)
    private String petitionerIdentificationIssuanceDate;

    @Column(name = "petitioner_identification_issuance_place")
    private String petitionerIdentificationIssuancePlace;

    @Column(name = "foreign_registration_number", length = 50)
    private String foreignRegistrationNumber;

    @Column(name = "foreign_registration_date", length = 10)
    private String foreignRegistrationDate;

    @Column(name = "registered_foreign_organization", length = 100)
    private String registeredForeignOrganization;

    @Column(name = "registered_foreign_country", length = 100)
    private String registeredForeignCountry;

    @ManyToOne
    @JoinColumn(name = "project_number_book_file_id", nullable = false)
    private ProjectNumberBookFile projectNumberBookFile;

    @ManyToOne
    @JoinColumn(name = "project_ward_id", nullable = false)
    private ProjectWard projectWard;

    @ManyToOne
    @JoinColumn(name = "project_district_id", nullable = false)
    private ProjectDistrict projectDistrict;

    @ManyToOne
    @JoinColumn(name = "project_province_id", nullable = false)
    private ProjectProvince projectProvince;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

}
