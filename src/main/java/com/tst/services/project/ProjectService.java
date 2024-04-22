package com.tst.services.project;

import com.tst.models.entities.Project;
import com.tst.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProjectService implements IProjectService {

    private final ProjectRepository projectRepository;


    @Override
    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    @Override
    public void delete(Project project) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
