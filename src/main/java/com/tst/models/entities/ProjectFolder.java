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
@Table(name = "project_folders")
public class ProjectFolder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "root_folder", nullable = false)
    private String rootFolder;

    @Column(name = "province_code", nullable = false)
    private String provinceCode;

    @Column(name = "district_code", nullable = false)
    private String districtCode;

    @Column(name = "ward_code", nullable = false)
    private String wardCode;

    @Column(name = "registration_type", nullable = false, length = 10)
    private String registrationType;

    @Column(name = "pager_size", nullable = false, length = 2)
    private String pagerSize;

    @Column(name = "registration_year", nullable = false, length = 10)
    private String registrationYear;

    @Column(name = "number_book", nullable = false, length = 10)
    private String numberBook;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

}
