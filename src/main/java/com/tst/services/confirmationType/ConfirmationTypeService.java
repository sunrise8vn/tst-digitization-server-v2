package com.tst.services.confirmationType;

import com.tst.models.entities.ConfirmationType;
import com.tst.models.responses.typeList.ConfirmationTypeResponse;
import com.tst.repositories.ConfirmationTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ConfirmationTypeService implements IConfirmationTypeService {

    private final ConfirmationTypeRepository confirmationTypeRepository;


    @Override
    public Optional<ConfirmationType> findById(Long id) {
        return confirmationTypeRepository.findById(id);
    }

    @Override
    public List<ConfirmationTypeResponse> findAllConfirmationTypeResponse() {
        return confirmationTypeRepository.findAllConfirmationTypeResponse();
    }

    @Override
    public void delete(ConfirmationType confirmationType) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
