package com.tst.repositories;

import com.tst.models.entities.ParentsChildren;
import com.tst.models.entities.ProjectWard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ParentsChildrenRepository extends JpaRepository<ParentsChildren, Long> {

    @Query("SELECT COUNT (pc.id) " +
            "FROM ParentsChildren AS pc " +
            "WHERE pc.projectWard = :projectWard " +
            "AND pc.numberBook = :numberBook"
    )
    Long countAllByProjectWardAndNumberBook(ProjectWard projectWard, String numberBook);


    @Query("SELECT pc " +
            "FROM ParentsChildren AS pc " +
            "WHERE pc.projectWard = :projectWard " +
            "AND pc.numberBook = :numberBook"
    )
    List<ParentsChildren> findAllByProjectWardAndNumberBook(ProjectWard projectWard, String numberBook);

}
