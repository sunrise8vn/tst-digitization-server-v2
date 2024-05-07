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
@Table(name = "parents_children")
public class ParentsChildren {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number", length = 20)
    private String number;

    @Column(name = "number_book", length = 10)
    private String numberBook;

    @Column(name = "number_page", length = 10)
    private String numberPage;

    @Column(name = "decision_no", length = 5)
    private String decisionNo;

    @Column(name = "registration_date", length = 10)
    private String registrationDate;

    @Column(name = "registration_type")
    private String registrationType;

    @Column(name = "confirmation_type ")
    private String confirmationType;

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

    @Column(name = "parent_full_name", length = 50)
    private String parentFullName;

    @Column(name = "parent_birthday", length = 10)
    private String parentBirthday;

    @Column(name = "parent_nation", length = 50)
    private String parentNation;

    @Column(name = "parent_nationality", length = 100)
    private String parentNationality;

    @Column(name = "parent_other_nationality", length = 100)
    private String parentOtherNationality;

    @Column(name = "parent_home_town")
    private String parentHomeTown;

    @Column(name = "parent_residence_type")
    private String parentResidenceType;

    @Column(name = "parent_residence")
    private String parentResidence;

    @Column(name = "parent_identification_type")
    private String parentIdentificationType;

    @Column(name = "parent_other_document")
    private String parentOtherDocument;

    @Column(name = "parent_identification_number", length = 50)
    private String parentIdentificationNumber;

    @Column(name = "parent_identification_issuance_date", length = 10)
    private String parentIdentificationIssuanceDate;

    @Column(name = "parent_identification_issuance_place")
    private String parentIdentificationIssuancePlace;

    @Column(name = "child_full_name", length = 50)
    private String childFullName;

    @Column(name = "child_birthday", length = 10)
    private String childBirthday;

    @Column(name = "child_nation", length = 50)
    private String childNation;

    @Column(name = "child_nationality", length = 100)
    private String childNationality;

    @Column(name = "child_other_nationality", length = 100)
    private String childOtherNationality;

    @Column(name = "child_home_town")
    private String childHomeTown;

    @Column(name = "child_residence_type")
    private String childResidenceType;

    @Column(name = "child_residence")
    private String childResidence;

    @Column(name = "child_identification_type")
    private String childIdentificationType;

    @Column(name = "child_other_document")
    private String childOtherDocument;

    @Column(name = "child_identification_number", length = 50)
    private String childIdentificationNumber;

    @Column(name = "child_identification_issuance_date", length = 10)
    private String childIdentificationIssuanceDate;

    @Column(name = "child_identification_issuance_place")
    private String childIdentificationIssuancePlace;

    @Column(name = "petitioner_full_name", length = 50)
    private String petitionerFullName;

    @Column(name = "petitioner_recipient_relationship")
    private String petitionerRecipientRelationship;

    @Column(name = "petitioner_receiver_relationship")
    private String petitionerReceiverRelationship;

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
