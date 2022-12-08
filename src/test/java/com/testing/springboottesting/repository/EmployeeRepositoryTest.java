package com.testing.springboottesting.repository;

import com.testing.springboottesting.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmployeeRepositoryTest {

    private Employee employee;

    //Given condition
    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .firstName("Bhuro")
                .lastName("patel")
                .email("Bhuro@gmail.com")
                .build();
    }

    @Autowired
    private EmployeeRepository employeeRepository;

    //JUnit test case  for save employee operation
    @Test
    @DisplayName("JUnit test case  for save employee operation")
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {

        //when- action or behaviour we'll test
        Employee savedEmployee = employeeRepository.save(employee);

        //then- verify the test
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    //Junit test for findALl method
    @Test
    public void givenEmployees_whenFindAll_thenEmployeeList() {
        employeeRepository.save(employee);
        //when- action
        List<Employee> employees = employeeRepository.findAll();
        //then- output expected
        assertThat(employees).isNotNull();
        assertThat(employees).hasSize(1).contains(employee);
    }

    //Junit test for getById method
    @Test
    public void givenEmployees_whenFindById_thenEmployeeWithSameId() {
        employeeRepository.save(employee);
        //when- action
        Optional<Employee> byId1 = employeeRepository.findById(employee.getId());
        //then- output expected
        assertThat(employee).isEqualTo(byId1.get());
    }

    //Junit test for findById method
    @Test
    public void givenEmplyee_whenFIndByEmail_thenEmployee() {
        employeeRepository.save(employee);
        // when- action
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(employee.getEmail());
        //then- output expected
        assertThat(optionalEmployee).isNotNull();
        assertThat(optionalEmployee).contains(employee);
    }

    //Junit test for updateEmployee method
    @Test
    public void givenEmployee_whenUpdateEmployee_thenUpdatedEmployee() {
        employeeRepository.save(employee);
        //when- action
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("bhuro@gmail.com");
        savedEmployee.setFirstName("Bhuro");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        //then- output expected
        assertThat(updatedEmployee.getEmail()).isEqualTo("bhuro@gmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Bhuro");
    }

    //Junit test for deleteEmployee method
    @Test
    public void givenEmployee_whenDelete_thenRemoveEmployee() {
        employeeRepository.save(employee);
        //when- action
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> optionalEmployee = employeeRepository.findById(employee.getId());
        //then- output expected
        assertThat(optionalEmployee).isEmpty();
    }

    //Junit test for findByJPQL method, custom query using JPQL with index
    @Test
    public void givenFirstNameAndLastName_whenFindByJpql_thenEmployee() {
        employeeRepository.save(employee);
        //when- action
        Employee savedEmployee = employeeRepository.findByJPQL(employee.getFirstName(), employee.getLastName());
        //then- output expected
        assertThat(savedEmployee).isNotNull();
        assertThat(employee.getClass()).isEqualTo(savedEmployee.getClass());
    }

    //Junit test for findByJPQLNamedParams method, custom query using JPQL with namedParams
    @Test
    public void givenFirstNameAndLastName_whenFindByJpqlNamedParams_thenEmployee() {
        employeeRepository.save(employee);
        //when- action
        Employee savedEmployee = employeeRepository.findByJPQLNamedParams(employee.getFirstName(), employee.getLastName());
        //then- output expected
        assertThat(savedEmployee).isNotNull();
        assertThat(employee.getClass()).isEqualTo(savedEmployee.getClass());
    }

    //Junit test for findByNativeSQl using index params
    @Test
    public void givenFirstNameLastName_whenFindByNativeSQL_thenEmployee() {
        employeeRepository.save(employee);
        //when- action
        Employee byNativeSQL = employeeRepository.findByNativeSQL(employee.getFirstName(), employee.getLastName());
        //then- output expected
        assertThat(byNativeSQL).isNotNull();
        assertThat(employee.getClass()).isEqualTo(byNativeSQL.getClass());
    }

    //Junit test for findByNativeQueryNamedParams , native query with named params
    @Test
    public void givenFirstNameLastName_whenFindByNativeQueryNamedParams_thenEmployee() {

        employeeRepository.save(employee);
        //when- action
        Employee byNativeSQL = employeeRepository.findByNativeQueryNamedParams(employee.getFirstName(), employee.getLastName());
        //then- output expected
        assertThat(byNativeSQL).isNotNull();
        assertThat(employee.getClass()).isEqualTo(byNativeSQL.getClass());
    }

}