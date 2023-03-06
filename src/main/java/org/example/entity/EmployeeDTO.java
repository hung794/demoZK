package org.example.entity;

public class EmployeeDTO {
    private String name;
    private String address;
    private Double salary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public EmployeeDTO(String name, String address, Double salary) {
        this.name = name;
        this.address = address;
        this.salary = salary;
    }
}
