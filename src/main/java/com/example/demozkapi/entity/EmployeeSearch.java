package com.example.demozkapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSearch {
    private String name;
    private String address;
    private Double startSalary;
    private Double endSalary;
}
