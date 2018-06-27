package com.chriniko.example.service.first_case;

import com.chriniko.example.domain.Event;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class BService {

    @PersistenceContext
    private EntityManager em;

    @Transactional(propagation = Propagation.REQUIRED,
            rollbackFor = {BException.class})
    public void second() {

        System.out.println("Inside BService#second method...");

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ignored) {
        }

        em.persist(new Event(UUID.randomUUID().toString(),
                "BService#second --- commit success!"));

        throw new BException("ooops");

    }

}
