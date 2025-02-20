package com.tst.repositories;

import com.tst.models.entities.Death;
import com.tst.models.entities.ProjectWard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeathRepository extends JpaRepository<Death, Long> {

    @Query("SELECT COUNT (de.id) " +
            "FROM Death AS de " +
            "WHERE de.projectWard = :projectWard " +
            "AND de.numberBook = :numberBook"
    )
    Long countAllByProjectWardAndNumberBook(ProjectWard projectWard, String numberBook);


    @Query("SELECT de " +
            "FROM Death AS de " +
            "WHERE de.projectWard = :projectWard " +
            "AND de.numberBook = :numberBook"
    )
    List<Death> findAllByProjectWardAndNumberBook(ProjectWard projectWard, String numberBook);

}
