package com.testing.springboottesting.service;

import com.testing.springboottesting.exception.ResourceNotFoundException;
import com.testing.springboottesting.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee saveEmployee(Employee employee) throws ResourceNotFoundException;
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(Long id);
    Employee updateEmployee(Employee updatedEmployee);
    void deleteEmployee(Long id);
}
