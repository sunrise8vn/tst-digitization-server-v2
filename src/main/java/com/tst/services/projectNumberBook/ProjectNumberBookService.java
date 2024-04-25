package com.tst.services.projectNumberBook;

import com.tst.exceptions.DataInputException;
import com.tst.exceptions.DataNotFoundException;
import com.tst.models.entities.*;
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
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${server.root-folder}")
    private String serverRootFolder;


    @Override
    public Optional<ProjectNumberBook> findById(Long id) {
        return projectNumberBookRepository.findById(id);
    }

    @Override
    @Transactional
    public ProjectNumberBook create(ProjectNumberBook projectNumberBook, MultipartFile coverFile) {

        if (coverFile == null) {
            throw new DataInputException("Tập tin pdf không được bỏ trống");
        }

        String fileType = Objects.requireNonNull(coverFile.getContentType()).split("/")[1].toLowerCase();

        if (!fileType.equals("pdf")) {
            throw new DataNotFoundException("Chỉ chấp nhận tập tin pdf");
        } else {
            ProjectRegistrationDate projectRegistrationDate = projectNumberBook.getProjectRegistrationDate();

            Boolean existProjectNumberBook = projectNumberBookRepository.existsByProjectRegistrationDateAndCode(projectRegistrationDate, projectNumberBook.getCode());

            if (existProjectNumberBook) {
                throw new DataInputException("Quyển sổ " + projectNumberBook.getCode() + " đã được đăng ký trong năm " + projectRegistrationDate.getCode());
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

            String coverFileName = "Cover." +
                    projectWard.getCode() + "." +
                    projectRegistrationType.getCode() + "." +
                    projectPaperSize.getCode() + "." +
                    projectRegistrationDate.getCode() + "." +
                    projectNumberBook.getCode() +
                    ".pdf";

            String coverFolderName = project.getFolder() + "/" +
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
                    .setFolderName(coverFolderName)
                    .setFileSize(fileSize)
                    .setSizeType("KB");

            projectNumberBookCover = projectNumberBookCoverService.create(projectNumberBookCover, coverFile);

            projectNumberBook.setProjectNumberBookCover(projectNumberBookCover);

            return projectNumberBookRepository.save(projectNumberBook);
        }
    }

    @Override
    public void delete(ProjectNumberBook projectNumberBook) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
