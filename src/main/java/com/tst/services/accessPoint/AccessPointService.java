package com.tst.services.accessPoint;

import com.tst.exceptions.DataInputException;
import com.tst.models.entities.AccessPoint;
import com.tst.models.entities.AccessPointHistory;
import com.tst.models.entities.AccessPointRevoke;
import com.tst.models.entities.extractFull.*;
import com.tst.models.entities.extractShort.*;
import com.tst.models.enums.EInputStatus;
import com.tst.repositories.AccessPointHistoryRepository;
import com.tst.repositories.AccessPointRepository;
import com.tst.repositories.AccessPointRevokeRepository;
import com.tst.repositories.extractFull.*;
import com.tst.repositories.extractShort.*;
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
    private final AccessPointRevokeRepository accessPointRevokeRepository;

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


    @Override
    public Optional<AccessPoint> findById(Long id) {
        return accessPointRepository.findById(id);
    }

    @Override
    @Transactional
    public void revokeExtractForm(AccessPoint accessPoint, Long totalCountRevoke) {
        List<ParentsChildrenExtractShort> parentsChildrenExtractShorts = parentsChildrenExtractShortRepository.findParentsChildrenSameByAccessPointAndStatusNewOrLater(accessPoint);
        List<ParentsChildrenExtractFull> parentsChildrenExtractFulls = parentsChildrenExtractFullRepository.findParentsChildrenSameByAccessPointAndStatusNewOrLater(accessPoint);
        List<BirthExtractShort> birthExtractShorts = birthExtractShortRepository.findBirthSameByAccessPointAndStatusNewOrLater(accessPoint);
        List<BirthExtractFull> birthExtractFulls = birthExtractFullRepository.findBirthSameByAccessPointAndStatusNewOrLater(accessPoint);
        List<MarryExtractShort> marryExtractShorts = marryExtractShortRepository.findMarrySameByAccessPointAndStatusNewOrLater(accessPoint);
        List<MarryExtractFull> marryExtractFulls = marryExtractFullRepository.findMarrySameByAccessPointAndStatusNewOrLater(accessPoint);
        List<WedlockExtractShort> wedlockExtractShorts = wedlockExtractShortRepository.findWedlockSameByAccessPointAndStatusNewOrLater(accessPoint);
        List<WedlockExtractFull> wedlockExtractFulls = wedlockExtractFullRepository.findWedlockSameByAccessPointAndStatusNewOrLater(accessPoint);
        List<DeathExtractShort> deathExtractShorts = deathExtractShortRepository.findDeathSameByAccessPointAndStatusNewOrLater(accessPoint);
        List<DeathExtractFull> deathExtractFulls = deathExtractFullRepository.findDeathSameByAccessPointAndStatusNewOrLater(accessPoint);

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

        Map<String, AccessPointHistory> accessPointHistoryMap = accessPointHistoryRepository.findByAccessPoint(accessPoint)
                .stream()
                .collect(Collectors.toMap(history -> history.getAssignees().getId(), Function.identity()));

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
            if (totalCountRevoke <= 0) {
                break;
            }

            AccessPointHistory accessPointHistoryShort =  accessPointHistoryMap.get(item.getImporter().getId());
            accessPointHistoryShort.setCountExtractShort(accessPointHistoryShort.getCountExtractShort() - 1);

            item.setStatus(EInputStatus.NEW);
            item.setImporter(null);
            item.setAccessPoint(null);
            parentsChildrenExtractShortsModified.add(item);

            totalCountRevoke--;
        }

        for (ParentsChildrenExtractFull item : parentsChildrenExtractFulls) {
            if (totalCountRevoke <= 0) {
                break;
            }

            AccessPointHistory accessPointHistoryFull =  accessPointHistoryMap.get(item.getImporter().getId());
            accessPointHistoryFull.setCountExtractFull(accessPointHistoryFull.getCountExtractFull() - 1);

            item.setStatus(EInputStatus.NEW);
            item.setImporter(null);
            item.setAccessPoint(null);
            parentsChildrenExtractFullsModified.add(item);

            totalCountRevoke--;
        }

        for (BirthExtractShort item : birthExtractShorts) {
            if (totalCountRevoke <= 0) {
                break;
            }

            AccessPointHistory accessPointHistoryShort =  accessPointHistoryMap.get(item.getImporter().getId());
            accessPointHistoryShort.setCountExtractShort(accessPointHistoryShort.getCountExtractShort() - 1);

            item.setStatus(EInputStatus.NEW);
            item.setImporter(null);
            item.setAccessPoint(null);
            birthExtractShortsModified.add(item);

            totalCountRevoke--;
        }

        for (BirthExtractFull item : birthExtractFulls) {
            if (totalCountRevoke <= 0) {
                break;
            }

            AccessPointHistory accessPointHistoryFull =  accessPointHistoryMap.get(item.getImporter().getId());
            accessPointHistoryFull.setCountExtractFull(accessPointHistoryFull.getCountExtractFull() - 1);

            item.setStatus(EInputStatus.NEW);
            item.setImporter(null);
            item.setAccessPoint(null);
            birthExtractFullsModified.add(item);

            totalCountRevoke--;
        }

        for (MarryExtractShort item : marryExtractShorts) {
            if (totalCountRevoke <= 0) {
                break;
            }

            AccessPointHistory accessPointHistoryShort =  accessPointHistoryMap.get(item.getImporter().getId());
            accessPointHistoryShort.setCountExtractShort(accessPointHistoryShort.getCountExtractShort() - 1);

            item.setStatus(EInputStatus.NEW);
            item.setImporter(null);
            item.setAccessPoint(null);
            marryExtractShortsModified.add(item);

            totalCountRevoke--;
        }

        for (MarryExtractFull item : marryExtractFulls) {
            if (totalCountRevoke <= 0) {
                break;
            }

            AccessPointHistory accessPointHistoryFull =  accessPointHistoryMap.get(item.getImporter().getId());
            accessPointHistoryFull.setCountExtractFull(accessPointHistoryFull.getCountExtractFull() - 1);

            item.setStatus(EInputStatus.NEW);
            item.setImporter(null);
            item.setAccessPoint(null);
            marryExtractFullsModified.add(item);

            totalCountRevoke--;
        }

        for (WedlockExtractShort item : wedlockExtractShorts) {
            if (totalCountRevoke <= 0) {
                break;
            }

            AccessPointHistory accessPointHistoryShort =  accessPointHistoryMap.get(item.getImporter().getId());
            accessPointHistoryShort.setCountExtractShort(accessPointHistoryShort.getCountExtractShort() - 1);

            item.setStatus(EInputStatus.NEW);
            item.setImporter(null);
            item.setAccessPoint(null);
            wedlockExtractShortsModified.add(item);

            totalCountRevoke--;
        }

        for (WedlockExtractFull item : wedlockExtractFulls) {
            if (totalCountRevoke <= 0) {
                break;
            }

            AccessPointHistory accessPointHistoryFull =  accessPointHistoryMap.get(item.getImporter().getId());
            accessPointHistoryFull.setCountExtractFull(accessPointHistoryFull.getCountExtractFull() - 1);

            item.setStatus(EInputStatus.NEW);
            item.setImporter(null);
            item.setAccessPoint(null);
            wedlockExtractFullsModified.add(item);

            totalCountRevoke--;
        }

        for (DeathExtractShort item : deathExtractShorts) {
            if (totalCountRevoke <= 0) {
                break;
            }

            AccessPointHistory accessPointHistoryShort =  accessPointHistoryMap.get(item.getImporter().getId());
            accessPointHistoryShort.setCountExtractShort(accessPointHistoryShort.getCountExtractShort() - 1);

            item.setStatus(EInputStatus.NEW);
            item.setImporter(null);
            item.setAccessPoint(null);
            deathExtractShortsModified.add(item);

            totalCountRevoke--;
        }

        for (DeathExtractFull item : deathExtractFulls) {
            if (totalCountRevoke <= 0) {
                break;
            }

            AccessPointHistory accessPointHistoryFull =  accessPointHistoryMap.get(item.getImporter().getId());
            accessPointHistoryFull.setCountExtractFull(accessPointHistoryFull.getCountExtractFull() - 1);

            item.setStatus(EInputStatus.NEW);
            item.setImporter(null);
            item.setAccessPoint(null);
            deathExtractFullsModified.add(item);

            totalCountRevoke--;
        }

        // Lưu tất cả các đối tượng cùng một lúc sau khi xử lý xong
        if (!parentsChildrenExtractShortsModified.isEmpty()) {
            parentsChildrenExtractShortRepository.saveAll(parentsChildrenExtractShortsModified);
        }

        if (!parentsChildrenExtractFullsModified.isEmpty()) {
            parentsChildrenExtractFullRepository.saveAll(parentsChildrenExtractFullsModified);
        }

        if (!birthExtractShortsModified.isEmpty()) {
            birthExtractShortRepository.saveAll(birthExtractShortsModified);
        }

        if (!birthExtractFullsModified.isEmpty()) {
            birthExtractFullRepository.saveAll(birthExtractFullsModified);
        }

        if (!marryExtractShortsModified.isEmpty()) {
            marryExtractShortRepository.saveAll(marryExtractShortsModified);
        }

        if (!marryExtractFullsModified.isEmpty()) {
            marryExtractFullRepository.saveAll(marryExtractFullsModified);
        }

        if (!wedlockExtractShortsModified.isEmpty()) {
            wedlockExtractShortRepository.saveAll(wedlockExtractShortsModified);
        }

        if (!wedlockExtractFullsModified.isEmpty()) {
            wedlockExtractFullRepository.saveAll(wedlockExtractFullsModified);
        }

        if (!deathExtractShortsModified.isEmpty()) {
            deathExtractShortRepository.saveAll(deathExtractShortsModified);
        }

        if (!deathExtractFullsModified.isEmpty()) {
            deathExtractFullRepository.saveAll(deathExtractFullsModified);
        }

        AccessPointRevoke accessPointRevoke = new AccessPointRevoke()
                .setAccessPoint(accessPoint)
                .setProject(accessPoint.getProject())
                .setTotalCountRevoke(totalCountRevoke);
        accessPointRevokeRepository.save(accessPointRevoke);

        accessPoint.setTotalCount(accessPoint.getTotalCount() - totalCountRevoke);
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
