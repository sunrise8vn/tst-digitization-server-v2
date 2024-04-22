package com.tst.services.projectRegistrationDate;

import com.tst.models.entities.ProjectRegistrationDate;
import com.tst.repositories.ProjectRegistrationDateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProjectRegistrationDateService implements IProjectRegistrationDateService {

    private final ProjectRegistrationDateRepository projectRegistrationDateRepository;


    @Override
    public Optional<ProjectRegistrationDate> findById(Long id) {
        return projectRegistrationDateRepository.findById(id);
    }

    @Override
    public void delete(ProjectRegistrationDate projectRegistrationDate) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
