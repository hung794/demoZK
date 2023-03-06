package org.example.entity;

public class EmployeeSearch {
    private String name;
    private String address;
    private Double startSalary;
    private Double endSalary;

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

    public Double getStartSalary() {
        return startSalary;
    }

    public void setStartSalary(Double startSalary) {
        this.startSalary = startSalary;
    }

    public Double getEndSalary() {
        return endSalary;
    }

    public void setEndSalary(Double endSalary) {
        this.endSalary = endSalary;
    }
}