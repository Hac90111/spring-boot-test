package com.testing.springboottesting.service.impl;

import com.testing.springboottesting.exception.ResourceNotFoundException;
import com.testing.springboottesting.model.Employee;
import com.testing.springboottesting.repository.EmployeeRepository;
import com.testing.springboottesting.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    //Constructor-based Injection, no need for @AutoWired
    private EmployeeRepository employeeRepository;


    // Only one constructor, no need for @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) throws ResourceNotFoundException {
        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (savedEmployee.isPresent()) {
            throw new ResourceNotFoundException("Employee already exist with given email: " + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees= employeeRepository.findAll();
        return employees;
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        Optional<Employee> employee= employeeRepository.findById(id);
        return employee;
    }

    @Override
    public Employee updateEmployee(Employee updatedEmployee) {
        return employeeRepository.save(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
