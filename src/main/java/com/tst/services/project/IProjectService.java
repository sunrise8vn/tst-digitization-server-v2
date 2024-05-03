package com.tst.services.project;

import com.tst.models.entities.*;
import com.tst.models.entities.locationRegion.LocationDistrict;
import com.tst.models.entities.locationRegion.LocationProvince;
import com.tst.models.entities.locationRegion.LocationWard;
import com.tst.models.enums.EPaperSize;
import com.tst.services.IGeneralService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProjectService extends IGeneralService<Project, Long> {

    void updatePaperCountSize(Project project);

    void createRegistrationPoint(
            Project project,
            LocationProvince locationProvince,
            LocationDistrict locationDistrict,
            LocationWard locationWard
    );

    void createRegistrationNumberBook(
            ProjectWard projectWard,
            RegistrationType registrationType,
            EPaperSize ePaperSize,
            String registrationDateCode,
            String numberBookCode,
            MultipartFile coverFile
    );

    void assignExtractFormToUser(Project project, Long totalCount, List<User> users);

}
