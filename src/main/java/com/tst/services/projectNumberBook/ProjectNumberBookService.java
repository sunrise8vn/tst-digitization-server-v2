package com.tst.services.projectNumberBook;

import com.tst.exceptions.DataInputException;
import com.tst.exceptions.DataNotFoundException;
import com.tst.models.entities.*;
import com.tst.models.enums.EProjectNumberBookStatus;
import com.tst.repositories.ProjectNumberBookRepository;
import com.tst.services.project.IProjectService;
import com.tst.services.projectDistrict.IProjectDistrictService;
import com.tst.services.projectNumberBookCover.IProjectNumberBookCoverService;
import com.tst.services.projectPaperSize.IProjectPaperSizeService;
import com.tst.services.projectProvince.IProjectProvinceService;
import com.tst.services.projectRegistrationType.IProjectRegistrationTypeService;
import com.tst.services.projectWard.IProjectWardService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProjectNumberBookService implements IProjectNumberBookService {

    private final ProjectNumberBookRepository projectNumberBookRepository;
    private final IProjectNumberBookCoverService projectNumberBookCoverService;
    private final IProjectPaperSizeService projectPaperSizeService;
    private final IProjectRegistrationTypeService projectRegistrationTypeService;
    private final IProjectWardService projectWardService;
    private final IProjectDistrictService projectDistrictService;
    private final IProjectProvinceService projectProvinceService;
    private final IProjectService projectService;


    @Override
    public Optional<ProjectNumberBook> findById(Long id) {
        return projectNumberBookRepository.findById(id);
    }

    @Override
    public Optional<ProjectNumberBook> findByIdAndStatus(Long id, EProjectNumberBookStatus status) {
        return projectNumberBookRepository.findByIdAndStatus(id, status);
    }

    @Override
    @Transactional
    public ProjectNumberBook create(ProjectNumberBook projectNumberBook, MultipartFile coverFile) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (coverFile == null) {
            throw new DataInputException("Tập tin pdf không được bỏ trống");
        }

        String fileType = Objects.requireNonNull(coverFile.getContentType()).split("/")[1].toLowerCase();

        if (!fileType.equals("pdf")) {
            throw new DataNotFoundException("Chỉ chấp nhận tập tin pdf");
        } else {
            ProjectRegistrationDate projectRegistrationDate = projectNumberBook.getProjectRegistrationDate();

            Boolean existProjectNumberBookNew = projectNumberBookRepository.existsByProjectRegistrationDateAndCodeAndStatus(
                    projectRegistrationDate,
                    projectNumberBook.getCode(),
                    EProjectNumberBookStatus.NEW
            );

            if (existProjectNumberBookNew) {
                throw new DataInputException("Quyển sổ " + projectNumberBook.getCode() + " đăng ký năm " + projectRegistrationDate.getCode() + " đang chờ xét duyệt");
            }

            Boolean existProjectNumberBookAccept = projectNumberBookRepository.existsByProjectRegistrationDateAndCodeAndStatus(
                    projectRegistrationDate,
                    projectNumberBook.getCode(),
                    EProjectNumberBookStatus.ACCEPT
            );

            if (existProjectNumberBookAccept) {
                throw new DataInputException("Quyển sổ " + projectNumberBook.getCode() + " đã được sử dụng trong năm " + projectRegistrationDate.getCode());
            }

            ProjectPaperSize projectPaperSize = projectPaperSizeService.findById(
                    projectRegistrationDate.getProjectPaperSize().getId()
            ).orElseThrow(() -> {
                throw new DataNotFoundException("Không tìm thấy khổ giấy của tài liệu");
            });

            ProjectRegistrationType projectRegistrationType = projectRegistrationTypeService.findById(
                    projectPaperSize.getProjectRegistrationType().getId()
            ).orElseThrow(() -> {
                throw new DataNotFoundException("Không tìm thấy loại tài liệu đăng ký");
            });

            ProjectWard projectWard = projectWardService.findById(
                    projectRegistrationType.getProjectWard().getId()
            ).orElseThrow(() -> {
                throw new DataNotFoundException("Không tìm thấy phường/xã đăng ký");
            });

            ProjectDistrict projectDistrict = projectDistrictService.findById(
                    projectWard.getProjectDistrict().getId()
            ).orElseThrow(() -> {
                throw new DataNotFoundException("Không tìm thấy thành phố/quận/huyện đăng ký");
            });

            ProjectProvince projectProvince = projectProvinceService.findById(
                    projectDistrict.getProjectProvince().getId()
            ).orElseThrow(() -> {
                throw new DataNotFoundException("Không tìm thấy tỉnh/thành phố đăng ký");
            });

            Project project = projectService.findById(
                    projectProvince.getProject().getId()
            ).orElseThrow(() -> {
                throw new DataNotFoundException("Không tìm thấy dự án đăng ký");
            });

            String coverFileName = projectWard.getCode() + "." +
                    projectRegistrationType.getCode() + "." +
                    projectPaperSize.getCode() + "." +
                    projectRegistrationDate.getCode() + "." +
                    projectNumberBook.getCode();

            String coverFolderPath = project.getFolder() + "/" +
                    projectProvince.getCode() + "/" +
                    projectDistrict.getCode() + "/" +
                    projectWard.getCode() + "/" +
                    projectRegistrationType.getCode() + "/" +
                    projectPaperSize.getCode() + "/" +
                    projectRegistrationDate.getCode() + "/" +
                    projectNumberBook.getCode();

            Long fileSize = coverFile.getSize() / 1024; // KB

            ProjectNumberBookCover projectNumberBookCover = new ProjectNumberBookCover()
                    .setFileName(coverFileName)
                    .setFolderPath(coverFolderPath)
                    .setFileSize(fileSize);

            projectNumberBookCover = projectNumberBookCoverService.create(projectNumberBookCover, coverFile);

            projectNumberBook.setCreatedBy(user);
            projectNumberBook.setStatus(EProjectNumberBookStatus.NEW);
            projectNumberBook.setProjectNumberBookCover(projectNumberBookCover);

            return projectNumberBookRepository.save(projectNumberBook);
        }
    }

    @Override
    public void update(ProjectNumberBook projectNumberBook) {
        projectNumberBookRepository.save(projectNumberBook);
    }

    @Override
    public void delete(ProjectNumberBook projectNumberBook) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
