package com.chriniko.example.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode

@Embeddable
public class EmployeeName {

    @Column(name = "first_name")
    private String firstname;

    @Column(name = "initials")
    private String initials;

    @Column(name = "surname")
    private String surname;

}
