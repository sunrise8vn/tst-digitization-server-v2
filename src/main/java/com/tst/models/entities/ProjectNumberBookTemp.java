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
@Table(name = "project_number_book_temp")
public class ProjectNumberBookTemp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "folder_name", nullable = false)
    private String folderName;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "size_type", nullable = false, length = 2)
    private String sizeType;

    @ManyToOne
    @JoinColumn(name = "project_number_book_id", nullable = false)
    private ProjectNumberBook projectNumberBook;
}
