package com.chriniko.example.service.second_case;

import com.chriniko.example.domain.Event;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

@Service
public class BService_2 {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void second(){

        entityManager.persist(new Event(UUID.randomUUID().toString(),
                "i have been created from BService#second method."));
    }

}
