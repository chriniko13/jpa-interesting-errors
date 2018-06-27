package com.chriniko.example.service.fourth_case;

import com.chriniko.example.domain.Event;
import com.chriniko.example.interceptor.EntityLocker;
import com.chriniko.example.interceptor.EntityLockerType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

@Service
public class AService_3 {

    @PersistenceContext
    private EntityManager entityManager;

    @EntityLocker(lockName = "Employees", lockerType = EntityLockerType.WRITE)
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    public void insertEventIfNotExists_ReadUncommitted(String message) {
        insertIfNotExistsInternal(message);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void insertEventIfNotExists_ReadCommitted(String message) {
        insertIfNotExistsInternal(message);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public void insertEventIfNotExists_Serializable(String message) {
        insertIfNotExistsInternal(message);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void insertEventIfNotExists_RepeatableRead(String message) {
        insertIfNotExistsInternal(message);
    }

    private void insertIfNotExistsInternal(String message) {
        List<Event> results = entityManager
                .createNamedQuery(Event.FIND_BY_MESSAGE, Event.class)
                .setParameter("m", message)
                .getResultList();

        if (results.isEmpty()) {
            entityManager.persist(new Event(UUID.randomUUID().toString(), message));
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Event> findAll() {
        return entityManager.createNamedQuery(Event.FIND_ALL, Event.class).getResultList();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAll() {
        entityManager.createNamedQuery(Event.DELETE_ALL).executeUpdate();
    }

}
