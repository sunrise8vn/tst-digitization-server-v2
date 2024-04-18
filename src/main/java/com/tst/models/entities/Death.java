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
@Table(name = "deaths")
public class Death {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number", length = 10)
    private String number;

    @Column(name = "number_book", length = 10)
    private String numberBook;

    @Column(name = "number_page", length = 4)
    private String numberPage;

    @Column(name = "registration_date", length = 10)
    private String registrationDate;

    @Column(name = "registration_type")
    private Integer registrationType;

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

    @Column(name = "dead_man_full_name", length = 50)
    private String deadManFullName;

    @Column(name = "dead_man_gender")
    private Integer deadManGender;

    @Column(name = "dead_man_birthday", length = 10)
    private String deadManBirthday;

    @Column(name = "dead_man_nation", length = 50)
    private String deadManNation;

    @Column(name = "dead_man_nationality", length = 100)
    private String deadManNationality;

    @Column(name = "dead_man_other_nationality", length = 100)
    private String deadManOtherNationality;

    @Column(name = "dead_man_residence_type")
    private Integer deadManResidenceType;

    @Column(name = "dead_man_residence")
    private String deadManResidence;

    @Column(name = "dead_man_identification_type")
    private Integer deadManIdentificationType;

    @Column(name = "dead_man_other_document")
    private String deadManOtherDocument;

    @Column(name = "dead_man_identification_number", length = 50)
    private String deadManIdentificationNumber;

    @Column(name = "dead_man_identification_issuance_date", length = 10)
    private String deadManIdentificationIssuanceDate;

    @Column(name = "dead_man_identification_issuance_place")
    private String deadManIdentificationIssuancePlace;

    @Column(name = "dead_man_dead_date", length = 10)
    private String deadManDeadDate;

    @Column(name = "dead_man_dead_date_word")
    private String deadManDeadDateWord;

    @Column(name = "dead_man_dead_time", length = 7)
    private String deadManDeadTime;

    @Column(name = "dead_man_dead_place")
    private String deadManDeadPlace;

    @Column(name = "dead_man_dead_reason")
    private String deadManDeadReason;

    @Column(name = "dead_man_death_statement_status")
    private String deadManDeathStatementStatus;

    @Column(name = "dead_man_death_statement_note_date", length = 10)
    private String deadManDeathStatementNoteDate;

    @Column(name = "dead_man_death_statement_base")
    private String deadManDeathStatementBase;

    @Column(name = "dead_man_death_cancel_statement_note_date", length = 10)
    private String deadManDeathCancelStatementNoteDate;

    @Column(name = "dead_man_death_cancel_statement_base")
    private String deadManDeathCancelStatementBase;

    @Column(name = "death_notice_type")
    private Integer deathNoticeType;

    @Column(name = "death_notice_number")
    private String deathNoticeNumber;

    @Column(name = "death_notice_date", length = 10)
    private String deathNoticeDate;

    @Column(name = "death_notice_issuance_place")
    private String deathNoticeIssuancePlace;

    @Column(name = "petitioner_full_name", length = 50)
    private String petitionerFullName;

    @Column(name = "petitioner_relationship")
    private String petitionerRelationship;

    @Column(name = "petitioner_identification_type")
    private Integer petitionerIdentificationType;

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
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}
