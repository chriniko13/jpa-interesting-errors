package com.chriniko.example;

import com.chriniko.example.config.AppConfiguration;
import com.chriniko.example.domain.Employee;
import com.chriniko.example.domain.EmployeeName;
import com.chriniko.example.service.EmployeeService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.stream.IntStream;

public class Application {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext(AppConfiguration.class);

        //Note: add your examples here if needed....
        basicExample(context);

        context.close();
    }

    private static void basicExample(AnnotationConfigApplicationContext context) {
        EmployeeService employeeService = context.getBean(EmployeeService.class);

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

        List<Employee> employees = employeeService.getAll();
        employees.forEach(System.out::println);


        Employee employee = employeeService.find(1L);
        employee.getEmployeeName().setFirstname(employee.getEmployeeName().getFirstname() + "__updated");
        employeeService.update(employee);

        System.out.println("update_result>> " + employeeService.find(1L));
    }
}
