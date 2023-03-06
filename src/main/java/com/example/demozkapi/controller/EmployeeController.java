package com.example.demozkapi.controller;

import com.example.demozkapi.entity.EmployeeDTO;
import com.example.demozkapi.entity.EmployeeSearch;
import com.example.demozkapi.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/create")
    public ResponseEntity createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.createEmployee(employeeDTO));
    }

    @PostMapping("/update")
    public ResponseEntity updateEmployee(@RequestParam Integer id, @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeDTO));
    }

    @GetMapping("/delete")
    public ResponseEntity deleteEmployee(@RequestParam Integer id) {
        return ResponseEntity.ok(employeeService.deleteEmployee(id));
    }

    @PostMapping("/search")
    public ResponseEntity searchEmployee(@RequestBody @Nullable EmployeeSearch body) {
        return ResponseEntity.ok(employeeService.searchEmployee(body));
    }
}