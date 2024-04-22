package com.tst.services.projectPaperSize;

import com.tst.models.entities.ProjectPaperSize;
import com.tst.repositories.ProjectPaperSizeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProjectPaperSizeService implements IProjectPaperSizeService {

    private final ProjectPaperSizeRepository projectPaperSizeRepository;


    @Override
    public Optional<ProjectPaperSize> findById(Long id) {
        return projectPaperSizeRepository.findById(id);
    }

    @Override
    public void delete(ProjectPaperSize projectPaperSize) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
