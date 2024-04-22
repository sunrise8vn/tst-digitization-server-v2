package com.tst.services.projectWard;

import com.tst.models.entities.ProjectWard;
import com.tst.repositories.ProjectWardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProjectWardService implements IProjectWardService {

    private final ProjectWardRepository projectWardRepository;


    @Override
    public Optional<ProjectWard> findById(Long id) {
        return projectWardRepository.findById(id);
    }

    @Override
    public void delete(ProjectWard projectWard) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
