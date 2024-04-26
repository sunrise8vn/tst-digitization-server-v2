package com.tst.models.dtos.project;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tst.models.entities.ProjectNumberBook;
import com.tst.models.entities.ProjectRegistrationDate;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProjectNumberBookCreateDTO {

    private String code;
    private Long projectRegistrationDateId;

    private MultipartFile coverFile;

    public ProjectNumberBook toProjectNumberBook(ProjectRegistrationDate projectRegistrationDate) {
        return new ProjectNumberBook()
                .setCode(code)
                .setProjectRegistrationDate(projectRegistrationDate)
                .setA0(0)
                .setA1(0)
                .setA2(0)
                .setA3(0)
                .setA4(0)
                .setConvertA4(0)
                ;
    }
}
