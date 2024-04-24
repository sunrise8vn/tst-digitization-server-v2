package com.tst.repositories;

import com.tst.models.entities.ProjectNumberBook;
import com.tst.models.entities.ProjectRegistrationDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectNumberBookRepository extends JpaRepository<ProjectNumberBook, Long> {

    Boolean existsByProjectRegistrationDateAndCode(ProjectRegistrationDate projectRegistrationDate, String code);

}
