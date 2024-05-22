package com.tst.repositories;

import com.tst.models.entities.*;
import com.tst.models.responses.locationRegion.LocationResponse;
import com.tst.models.responses.project.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT NEW com.tst.models.responses.project.ProjectResponse (" +
                "prj.id, " +
                "prj.name, " +
                "prj.description" +
            ") " +
            "FROM Project AS prj " +
            "JOIN ProjectUser AS pu " +
            "ON pu.project = prj " +
            "AND pu.user = :user " +
            "AND prj = :project"
    )
    Optional<ProjectResponse> findProjectResponseByProjectAndUser(Project project, User user);


    @Query("SELECT NEW com.tst.models.responses.project.RegistrationPointResponse (" +
                "pw.id, " +
                "prj.name, " +
                "pp.name, " +
                "pd.name, " +
                "pw.name" +
            ") " +
            "FROM ProjectWard AS pw " +
            "JOIN ProjectDistrict AS pd " +
            "ON pw.projectDistrict = pd " +
            "JOIN ProjectProvince AS pp " +
            "ON pd.projectProvince = pp " +
            "JOIN Project AS prj " +
            "ON pp.project = prj " +
            "JOIN ProjectUser AS pu " +
            "ON pu.project = prj " +
            "WHERE prj = :project " +
            "AND pu.user = :user"
    )
    List<RegistrationPointResponse> findAllRegistrationPointByProjectAndUser(Project project, User user);


    @Query("SELECT DISTINCT NEW com.tst.models.responses.locationRegion.LocationResponse (" +
                "pp.id, " +
                "pp.name, " +
                "pp.code " +
            ") " +
            "FROM ProjectProvince AS pp " +
            "JOIN ProjectUser AS pu " +
            "ON pp.project = pu.project " +
            "AND pu.user = :user " +
            "AND pp.project = :project"
    )
    List<LocationResponse> findAllProvincesByProjectAndUser(Project project, User user);


    @Query("SELECT DISTINCT NEW com.tst.models.responses.locationRegion.LocationResponse (" +
                "pd.id, " +
                "pd.name, " +
                "pd.code " +
            ") " +
            "FROM ProjectDistrict AS pd " +
            "JOIN ProjectProvince AS pp " +
            "ON pd.projectProvince = :projectProvince " +
            "AND pd.project = :project " +
            "JOIN ProjectUser AS pu " +
            "ON pp.project = pu.project " +
            "AND pu.user = :user "
    )
    List<LocationResponse> findAllDistrictsByProjectAndProvinceAndUser(
            Project project,
            ProjectProvince projectProvince,
            User user
    );


    @Query("SELECT DISTINCT NEW com.tst.models.responses.locationRegion.LocationResponse (" +
                "pw.id, " +
                "pw.name, " +
                "pw.code " +
            ") " +
            "FROM ProjectWard AS pw " +
            "JOIN ProjectDistrict AS pd " +
            "ON pw.projectDistrict = :projectDistrict " +
            "AND pw.project = :project " +
            "JOIN ProjectUser AS pu " +
            "ON pd.project = pu.project " +
            "AND pu.user = :user "
    )
    List<LocationResponse> findAllWardsByProjectAndDistrictAndUser(
            Project project,
            ProjectDistrict projectDistrict,
            User user
    );


    @Query("SELECT DISTINCT NEW com.tst.models.responses.project.NumberBookNewResponse (" +
                "pnb.id, " +
                "pd.name, " +
                "pw.name, " +
                "prt.code, " +
                "pps.code, " +
                "prd.code, " +
                "pnb.code, " +
                "pnb.status" +
            ") " +
            "FROM ProjectNumberBook AS pnb " +
            "JOIN ProjectRegistrationDate AS prd " +
            "ON pnb.projectRegistrationDate = prd " +
            "AND pnb.status = 'NEW' " +
            "JOIN ProjectPaperSize AS pps " +
            "ON prd.projectPaperSize = pps " +
            "JOIN ProjectRegistrationType AS prt " +
            "ON pps.projectRegistrationType = prt " +
            "JOIN ProjectWard AS pw " +
            "ON prt.projectWard = pw " +
            "AND pw.project = :project " +
            "JOIN ProjectDistrict AS pd " +
            "ON pw.projectDistrict = pd " +
            "JOIN ProjectProvince AS pp " +
            "ON pd.projectProvince = pp " +
            "AND pp = :projectProvince"
    )
    List<NumberBookNewResponse> findAllNewNumberBooksByProjectAndProjectProvince(
            Project project,
            ProjectProvince projectProvince
    );


    @Query("SELECT DISTINCT NEW com.tst.models.responses.project.NumberBookNewResponse (" +
                "pnb.id, " +
                "pd.name, " +
                "pw.name, " +
                "prt.code, " +
                "pps.code, " +
                "prd.code, " +
                "pnb.code, " +
                "pnb.status" +
            ") " +
            "FROM ProjectNumberBook AS pnb " +
            "JOIN ProjectRegistrationDate AS prd " +
            "ON pnb.projectRegistrationDate = prd " +
            "AND pnb.status = 'NEW' " +
            "JOIN ProjectPaperSize AS pps " +
            "ON prd.projectPaperSize = pps " +
            "JOIN ProjectRegistrationType AS prt " +
            "ON pps.projectRegistrationType = prt " +
            "JOIN ProjectWard AS pw " +
            "ON prt.projectWard = pw " +
            "AND pw.project = :project " +
            "JOIN ProjectDistrict AS pd " +
            "ON pw.projectDistrict = pd " +
            "AND pd = :projectDistrict"
    )
    List<NumberBookNewResponse> findAllNewNumberBooksByProjectAndProjectDistrict(
            Project project,
            ProjectDistrict projectDistrict
    );


    @Query("SELECT DISTINCT NEW com.tst.models.responses.project.NumberBookNewResponse (" +
                "pnb.id, " +
                "pd.name, " +
                "pw.name, " +
                "prt.code, " +
                "pps.code, " +
                "prd.code, " +
                "pnb.code, " +
                "pnb.status" +
            ") " +
            "FROM ProjectNumberBook AS pnb " +
            "JOIN ProjectRegistrationDate AS prd " +
            "ON pnb.projectRegistrationDate = prd " +
            "AND pnb.status = 'ACCEPT' " +
            "JOIN ProjectPaperSize AS pps " +
            "ON prd.projectPaperSize = pps " +
            "JOIN ProjectRegistrationType AS prt " +
            "ON pps.projectRegistrationType = prt " +
            "JOIN ProjectWard AS pw " +
            "ON prt.projectWard = pw " +
            "AND pw.project = :project " +
            "AND pw = :projectWard " +
            "JOIN ProjectDistrict AS pd " +
            "ON pw.projectDistrict = pd "
    )
    List<NumberBookNewResponse> findAllNewNumberBooksByProjectAndProjectWard(
            Project project,
            ProjectWard projectWard
    );


    @Query("SELECT DISTINCT NEW com.tst.models.responses.project.NumberBookApprovedResponse (" +
                "pnb.id, " +
                "pd.name, " +
                "pw.name, " +
                "prt.code, " +
                "pps.code, " +
                "prd.code, " +
                "pnb.code, " +
                "pnb.status" +
            ") " +
            "FROM ProjectNumberBook AS pnb " +
            "JOIN ProjectRegistrationDate AS prd " +
            "ON pnb.projectRegistrationDate = prd " +
            "AND pnb.status = 'ACCEPT' " +
            "JOIN ProjectPaperSize AS pps " +
            "ON prd.projectPaperSize = pps " +
            "JOIN ProjectRegistrationType AS prt " +
            "ON pps.projectRegistrationType = prt " +
            "JOIN ProjectWard AS pw " +
            "ON prt.projectWard = pw " +
            "AND pw.project = :project " +
            "JOIN ProjectDistrict AS pd " +
            "ON pw.projectDistrict = pd " +
            "JOIN ProjectProvince AS pp " +
            "ON pd.projectProvince = pp " +
            "AND pp = :projectProvince"
    )
    List<NumberBookApprovedResponse> findAllApprovedNumberBooksByProjectAndProjectProvince(
            Project project,
            ProjectProvince projectProvince
    );


    @Query("SELECT DISTINCT NEW com.tst.models.responses.project.NumberBookApprovedResponse (" +
                "pnb.id, " +
                "pd.name, " +
                "pw.name, " +
                "prt.code, " +
                "pps.code, " +
                "prd.code, " +
                "pnb.code, " +
                "pnb.status" +
            ") " +
            "FROM ProjectNumberBook AS pnb " +
            "JOIN ProjectRegistrationDate AS prd " +
            "ON pnb.projectRegistrationDate = prd " +
            "AND pnb.status = 'ACCEPT' " +
            "JOIN ProjectPaperSize AS pps " +
            "ON prd.projectPaperSize = pps " +
            "JOIN ProjectRegistrationType AS prt " +
            "ON pps.projectRegistrationType = prt " +
            "JOIN ProjectWard AS pw " +
            "ON prt.projectWard = pw " +
            "AND pw.project = :project " +
            "JOIN ProjectDistrict AS pd " +
            "ON pw.projectDistrict = pd " +
            "AND pd = :projectDistrict"
    )
    List<NumberBookApprovedResponse> findAllApprovedNumberBooksByProjectAndProjectDistrict(
            Project project,
            ProjectDistrict projectDistrict
    );


    @Query("SELECT DISTINCT NEW com.tst.models.responses.project.NumberBookApprovedResponse (" +
                "pnb.id, " +
                "pd.name, " +
                "pw.name, " +
                "prt.code, " +
                "pps.code, " +
                "prd.code, " +
                "pnb.code, " +
                "pnb.status" +
            ") " +
            "FROM ProjectNumberBook AS pnb " +
            "JOIN ProjectRegistrationDate AS prd " +
            "ON pnb.projectRegistrationDate = prd " +
            "AND pnb.status = 'ACCEPT' " +
            "JOIN ProjectPaperSize AS pps " +
            "ON prd.projectPaperSize = pps " +
            "JOIN ProjectRegistrationType AS prt " +
            "ON pps.projectRegistrationType = prt " +
            "JOIN ProjectWard AS pw " +
            "ON prt.projectWard = pw " +
            "AND pw.project = :project " +
            "AND pw = :projectWard " +
            "JOIN ProjectDistrict AS pd " +
            "ON pw.projectDistrict = pd "
    )
    List<NumberBookApprovedResponse> findAllApprovedNumberBooksByProjectAndProjectWard(
            Project project,
            ProjectWard projectWard
    );

}
