package com.tst.repositories;

import com.tst.models.entities.Birth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BirthRepository extends JpaRepository<Birth, Long> {
}
