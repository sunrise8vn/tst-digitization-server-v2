package com.tst.repositories.extractFull;

import com.tst.models.entities.Project;
import com.tst.models.entities.extractFull.ParentsChildrenExtractFull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParentsChildrenExtractFullRepository extends JpaRepository<ParentsChildrenExtractFull, Long> {

    List<ParentsChildrenExtractFull> findByProjectAndImporterIsNull(Project project);
}
