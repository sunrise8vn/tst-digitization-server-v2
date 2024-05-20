package com.tst.models.entities;

import com.tst.models.enums.EPaperSize;
import com.tst.models.enums.EProjectNumberBookFileStatus;
import com.tst.models.enums.ERegistrationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project_number_book_files")
public class ProjectNumberBookFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "folder_path", nullable = false)
    private String folderPath;

    @Enumerated(EnumType.STRING)
    @Column(name = "paper_size", nullable = false)
    private EPaperSize paperSize;

    @Column(name = "file_size", nullable = false)
    private Long fileSize = 0L;

    @Column(name = "size_type", nullable = false, length = 2)
    private String sizeType = "KB";

    @Column(name = "number", length = 20)
    private String number;

    @Column(name = "registration_date", length = 10)
    private String registrationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "registration_type", nullable = false)
    private ERegistrationType registrationType;

    @ManyToOne
    @JoinColumn(name = "project_number_book_id", nullable = false)
    private ProjectNumberBook projectNumberBook;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EProjectNumberBookFileStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id", nullable = false)
    private User createdBy;

    @UpdateTimestamp
    @Column(name = "organized_at")
    private LocalDateTime organizedAt;

    @ManyToOne
    @JoinColumn(name = "organized_by")
    private User organizedBy;

    @UpdateTimestamp
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @PrePersist
    public void prePersist() {
        this.createdBy = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
