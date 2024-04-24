package com.tst.api;

import com.tst.exceptions.DataNotFoundException;
import com.tst.models.dtos.project.ProjectNumberBookCreateDTO;
import com.tst.models.entities.ProjectNumberBook;
import com.tst.models.entities.ProjectRegistrationDate;
import com.tst.models.responses.ResponseObject;
import com.tst.services.projectNumberBook.IProjectNumberBookService;
import com.tst.services.projectNumberBookCover.IProjectNumberBookCoverService;
import com.tst.services.projectRegistrationDate.IProjectRegistrationDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("${api.prefix}/projects")
@RequiredArgsConstructor
public class ProjectAPI {

    private final IProjectNumberBookService projectNumberBookService;
    private final IProjectRegistrationDateService projectRegistrationDateService;
    private final IProjectNumberBookCoverService projectNumberBookCoverService;

    @PostMapping("/number-book")
    public ResponseEntity<ResponseObject> createProjectNumberBook(ProjectNumberBookCreateDTO projectNumberBookCreateDTO) {

        ProjectRegistrationDate projectRegistrationDate = projectRegistrationDateService.findById(
                projectNumberBookCreateDTO.getProjectRegistrationDateId()
        ).orElseThrow(() -> {
            throw new DataNotFoundException("Năm đăng ký không tồn tại");
        });

        ProjectNumberBook projectNumberBook = projectNumberBookCreateDTO.toProjectNumberBook(projectRegistrationDate);

        projectNumberBookService.create(projectNumberBook, projectNumberBookCreateDTO.getCoverFile());

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Create Project Number Book successfully")
                .status(HttpStatus.OK.value())
                .statusText(HttpStatus.OK)
                .data("")
                .build());
    }
}
