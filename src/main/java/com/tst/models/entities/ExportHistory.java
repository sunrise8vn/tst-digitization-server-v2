package com.tst.models.entities;

import com.tst.models.enums.EExportStatus;
import com.tst.models.enums.EExportType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(
    name = "export_histories",
    indexes = {
        @Index(name = "index_project_id", columnList = "project_id"),
        @Index(name = "index_province_id", columnList = "province_id"),
        @Index(name = "index_district_id", columnList = "district_id"),
        @Index(name = "index_ward_id", columnList = "ward_id"),
        @Index(name = "index_excel_name", columnList = "excel_name"),
        @Index(name = "index_zip_name", columnList = "zip_name")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "unique_ward_id_excel_name", columnNames = {"ward_id", "excel_name"}),
        @UniqueConstraint(name = "unique_ward_id_zip_name", columnNames = {"ward_id", "zip_name"})
    }
)
public class ExportHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "province_id", nullable = false)
    private Long provinceId;

    @Column(name = "province_name")
    private String provinceName;

    @Column(name = "district_id", nullable = false)
    private Long districtId;

    @Column(name = "district_name")
    private String districtName;

    @Column(name = "ward_id", nullable = false)
    private Long wardId;

    @Column(name = "ward_name")
    private String wardName;

    @Column(name = "registration_type", nullable = false)
    private String registrationType;

    @Column(name = "paper_size", nullable = false)
    private String paperSize;

    @Column(name = "number_book_year", nullable = false)
    private String numberBookYear;

    @Column(name = "number_book", nullable = false)
    private String numberBook;

    @Column(name = "total_count", nullable = false)
    private Integer totalCount = 0;

    @Column(name = "excel_folder_name")
    private String excelFolderName;

    @Column(name = "excel_name")
    private String excelName;

    @Column(name = "excel_size")
    private Long excelSize = 0L;

    @Column(name = "excel_size_type")
    private String excelSizeType = "KB";

    @Column(name = "zip_folder_name")
    private String zipFolderName;

    @Column(name = "zip_name")
    private String zipName;

    @Column(name = "zip_size")
    private Long zipSize = 0L;

    @Column(name = "zip_size_type")
    private String zipSizeType = "MB";

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private EExportStatus status = EExportStatus.PROCESSING;

    @Enumerated(EnumType.STRING)
    @Column(name = "export_type", length = 10, nullable = false)
    private EExportType exportType;

    @Column(columnDefinition = "BIGINT(20) DEFAULT 0", nullable = false, unique = true)
    private Long ts = new Date().getTime();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @PrePersist
    public void prePersist() {
        this.createdBy = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
