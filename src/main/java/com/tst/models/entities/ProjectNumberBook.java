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
