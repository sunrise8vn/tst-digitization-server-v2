package com.tst.repositories.extractShort;

import com.tst.models.entities.Project;
import com.tst.models.entities.extractShort.ParentsChildrenExtractShort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParentsChildrenExtractShortRepository extends JpaRepository<ParentsChildrenExtractShort, Long> {

    List<ParentsChildrenExtractShort> findByProjectAndImporterIsNull(Project project);
}
