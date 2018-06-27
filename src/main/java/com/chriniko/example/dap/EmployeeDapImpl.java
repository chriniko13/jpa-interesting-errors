package com.chriniko.example.dap;

import com.chriniko.example.domain.Employee;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.Collection;

@Repository
public class EmployeeDapImpl implements EmployeeDap {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void add(Employee employee) {
        em.persist(employee);
    }

    @Override
    public void delete(Long id) {
        Employee employee = em.find(Employee.class,
                id,
                LockModeType.OPTIMISTIC);
        em.remove(employee);
    }

    @Override
    public void delete(Employee employee) {
        em.remove(em.merge(employee));
    }

    @Override
    public void deleteAll() {
        Query q = em.createNamedQuery("Employee.deleteAll");
        q.executeUpdate();

        this.resetAutoIncrement();
    }

    @Override
    public Employee update(Employee employee) {
        return em.merge(employee);
    }

    @Override
    public Employee find(Long id) {
        return em.find(Employee.class,
                id,
                LockModeType.OPTIMISTIC);
    }

    @Override
    public Collection<Employee> findAll() {
        TypedQuery<Employee> q = em.createNamedQuery("Employee.findAll", Employee.class);
        return q.getResultList();
    }

    private void resetAutoIncrement() {
        Query query = em.createNativeQuery("ALTER TABLE test_jpa_example.employees AUTO_INCREMENT = 1");
        query.executeUpdate();
    }
}
