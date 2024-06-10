package com.tst.services.accessPointHistory;

import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.AccessPointHistory;
import com.tst.models.entities.Project;
import com.tst.models.entities.User;
import com.tst.models.responses.accessPoint.ImporterAccessPointHistoryResponse;
import com.tst.models.responses.report.ExtractFormImportedForManagerResponse;
import com.tst.models.responses.report.TotalAccessPointHistoryResponse;
import com.tst.repositories.AccessPointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
    public List<TotalAccessPointHistoryResponse> findAllNotDoneByProject(Project project) {
        return accessPointHistoryRepository.findAllNotDoneByProject(project);
    }

    @Override
    public List<TotalAccessPointHistoryResponse> findAllNotDoneByProjectAndAccessPoint(Project project, AccessPoint accessPoint) {
        return accessPointHistoryRepository.findAllNotDoneByProjectAndAccessPoint(project, accessPoint);
    }

    @Override
    public List<ExtractFormImportedForManagerResponse> findAllExtractFormImportedForManager(Long projectId) {
        List<Object[]> results = accessPointHistoryRepository.findAllExtractFormImportedForManager(projectId);

        return results.stream()
                .map(row -> new ExtractFormImportedForManagerResponse()
                        .setAccessPointId(((Long) row[0]))
                        .setFullName(((String) row[1]))
                        .setCountExtractShort(((Long) row[2]))
                        .setCountExtractFull(((Long) row[3]))
                        .setCountExtractShortImported(((Long) row[4]))
                        .setCountExtractFullImported(((Long) row[5])))
                .collect(Collectors.toList());
    }

    @Override
    public List<ExtractFormImportedForManagerResponse> findAllExtractFormImportedByAccessPointForManager(Long projectId, Long accessPointId) {
        List<Object[]> results = accessPointHistoryRepository.findAllExtractFormImportedByAccessPointForManager(projectId, accessPointId);

        return results.stream()
                .map(row -> new ExtractFormImportedForManagerResponse()
                        .setAccessPointId(((Long) row[0]))
                        .setFullName(((String) row[1]))
                        .setCountExtractShort(((Long) row[2]))
                        .setCountExtractFull(((Long) row[3]))
                        .setCountExtractShortImported(((Long) row[4]))
                        .setCountExtractFullImported(((Long) row[5])))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(AccessPointHistory accessPointHistory) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
