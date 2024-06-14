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
    name = "marries",
    uniqueConstraints = {
        @UniqueConstraint(name = "unique_project_number_book_file_id", columnNames = {"project_number_book_file_id"})
    },
    indexes = {
        @Index(name = "index_project_ward_numberBook", columnList = "project_ward_id,number_book")
    }
)
public class Marry {
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

    @Column(name = "marital_relationship_establishment_date ")
    private String maritalRelationshipEstablishmentDate;

    @Column(name = "implementer", length = 50)
    private String implementer;

    @Column(name = "note")
    private String note;

    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(name = "cancel_marry_note_date")
    private String cancelMarryNoteDate;

    @Column(name = "cancel_marry_base")
    private String cancelMarryBase;

    @Column(name = "marriage_recognition_note_date")
    private String marriageRecognitionNoteDate;

    @Column(name = "marriage_recognition_base")
    private String marriageRecognitionBase;

    @Column(name = "husband_full_name", length = 50)
    private String husbandFullName;

    @Column(name = "husband_birthday", length = 10)
    private String husbandBirthday;

    @Column(name = "husband_nation", length = 50)
    private String husbandNation;

    @Column(name = "husband_nationality", length = 100)
    private String husbandNationality;

    @Column(name = "husband_other_nationality", length = 100)
    private String husbandOtherNationality;

    @Column(name = "husband_residence_type")
    private String husbandResidenceType;

    @Column(name = "husband_residence")
    private String husbandResidence;

    @Column(name = "husband_identification_type")
    private String husbandIdentificationType;

    @Column(name = "husband_other_document")
    private String husbandOtherDocument;

    @Column(name = "husband_identification_number", length = 50)
    private String husbandIdentificationNumber;

    @Column(name = "husband_identification_issuance_date", length = 10)
    private String husbandIdentificationIssuanceDate;

    @Column(name = "husband_identification_issuance_place")
    private String husbandIdentificationIssuancePlace;

    @Column(name = "wife_full_name", length = 50)
    private String wifeFullName;

    @Column(name = "wife_birthday", length = 10)
    private String wifeBirthday;

    @Column(name = "wife_nation", length = 50)
    private String wifeNation;

    @Column(name = "wife_nationality", length = 100)
    private String wifeNationality;

    @Column(name = "wife_other_nationality", length = 100)
    private String wifeOtherNationality;

    @Column(name = "wife_residence_type")
    private String wifeResidenceType;

    @Column(name = "wife_residence")
    private String wifeResidence;

    @Column(name = "wife_identification_type")
    private String wifeIdentificationType;

    @Column(name = "wife_other_document")
    private String wifeOtherDocument;

    @Column(name = "wife_identification_number", length = 50)
    private String wifeIdentificationNumber;

    @Column(name = "wife_identification_issuance_date", length = 10)
    private String wifeIdentificationIssuanceDate;

    @Column(name = "wife_identification_issuance_place")
    private String wifeIdentificationIssuancePlace;

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
