package com.tst.services.accessPointHistory;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.AccessPointHistory;
import com.tst.models.entities.Project;
import com.tst.models.entities.User;
import com.tst.models.responses.accessPoint.ImporterAccessPointHistoryResponse;
import com.tst.repositories.AccessPointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AccessPointHistoryService implements IAccessPointHistoryService {

    private final AccessPointHistoryRepository accessPointHistoryRepository;


    @Override
    public Optional<AccessPointHistory> findById(Long id) {
        return accessPointHistoryRepository.findById(id);
    }

    @Override
    public Optional<AccessPointHistory> findByProjectAndAccessPointAndAssignees(
            Project project,
            AccessPoint accessPoint,
            User assignees
    ) {
        return accessPointHistoryRepository.findByProjectAndAccessPointAndAssignees(project, accessPoint, assignees);
    }

    @Override
    public List<ImporterAccessPointHistoryResponse> findAllNotDoneByProjectAndAssignees(Project project, User assignees) {
        return accessPointHistoryRepository.findAllNotDoneByProjectAndAssignees(project, assignees);
    }

    @Override
    public void delete(AccessPointHistory accessPointHistory) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
