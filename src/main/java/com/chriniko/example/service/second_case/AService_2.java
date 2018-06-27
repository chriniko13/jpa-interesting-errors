package com.chriniko.example.service.second_case;

import com.chriniko.example.domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
public class AService_2 {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private BService_2 bService2;

    @Transactional(propagation = Propagation.REQUIRED)
    public void init() {
        entityManager.createQuery("delete Event as ev").executeUpdate();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<Event> first() {

        bService2.second();

        TypedQuery<Event> tQ = entityManager.createQuery("select ev from Event ev", Event.class);
        return tQ.getResultList();

    }

}
