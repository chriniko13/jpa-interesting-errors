package com.chriniko.example.service;

import com.chriniko.example.domain.Employee;
import com.chriniko.example.domain.EmployeeName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {com.chriniko.example.config.AppConfiguration.class}
)
public class EmployeeServiceImplTest {

    @Autowired
    private EmployeeService employeeService;

    @Test
    public void add() {

        //given
        employeeService.removeAll();

        Employee employee = Employee.builder()
                .employeeName(EmployeeName
                        .builder()
                        .firstname("firstname")
                        .initials("initials")
                        .surname("surname")
                        .build())
                .build();

        //when
        employeeService.add(employee);


        //then
        List<Employee> employees = employeeService.getAll();
        assertEquals(1, employees.size());
        assertEquals(new Long(1), employees.get(0).getId());

    }

    @Test
    public void delete_long() {

        //given
        employeeService.removeAll();

        Employee employee = Employee.builder()
                .employeeName(EmployeeName
                        .builder()
                        .firstname("firstname")
                        .initials("initials")
                        .surname("surname")
                        .build())
                .build();

        employeeService.add(employee);

        //when
        employeeService.delete(1L);

        //then
        assertEquals(0, employeeService.getAll().size());

    }

    @Test
    public void delete_entity() {

        //given
        employeeService.removeAll();

        Employee employee = Employee.builder()
                .employeeName(EmployeeName
                        .builder()
                        .firstname("firstname")
                        .initials("initials")
                        .surname("surname")
                        .build())
                .build();

        employeeService.add(employee);

        //when
        employeeService.delete(employee);

        //then
        assertEquals(0, employeeService.getAll().size());
    }

    @Test
    public void update() {

        //given
        employeeService.removeAll();

        Employee employee = Employee.builder()
                .employeeName(EmployeeName
                        .builder()
                        .firstname("firstname")
                        .initials("initials")
                        .surname("surname")
                        .build())
                .build();

        employeeService.add(employee);

        //when
        EmployeeName employeeName = employee.getEmployeeName();
        employeeName.setFirstname("updated-firstname");
        employee.setEmployeeName(employeeName);
        employeeService.update(employee);

        //then
        assertEquals("updated-firstname",
                employeeService.find(1L).getEmployeeName().getFirstname());
    }

    @Test
    public void find_id() {

        //given
        employeeService.removeAll();

        Employee employee = Employee.builder()
                .employeeName(EmployeeName
                        .builder()
                        .firstname("firstname")
                        .initials("initials")
                        .surname("surname")
                        .build())
                .build();

        employeeService.add(employee);

        //when
        Employee record = employeeService.find(1L);

        //then
        boolean sameEmpname = record.getEmployeeName().equals(employee.getEmployeeName());
        assertTrue(sameEmpname);
    }

    @Test
    public void getAll() {

        //given
        employeeService.removeAll();

        IntStream.rangeClosed(1, 20)
                .forEach(idx -> {

                    employeeService.add(Employee
                            .builder()
                            .employeeName(EmployeeName.builder()
                                    .firstname("Firstname " + idx)
                                    .initials("Initials " + idx)
                                    .surname("Surname " + idx)
                                    .build())
                            .build()
                    );
                });

        //when
        List<Employee> records = employeeService.getAll();

        //then
        assertEquals(20, records.size());
    }

    @Test
    public void removeAll() {

        //given
        employeeService.removeAll();

        IntStream.rangeClosed(1, 20)
                .forEach(idx -> {

                    employeeService.add(Employee
                            .builder()
                            .employeeName(EmployeeName.builder()
                                    .firstname("Firstname " + idx)
                                    .initials("Initials " + idx)
                                    .surname("Surname " + idx)
                                    .build())
                            .build()
                    );
                });
        List<Employee> records = employeeService.getAll();
        assertEquals(20, records.size());

        //when
        employeeService.removeAll();


        //then
        assertEquals(0, employeeService.getAll().size());
    }
}