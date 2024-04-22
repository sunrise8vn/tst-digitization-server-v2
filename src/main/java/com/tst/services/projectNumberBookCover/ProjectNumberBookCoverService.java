package com.tst.services.projectNumberBookCover;

import com.tst.models.entities.ProjectNumberBookCover;
import com.tst.repositories.ProjectNumberBookCoverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProjectNumberBookCoverService implements IProjectNumberBookCoverService {

    private final ProjectNumberBookCoverRepository projectNumberBookCoverRepository;


    @Override
    public Optional<ProjectNumberBookCover> findById(String id) {
        return projectNumberBookCoverRepository.findById(id);
    }

    @Override
    public void delete(ProjectNumberBookCover projectNumberBookCover) {

    }

    @Override
    public void deleteById(String id) {

    }
}
