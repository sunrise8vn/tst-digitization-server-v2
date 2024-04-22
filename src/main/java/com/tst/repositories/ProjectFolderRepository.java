package com.tst.repositories;

import com.tst.models.entities.ProjectFolder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectFolderRepository extends JpaRepository<ProjectFolder, Long> {
}
