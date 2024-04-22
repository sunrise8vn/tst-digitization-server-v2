package com.tst.services.projectRegistrationType;

import com.tst.models.entities.ProjectRegistrationType;
import com.tst.repositories.ProjectRegistrationTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProjectRegistrationTypeService implements IProjectRegistrationTypeService {

    private final ProjectRegistrationTypeRepository projectRegistrationTypeRepository;


    @Override
    public Optional<ProjectRegistrationType> findById(Long id) {
        return projectRegistrationTypeRepository.findById(id);
    }

    @Override
    public void delete(ProjectRegistrationType projectRegistrationType) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
