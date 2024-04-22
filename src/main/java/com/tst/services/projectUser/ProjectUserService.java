package com.tst.services.projectUser;

import com.tst.models.entities.ProjectUser;
import com.tst.repositories.ProjectUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProjectUserService implements IProjectUserService {

    private final ProjectUserRepository projectUserRepository;


    @Override
    public Optional<ProjectUser> findById(Long id) {
        return projectUserRepository.findById(id);
    }

    @Override
    public void delete(ProjectUser projectUser) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
