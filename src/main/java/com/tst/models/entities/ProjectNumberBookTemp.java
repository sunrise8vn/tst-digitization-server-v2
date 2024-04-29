package com.tst.models.entities;

import com.tst.models.enums.EPaperSize;
import com.tst.models.enums.EProjectNumberBookTempStatus;
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
@Table(name = "project_number_book_temp")
public class ProjectNumberBookTemp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "folder_path", nullable = false)
    private String folderPath;

    @Enumerated(EnumType.STRING)
    @Column(name = "paper_size", nullable = false, length = 2)
    private EPaperSize paperSize;

    @Column(name = "file_size", nullable = false)
    private Long fileSize = 0L;

    @Column(name = "size_type", nullable = false, length = 2)
    private String sizeType = "KB";

    @ManyToOne
    @JoinColumn(name = "project_number_book_id", nullable = false)
    private ProjectNumberBook projectNumberBook;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private EProjectNumberBookTempStatus status;

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
