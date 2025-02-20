package com.tst.services.deathNoticeType;

import com.tst.models.entities.DeathNoticeType;
import com.tst.models.responses.typeList.DeathNoticeTypeResponse;
import com.tst.repositories.DeathNoticeTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public List<DeathNoticeTypeResponse> findAllDeathNoticeTypeResponse() {
        return deathNoticeTypeRepository.findAllDeathNoticeTypeResponse();
    }

    @Override
    public void delete(DeathNoticeType deathNoticeType) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
