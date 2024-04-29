package com.tst.models.dtos.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tst.models.entities.ProjectNumberBook;
import com.tst.models.entities.ProjectRegistrationDate;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProjectNumberBookCreateDTO {

    private String code;
    private Long project_registration_date_id;
    private MultipartFile cover_file;

    public ProjectNumberBook toProjectNumberBook(ProjectRegistrationDate projectRegistrationDate) {
        return new ProjectNumberBook()
                .setCode(code)
                .setProjectRegistrationDate(projectRegistrationDate)
                ;
    }
}
