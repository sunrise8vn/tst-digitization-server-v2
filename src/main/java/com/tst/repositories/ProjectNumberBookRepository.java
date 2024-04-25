package com.tst.repositories;

import com.tst.models.entities.ProjectNumberBook;
import com.tst.models.entities.ProjectRegistrationDate;
import com.tst.models.enums.EProjectNumberBookStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectNumberBookRepository extends JpaRepository<ProjectNumberBook, Long> {

    Boolean existsByProjectRegistrationDateAndCode(ProjectRegistrationDate projectRegistrationDate, String code);

    Optional<ProjectNumberBook> findByIdAndStatus(Long id, EProjectNumberBookStatus status);

}
