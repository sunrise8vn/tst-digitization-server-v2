package com.tst.services.deathNoticeType;

import com.tst.models.entities.DeathNoticeType;
import com.tst.repositories.DeathNoticeTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DeathNoticeTypeService implements IDeathNoticeTypeService {

    private final DeathNoticeTypeRepository deathNoticeTypeRepository;


    @Override
    public Optional<DeathNoticeType> findById(Long id) {
        return deathNoticeTypeRepository.findById(id);
    }

    @Override
    public void delete(DeathNoticeType deathNoticeType) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
