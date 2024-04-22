package com.tst.repositories;

import com.tst.models.entities.Death;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeathRepository extends JpaRepository<Death, Long> {
}
