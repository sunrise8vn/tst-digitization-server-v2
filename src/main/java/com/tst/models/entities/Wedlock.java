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
@Table(name = "wedlocks")
public class Wedlock {
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

    @Column(name = "issuance_place")
    private String issuancePlace;

    @Column(name = "signer", length = 50)
    private String signer;

    @Column(name = "signer_position", length = 50)
    private String signerPosition;

    @Column(name = "implementer", length = 50)
    private String implementer;

    @Column(name = "note")
    private String note;

    @Column(name = "confirmer_full_name", length = 50)
    private String confirmerFullName;

    @Column(name = "confirmer_gender")
    private String confirmerGender;

    @Column(name = "confirmer_birthday", length = 10)
    private String confirmerBirthday;

    @Column(name = "confirmer_nation", length = 50)
    private String confirmerNation;

    @Column(name = "confirmer_nationality", length = 100)
    private String confirmerNationality;

    @Column(name = "confirmer_other_nationality", length = 100)
    private String confirmerOtherNationality;

    @Column(name = "confirmer_residence_type")
    private String confirmerResidenceType;

    @Column(name = "confirmer_residence")
    private String confirmerResidence;

    @Column(name = "confirmer_identification_type")
    private String confirmerIdentificationType;

    @Column(name = "confirmer_other_document")
    private String confirmerOtherDocument;

    @Column(name = "confirmer_identification_number", length = 50)
    private String confirmerIdentificationNumber;

    @Column(name = "confirmer_identification_issuance_date", length = 10)
    private String confirmerIdentificationIssuanceDate;

    @Column(name = "confirmer_identification_issuance_place")
    private String confirmerIdentificationIssuancePlace;

    @Column(name = "confirmer_period_residence_at")
    private String confirmerPeriodResidenceAt;

    @Column(name = "confirmer_period_residence_from", length = 10)
    private String confirmerPeriodResidenceFrom;

    @Column(name = "confirmer_period_residence_to", length = 10)
    private String confirmerPeriodResidenceTo;

    @Column(name = "confirmer_marital_status")
    private String confirmerMaritalStatus;

    @Column(name = "confirmer_intended_use_type")
    private String confirmerIntendedUseType;

    @Column(name = "confirmer_intended_use")
    private String confirmerIntendedUse;

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

    @ManyToOne
    @JoinColumn(name = "project_number_book_file_id", nullable = false)
    private ProjectNumberBookFile projectNumberBookFile;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

}
