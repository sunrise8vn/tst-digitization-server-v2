package com.tst.services.parentsChildren;

import com.tst.models.entities.ParentsChildren;
import com.tst.repositories.ParentsChildrenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ParentsChildrenService implements IParentsChildrenService {

    private final ParentsChildrenRepository parentsChildrenRepository;


    @Override
    public Optional<ParentsChildren> findById(Long id) {
        return parentsChildrenRepository.findById(id);
    }

    @Override
    public void delete(ParentsChildren parentsChildren) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
