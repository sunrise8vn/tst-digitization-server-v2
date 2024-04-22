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
@Table(name = "project_registration_date")
public class ProjectRegistrationDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String code;

    @ManyToOne
    @JoinColumn(name = "project_paper_size_id", nullable = false)
    private ProjectPaperSize projectPaperSize;

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
