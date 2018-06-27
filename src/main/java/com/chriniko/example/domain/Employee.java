package com.chriniko.example.domain;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)

@javax.persistence.Entity
@Table(name = "employees")

@NamedQueries(

        value = {
                @NamedQuery(name = "Employee.findAll", query = "select emp from Employee emp"),
                @NamedQuery(name = "Employee.deleteAll", query = "delete Employee as emp")
        }
)
public class Employee extends Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private Long id;

    @Embedded
    private EmployeeName employeeName;

}
