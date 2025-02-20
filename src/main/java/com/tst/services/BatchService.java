package com.tst.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BatchService {

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public void batchCreate(List<?> entities) {
        final int batchSize = 500;

        for (int i = 0; i < entities.size(); i++) {
            entityManager.persist(entities.get(i));

            if (i % batchSize == 0 && i > 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }

        entityManager.flush();
        entityManager.clear();
    }

    @Transactional
    public void batchUpdate(List<?> entities) {
        final int batchSize = 500;

        for (int i = 0; i < entities.size(); i++) {
            entityManager.merge(entities.get(i));

            if (i % batchSize == 0 && i > 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }

        entityManager.flush();
        entityManager.clear();
    }
}
