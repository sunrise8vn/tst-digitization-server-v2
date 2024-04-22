package com.tst.services.projectNumberBook;

import com.tst.models.entities.ProjectNumberBook;
import com.tst.repositories.ProjectNumberBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProjectNumberBookService implements IProjectNumberBookService {

    private final ProjectNumberBookRepository projectNumberBookRepository;


    @Override
    public Optional<ProjectNumberBook> findById(Long id) {
        return projectNumberBookRepository.findById(id);
    }

    @Override
    public void delete(ProjectNumberBook projectNumberBook) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
