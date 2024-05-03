package com.tst.repositories.extractShort;

import com.tst.models.entities.Project;
import com.tst.models.entities.extractShort.WedlockExtractShort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WedlockExtractShortRepository extends JpaRepository<WedlockExtractShort, Long> {

    List<WedlockExtractShort> findByProjectAndImporterIsNull(Project project);
}
