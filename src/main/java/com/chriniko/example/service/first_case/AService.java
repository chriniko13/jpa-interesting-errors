package com.chriniko.example.service.first_case;

import com.chriniko.example.domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class AService {

    @Autowired
    private BService bService;

    @PersistenceContext
    private EntityManager em;

    @Transactional(propagation = Propagation.REQUIRED,
            noRollbackFor = {BException.class})
    public void first() {

        System.out.println("Inside AService#first method...");

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ignored) {
        }

        bService.second();

        em.persist(new Event(UUID.randomUUID().toString(),
                "AService#first --- commit success!"));

    }

}
