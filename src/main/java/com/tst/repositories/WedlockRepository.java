package com.tst.repositories;

import com.tst.models.entities.ProjectWard;
import com.tst.models.entities.Wedlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WedlockRepository extends JpaRepository<Wedlock, Long> {

    @Query("SELECT COUNT (wl.id) " +
            "FROM Wedlock AS wl " +
            "WHERE wl.projectWard = :projectWard " +
            "AND wl.numberBook = :numberBook"
    )
    Long countAllByProjectWardAndNumberBook(ProjectWard projectWard, String numberBook);


    @Query("SELECT wl " +
            "FROM Wedlock AS wl " +
            "WHERE wl.projectWard = :projectWard " +
            "AND wl.numberBook = :numberBook"
    )
    List<Wedlock> findAllByProjectWardAndNumberBook(ProjectWard projectWard, String numberBook);

}
