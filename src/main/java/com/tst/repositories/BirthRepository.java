package com.tst.repositories;

import com.tst.models.entities.Birth;
import com.tst.models.entities.ProjectWard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BirthRepository extends JpaRepository<Birth, Long> {

    @Query("SELECT COUNT (bi.id) " +
            "FROM Birth AS bi " +
            "WHERE bi.projectWard = :projectWard " +
            "AND bi.numberBook = :numberBook"
    )
    Long countAllByProjectWardAndNumberBook(ProjectWard projectWard, String numberBook);


    @Query("SELECT bi " +
            "FROM Birth AS bi " +
            "WHERE bi.projectWard = :projectWard " +
            "AND bi.numberBook = :numberBook"
    )
    List<Birth> findAllByProjectWardAndNumberBook(ProjectWard projectWard, String numberBook);
}
