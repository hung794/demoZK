package com.example.demozkapi.service;

import com.example.demozkapi.entity.Employee;
import com.example.demozkapi.entity.EmployeeDTO;
import com.example.demozkapi.entity.EmployeeSearch;
import com.example.demozkapi.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepo employeeRepo;

    @PersistenceContext
    private EntityManager entityManager;

    public Employee createEmployee(EmployeeDTO dto) {
        Employee employee = Employee.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .salary(dto.getSalary())
                .build();
        return employeeRepo.save(employee);
    }

    public Employee updateEmployee(Integer id, EmployeeDTO dto) {
        Employee employee = employeeRepo.findById(id).orElse(null);
        if (employee != null) {
            employee.setId(id);
            employee.setName(dto.getName());
            employee.setAddress(dto.getAddress());
            employee.setSalary(dto.getSalary());
            return employeeRepo.save(employee);
        } else return null;
    }

    public String deleteEmployee(Integer id) {
        if (id == null) {
            return "Employee not found!";
        }
        else {
            employeeRepo.deleteById(id);
            return "Delete employee success!";
        }
    }

    public List<Employee> searchEmployee(EmployeeSearch body) {
        StringBuilder sql = new StringBuilder("FROM Employee e WHERE 1 = 1");
        Map<String, Object> parameters = new HashMap<>();
        if(body != null) {
            if(body.getName() != null && !body.getName().isEmpty()) {
                sql.append(" AND e.name LIKE :name");
                parameters.put("name", "%" + body.getName() + "%");
            }
            if(body.getAddress() != null && !body.getAddress().isEmpty()){
                sql.append(" AND e.address LIKE :address");
                parameters.put("address", "%" + body.getAddress() + "%");
            }
            if(body.getStartSalary() != null){
                sql.append(" AND e.salary >= :startSalary");
                parameters.put("startSalary", body.getStartSalary());
            }
            if(body.getEndSalary() != null){
                sql.append(" AND e.salary <= :endSalary");
                parameters.put("endSalary", body.getEndSalary());
            }
        }
        Query query = entityManager.createQuery(sql.toString());
        if (parameters.size() > 0) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return query.getResultList();
    }
}
