package com.tst.repositories.extractFull;

import com.tst.models.entities.Project;
import com.tst.models.entities.extractFull.WedlockExtractFull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WedlockExtractFullRepository extends JpaRepository<WedlockExtractFull, Long> {

    List<WedlockExtractFull> findByProjectAndImporterIsNull(Project project);
}
