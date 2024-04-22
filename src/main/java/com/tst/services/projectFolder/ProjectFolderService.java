package com.tst.services.projectFolder;

import com.tst.models.entities.ProjectFolder;
import com.tst.repositories.ProjectFolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProjectFolderService implements IProjectFolderService {
    private final ProjectFolderRepository projectFolderRepository;


    @Override
    public Optional<ProjectFolder> findById(Long id) {
        return projectFolderRepository.findById(id);
    }

    @Override
    public void delete(ProjectFolder projectFolder) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
