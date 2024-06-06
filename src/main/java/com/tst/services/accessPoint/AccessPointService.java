package com.tst.services.accessPoint;

import com.tst.exceptions.DataInputException;
import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.AccessPointHistory;
import com.tst.models.entities.Project;
import com.tst.models.entities.User;
import com.tst.models.entities.extractFull.*;
import com.tst.models.entities.extractShort.*;
import com.tst.models.enums.EAccessPointStatus;
import com.tst.models.enums.EInputStatus;
import com.tst.models.responses.report.AccessPointResponse;
import com.tst.repositories.AccessPointHistoryRepository;
import com.tst.repositories.AccessPointRepository;
import com.tst.repositories.extractFull.*;
import com.tst.repositories.extractShort.*;
import com.tst.services.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AccessPointService implements IAccessPointService {

    private final AccessPointRepository accessPointRepository;
    private final AccessPointHistoryRepository accessPointHistoryRepository;

    private final ParentsChildrenExtractShortRepository parentsChildrenExtractShortRepository;
    private final ParentsChildrenExtractFullRepository parentsChildrenExtractFullRepository;
    private final BirthExtractShortRepository birthExtractShortRepository;
    private final BirthExtractFullRepository birthExtractFullRepository;
    private final MarryExtractShortRepository marryExtractShortRepository;
    private final MarryExtractFullRepository marryExtractFullRepository;
    private final WedlockExtractShortRepository wedlockExtractShortRepository;
    private final WedlockExtractFullRepository wedlockExtractFullRepository;
    private final DeathExtractShortRepository deathExtractShortRepository;
    private final DeathExtractFullRepository deathExtractFullRepository;

    private final BatchService batchService;


    @Override
    public Optional<AccessPoint> findById(Long id) {
        return accessPointRepository.findById(id);
    }

    @Override
    public List<AccessPointResponse> findAllAccessPointProcessingByProjectAndUserAndStatusNot(Project project, User user, EAccessPointStatus status) {
        return accessPointRepository.findAllAccessPointProcessingByProjectAndUserAndStatusNot(project, user, status);
   }

    @Override
    public List<AccessPointResponse> findAllAccessPointProcessingByProjectAndStatusNot(Project project, EAccessPointStatus status) {
        return accessPointRepository.findAllAccessPointProcessingByProjectAndStatusNot(project, status);
    }

    @Override
    @Transactional
    public void revokeExtractForm(AccessPoint accessPoint, Long totalCountRevoke) {
        List<ParentsChildrenExtractShort> parentsChildrenExtractShorts = parentsChildrenExtractShortRepository.findAllParentsChildrenSameByAccessPointAndStatusNewOrLater(accessPoint);
        List<ParentsChildrenExtractFull> parentsChildrenExtractFulls = parentsChildrenExtractFullRepository.findAllParentsChildrenSameByAccessPointAndStatusNewOrLater(accessPoint);
        List<BirthExtractShort> birthExtractShorts = birthExtractShortRepository.findAllBirthSameByAccessPointAndStatusNewOrLater(accessPoint);
        List<BirthExtractFull> birthExtractFulls = birthExtractFullRepository.findAllBirthSameByAccessPointAndStatusNewOrLater(accessPoint);
        List<MarryExtractShort> marryExtractShorts = marryExtractShortRepository.findMarrySameByAccessPointAndStatusNewOrLater(accessPoint);
        List<MarryExtractFull> marryExtractFulls = marryExtractFullRepository.findAllMarrySameByAccessPointAndStatusNewOrLater(accessPoint);
        List<WedlockExtractShort> wedlockExtractShorts = wedlockExtractShortRepository.findAllWedlockSameByAccessPointAndStatusNewOrLater(accessPoint);
        List<WedlockExtractFull> wedlockExtractFulls = wedlockExtractFullRepository.findAllWedlockSameByAccessPointAndStatusNewOrLater(accessPoint);
        List<DeathExtractShort> deathExtractShorts = deathExtractShortRepository.findAllDeathSameByAccessPointAndStatusNewOrLater(accessPoint);
        List<DeathExtractFull> deathExtractFulls = deathExtractFullRepository.findAllDeathSameByAccessPointAndStatusNewOrLater(accessPoint);

        long totalCountParentsChildRemaining = parentsChildrenExtractShorts.size() + parentsChildrenExtractFulls.size();
        long totalCountBirthRemaining = birthExtractShorts.size() + birthExtractFulls.size();
        long totalCountMarryRemaining = marryExtractShorts.size() + marryExtractFulls.size();
        long totalCountWedlockRemaining = wedlockExtractShorts.size() + wedlockExtractFulls.size();
        long totalCountDeathRemaining = deathExtractShorts.size() + deathExtractFulls.size();

        long totalCountRemaining = totalCountParentsChildRemaining +
                totalCountBirthRemaining +
                totalCountMarryRemaining +
                totalCountWedlockRemaining +
                totalCountDeathRemaining;

        if (totalCountRevoke > totalCountRemaining) {
            throw new DataInputException("Số lượng biểu mẫu chưa nhập chỉ còn " + totalCountRemaining);
        }

        long totalCountRevoked = totalCountRevoke;
        long totalCountExtractShortRevoked = 0L;
        long totalCountExtractFullRevoked = 0L;

        Map<String, AccessPointHistory> accessPointHistoryMap = accessPointHistoryRepository.findByAccessPoint(accessPoint)
                .stream()
                .collect(Collectors.toMap(item -> item.getAssignees().getId(), Function.identity()));

        List<ParentsChildrenExtractShort> parentsChildrenExtractShortsModified = new ArrayList<>();
        List<ParentsChildrenExtractFull> parentsChildrenExtractFullsModified = new ArrayList<>();
        List<BirthExtractShort> birthExtractShortsModified = new ArrayList<>();
        List<BirthExtractFull> birthExtractFullsModified = new ArrayList<>();
        List<MarryExtractShort> marryExtractShortsModified = new ArrayList<>();
        List<MarryExtractFull> marryExtractFullsModified = new ArrayList<>();
        List<WedlockExtractShort> wedlockExtractShortsModified = new ArrayList<>();
        List<WedlockExtractFull> wedlockExtractFullsModified = new ArrayList<>();
        List<DeathExtractShort> deathExtractShortsModified = new ArrayList<>();
        List<DeathExtractFull> deathExtractFullsModified = new ArrayList<>();

        for (ParentsChildrenExtractShort item : parentsChildrenExtractShorts) {
            if (totalCountRevoked <= 0) {
                break;
            }

            AccessPointHistory accessPointHistoryShort =  accessPointHistoryMap.get(item.getImporter().getId());
            accessPointHistoryShort.setCountExtractShort(accessPointHistoryShort.getCountExtractShort() - 1);
            accessPointHistoryShort.setTotalCount(accessPointHistoryShort.getTotalCount() - 1);
            accessPointHistoryShort.setCountRevokeExtractShort(accessPointHistoryShort.getCountRevokeExtractShort() + 1);
            accessPointHistoryShort.setTotalCountRevoke(accessPointHistoryShort.getTotalCountRevoke() + 1);

            item.setStatus(EInputStatus.NEW);
            item.setImporter(null);
            item.setImportedAt(null);
            item.setAccessPoint(null);
            parentsChildrenExtractShortsModified.add(item);

            totalCountExtractShortRevoked++;
            totalCountRevoked--;
        }

        for (ParentsChildrenExtractFull item : parentsChildrenExtractFulls) {
            if (totalCountRevoked <= 0) {
                break;
            }

            AccessPointHistory accessPointHistoryFull =  accessPointHistoryMap.get(item.getImporter().getId());
            accessPointHistoryFull.setCountExtractFull(accessPointHistoryFull.getCountExtractFull() - 1);
            accessPointHistoryFull.setTotalCount(accessPointHistoryFull.getTotalCount() - 1);
            accessPointHistoryFull.setCountRevokeExtractFull(accessPointHistoryFull.getCountRevokeExtractFull() + 1);
            accessPointHistoryFull.setTotalCountRevoke(accessPointHistoryFull.getTotalCountRevoke() + 1);

            item.setStatus(EInputStatus.NEW);
            item.setImporter(null);
            item.setImportedAt(null);
            item.setAccessPoint(null);
            parentsChildrenExtractFullsModified.add(item);

            totalCountExtractFullRevoked++;
            totalCountRevoked--;
        }

        for (BirthExtractShort item : birthExtractShorts) {
            if (totalCountRevoked <= 0) {
                break;
            }

            AccessPointHistory accessPointHistoryShort =  accessPointHistoryMap.get(item.getImporter().getId());
            accessPointHistoryShort.setCountExtractShort(accessPointHistoryShort.getCountExtractShort() - 1);
            accessPointHistoryShort.setTotalCount(accessPointHistoryShort.getTotalCount() - 1);
            accessPointHistoryShort.setCountRevokeExtractShort(accessPointHistoryShort.getCountRevokeExtractShort() + 1);
            accessPointHistoryShort.setTotalCountRevoke(accessPointHistoryShort.getTotalCountRevoke() + 1);

            item.setStatus(EInputStatus.NEW);
            item.setImporter(null);
            item.setImportedAt(null);
            item.setAccessPoint(null);
            birthExtractShortsModified.add(item);

            totalCountExtractShortRevoked++;
            totalCountRevoked--;
        }

        for (BirthExtractFull item : birthExtractFulls) {
            if (totalCountRevoked <= 0) {
                break;
            }

            AccessPointHistory accessPointHistoryFull =  accessPointHistoryMap.get(item.getImporter().getId());
            accessPointHistoryFull.setCountExtractFull(accessPointHistoryFull.getCountExtractFull() - 1);
            accessPointHistoryFull.setTotalCount(accessPointHistoryFull.getTotalCount() - 1);
            accessPointHistoryFull.setCountRevokeExtractFull(accessPointHistoryFull.getCountRevokeExtractFull() + 1);
            accessPointHistoryFull.setTotalCountRevoke(accessPointHistoryFull.getTotalCountRevoke() + 1);

            item.setStatus(EInputStatus.NEW);
            item.setImporter(null);
            item.setImportedAt(null);
            item.setAccessPoint(null);
            birthExtractFullsModified.add(item);

            totalCountExtractFullRevoked++;
            totalCountRevoked--;
        }

        for (MarryExtractShort item : marryExtractShorts) {
            if (totalCountRevoked <= 0) {
                break;
            }

            AccessPointHistory accessPointHistoryShort =  accessPointHistoryMap.get(item.getImporter().getId());
            accessPointHistoryShort.setCountExtractShort(accessPointHistoryShort.getCountExtractShort() - 1);
            accessPointHistoryShort.setTotalCount(accessPointHistoryShort.getTotalCount() - 1);
            accessPointHistoryShort.setCountRevokeExtractShort(accessPointHistoryShort.getCountRevokeExtractShort() + 1);
            accessPointHistoryShort.setTotalCountRevoke(accessPointHistoryShort.getTotalCountRevoke() + 1);

            item.setStatus(EInputStatus.NEW);
            item.setImporter(null);
            item.setImportedAt(null);
            item.setAccessPoint(null);
            marryExtractShortsModified.add(item);

            totalCountExtractShortRevoked++;
            totalCountRevoked--;
        }

        for (MarryExtractFull item : marryExtractFulls) {
            if (totalCountRevoked <= 0) {
                break;
            }

            AccessPointHistory accessPointHistoryFull =  accessPointHistoryMap.get(item.getImporter().getId());
            accessPointHistoryFull.setCountExtractFull(accessPointHistoryFull.getCountExtractFull() - 1);
            accessPointHistoryFull.setTotalCount(accessPointHistoryFull.getTotalCount() - 1);
            accessPointHistoryFull.setCountRevokeExtractFull(accessPointHistoryFull.getCountRevokeExtractFull() + 1);
            accessPointHistoryFull.setTotalCountRevoke(accessPointHistoryFull.getTotalCountRevoke() + 1);

            item.setStatus(EInputStatus.NEW);
            item.setImporter(null);
            item.setImportedAt(null);
            item.setAccessPoint(null);
            marryExtractFullsModified.add(item);

            totalCountExtractFullRevoked++;
            totalCountRevoked--;
        }

        for (WedlockExtractShort item : wedlockExtractShorts) {
            if (totalCountRevoked <= 0) {
                break;
            }

            AccessPointHistory accessPointHistoryShort =  accessPointHistoryMap.get(item.getImporter().getId());
            accessPointHistoryShort.setCountExtractShort(accessPointHistoryShort.getCountExtractShort() - 1);
            accessPointHistoryShort.setTotalCount(accessPointHistoryShort.getTotalCount() - 1);
            accessPointHistoryShort.setCountRevokeExtractShort(accessPointHistoryShort.getCountRevokeExtractShort() + 1);
            accessPointHistoryShort.setTotalCountRevoke(accessPointHistoryShort.getTotalCountRevoke() + 1);

            item.setStatus(EInputStatus.NEW);
            item.setImporter(null);
            item.setImportedAt(null);
            item.setAccessPoint(null);
            wedlockExtractShortsModified.add(item);

            totalCountExtractShortRevoked++;
            totalCountRevoked--;
        }

        for (WedlockExtractFull item : wedlockExtractFulls) {
            if (totalCountRevoked <= 0) {
                break;
            }

            AccessPointHistory accessPointHistoryFull =  accessPointHistoryMap.get(item.getImporter().getId());
            accessPointHistoryFull.setCountExtractFull(accessPointHistoryFull.getCountExtractFull() - 1);
            accessPointHistoryFull.setTotalCount(accessPointHistoryFull.getTotalCount() - 1);
            accessPointHistoryFull.setCountRevokeExtractFull(accessPointHistoryFull.getCountRevokeExtractFull() + 1);
            accessPointHistoryFull.setTotalCountRevoke(accessPointHistoryFull.getTotalCountRevoke() + 1);

            item.setStatus(EInputStatus.NEW);
            item.setImporter(null);
            item.setImportedAt(null);
            item.setAccessPoint(null);
            wedlockExtractFullsModified.add(item);

            totalCountExtractFullRevoked++;
            totalCountRevoked--;
        }

        for (DeathExtractShort item : deathExtractShorts) {
            if (totalCountRevoked <= 0) {
                break;
            }

            AccessPointHistory accessPointHistoryShort =  accessPointHistoryMap.get(item.getImporter().getId());
            accessPointHistoryShort.setCountExtractShort(accessPointHistoryShort.getCountExtractShort() - 1);
            accessPointHistoryShort.setTotalCount(accessPointHistoryShort.getTotalCount() - 1);
            accessPointHistoryShort.setCountRevokeExtractShort(accessPointHistoryShort.getCountRevokeExtractShort() + 1);
            accessPointHistoryShort.setTotalCountRevoke(accessPointHistoryShort.getTotalCountRevoke() + 1);

            item.setStatus(EInputStatus.NEW);
            item.setImporter(null);
            item.setImportedAt(null);
            item.setAccessPoint(null);
            deathExtractShortsModified.add(item);

            totalCountExtractShortRevoked++;
            totalCountRevoked--;
        }

        for (DeathExtractFull item : deathExtractFulls) {
            if (totalCountRevoked <= 0) {
                break;
            }

            AccessPointHistory accessPointHistoryFull =  accessPointHistoryMap.get(item.getImporter().getId());
            accessPointHistoryFull.setCountExtractFull(accessPointHistoryFull.getCountExtractFull() - 1);
            accessPointHistoryFull.setTotalCount(accessPointHistoryFull.getTotalCount() - 1);
            accessPointHistoryFull.setCountRevokeExtractFull(accessPointHistoryFull.getCountRevokeExtractFull() + 1);
            accessPointHistoryFull.setTotalCountRevoke(accessPointHistoryFull.getTotalCountRevoke() + 1);

            item.setStatus(EInputStatus.NEW);
            item.setImporter(null);
            item.setImportedAt(null);
            item.setAccessPoint(null);
            deathExtractFullsModified.add(item);

            totalCountExtractFullRevoked++;
            totalCountRevoked--;
        }

        // Lưu tất cả các đối tượng cùng một lúc sau khi xử lý xong
        if (!parentsChildrenExtractShortsModified.isEmpty()) {
            batchService.batchUpdate(parentsChildrenExtractShortsModified);
        }

        if (!parentsChildrenExtractFullsModified.isEmpty()) {
            batchService.batchUpdate(parentsChildrenExtractFullsModified);
        }

        if (!birthExtractShortsModified.isEmpty()) {
            batchService.batchUpdate(birthExtractShortsModified);
        }

        if (!birthExtractFullsModified.isEmpty()) {
            batchService.batchUpdate(birthExtractFullsModified);
        }

        if (!marryExtractShortsModified.isEmpty()) {
            batchService.batchUpdate(marryExtractShortsModified);
        }

        if (!marryExtractFullsModified.isEmpty()) {
            batchService.batchUpdate(marryExtractFullsModified);
        }

        if (!wedlockExtractShortsModified.isEmpty()) {
            batchService.batchUpdate(wedlockExtractShortsModified);
        }

        if (!wedlockExtractFullsModified.isEmpty()) {
            batchService.batchUpdate(wedlockExtractFullsModified);
        }

        if (!deathExtractShortsModified.isEmpty()) {
            batchService.batchUpdate(deathExtractShortsModified);
        }

        if (!deathExtractFullsModified.isEmpty()) {
            batchService.batchUpdate(deathExtractFullsModified);
        }

        accessPoint.setCountExtractShort(accessPoint.getCountExtractShort() - totalCountExtractShortRevoked);
        accessPoint.setCountExtractFull(accessPoint.getCountExtractFull() - totalCountExtractFullRevoked);
        accessPoint.setTotalCount(accessPoint.getTotalCount() - totalCountRevoke);
        accessPoint.setTotalCountRevoke(accessPoint.getTotalCountRevoke() + totalCountRevoke);
        accessPointRepository.save(accessPoint);

        accessPointHistoryRepository.saveAll(accessPointHistoryMap.values());

    }

    @Override
    public void delete(AccessPoint accessPoint) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
