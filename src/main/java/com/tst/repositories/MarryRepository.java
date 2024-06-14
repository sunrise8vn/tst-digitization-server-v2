package com.tst.repositories;

import com.tst.models.entities.Marry;
import com.tst.models.entities.ProjectWard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MarryRepository extends JpaRepository<Marry, Long> {

    @Query("SELECT COUNT (mr.id) " +
            "FROM Marry AS mr " +
            "WHERE mr.projectWard = :projectWard " +
            "AND mr.numberBook = :numberBook"
    )
    Long countAllByProjectWardAndNumberBook(ProjectWard projectWard, String numberBook);


    @Query("SELECT mr " +
            "FROM Marry AS mr " +
            "WHERE mr.projectWard = :projectWard " +
            "AND mr.numberBook = :numberBook"
    )
    List<Marry> findAllByProjectWardAndNumberBook(ProjectWard projectWard, String numberBook);
}
