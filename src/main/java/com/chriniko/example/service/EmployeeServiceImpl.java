package com.chriniko.example.service;

import com.chriniko.example.dap.EmployeeDap;
import com.chriniko.example.domain.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeDap employeeDap;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void add(Employee employee) {
        employeeDap.add(employee);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void delete(Long id) {
        employeeDap.delete(id);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void delete(Employee employee) {
        employeeDap.delete(employee);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Employee update(Employee employee) {
        return employeeDap.update(employee);
    }

    @Transactional(readOnly = true,
            propagation = Propagation.REQUIRES_NEW)
    @Override
    public Employee find(Long id) {
        return employeeDap.find(id);
    }

    @Transactional(readOnly = true,
            propagation = Propagation.REQUIRES_NEW)
    @Override
    public List<Employee> getAll() {
        return Optional.ofNullable(employeeDap.findAll())
                .map(ArrayList::new)
                .orElseGet(ArrayList::new);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void removeAll() {
        employeeDap.deleteAll();
    }
}
