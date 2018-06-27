package com.chriniko.example.service;


import com.chriniko.example.domain.Employee;

import java.util.List;

public interface EmployeeService {

    void add(Employee employee);

    void delete(Long id);

    void delete(Employee employee);

    Employee update(Employee employee);

    Employee find(Long id);

    List<Employee> getAll();

    void removeAll();
}
