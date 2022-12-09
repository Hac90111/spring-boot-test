package com.testing.springboottesting.service.impl;

import com.testing.springboottesting.exception.ResourceNotFoundException;
import com.testing.springboottesting.model.Employee;
import com.testing.springboottesting.repository.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @Mock
    private EmployeeRepository repository;
    @InjectMocks // creates mock object and then inject the mock that is marked with @Mock(repository)
    private EmployeeServiceImpl service;

    private Employee employee;

    //inject repo and service before each test case
    @BeforeEach
    void setUp() {
        employee= Employee.builder()
                .id(1L)
                .firstName("Lalu")
                .lastName("Baluu")
                .email("lalu@gmail.com").build();
    }

    //Junit test for saveEmployee method
        @Test
        @DisplayName("JUnit-test for saveEmployee method")
        public void givenEmployee_whenSaveEmployee_thenEmployee(){
        //BDDMockito. given() -static import
            given(repository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
            given(repository.save(employee)).willReturn(employee);
                // System.out.println(repository);
                // System.out.println(service);
            //when- action
            Employee savedEmployee=service.saveEmployee(employee);
                //System.out.println(savedEmployee);
            //then- output expected
            assertThat(savedEmployee).isNotNull();
        }

    @Test
    @DisplayName("saveEmployee method throws Exception")
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException(){
        given(repository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));
         //when- preCondition
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class,()->service.saveEmployee(employee));
        //then- output expected
        verify(repository,never()).save(any(Employee.class));
    }

 //Junit test for
     @Test
     @DisplayName("test for getAllEmployees method- positive scenario")
     public void givenEmployeeList_whenGetALlEmployees_thenEmployeeList(){
         // given- precondition
         Employee employee1= Employee.builder()
                 .id(2L)
                 .firstName("Bhalu")
                 .lastName("nahi")
                 .email("nahi@gmail.com").build();
         given(repository.findAll()).willReturn(List.of(employee, employee1));
         //when- action
         List<Employee> employeeList= service.getAllEmployees();

         //then- output expected
         assertThat(employeeList).isNotNull();
         assertThat(employeeList.size()).isEqualTo(2);
     }

  //Junit test for getALlEmployees with negative scenario
      @Test
      @DisplayName("test for getAllEmployees- negative scenario")
      public void givenInvalidInput_whenGetAllEmployees_thenEmptyEmployeeList(){
          // given- precondition
          given(repository.findAll()).willReturn(Collections.emptyList());

          //when- action
          List<Employee> employeeList= service.getAllEmployees();

          //then- output expected
          assertThat(employeeList).isEmpty();
          assertThat(employeeList.size()).isEqualTo(0);
      }

   //Junit test for  findById method
       @Test
       @DisplayName("test for getEmployeeById method")
       public void givenEmployeeId_whenGetEmployeeById_thenEmployee(){
           // given- precondition
         given(repository.findById(employee.getId())).willReturn(Optional.of(employee));
           //when- action
           Optional<Employee> savedEmployee= service.getEmployeeById(employee.getId());
           //then- output expected
           //Assertions.assertThat()- static import from org.assertj.core.api.Assertions.*;
           assertThat(savedEmployee).isNotNull();
           assertThat(savedEmployee.get().getId()).isNotEqualTo(0);
           assertThat(savedEmployee.get()).isEqualTo(employee);
       }
    //Junit test for updateEmployee method
        @Test
        @DisplayName("test for updateEmployee method")
        public void givenEmployee_whenUpdateEmployee_thenUpdatedEmployee(){
            // given- precondition
            given(repository.save(employee)).willReturn(employee);
            employee.setFirstName("Himan");
            employee.setEmail("h@gmail.com");
            //when- action
           Employee updatedEmployee= service.updateEmployee(employee);
            //then- output expected
            assertThat(employee.getFirstName()).isEqualTo(updatedEmployee.getFirstName());
            assertThat(employee.getEmail()).isEqualTo(updatedEmployee.getEmail());
        }
     //Junit test for deleteEmployee method
         @Test
         @DisplayName("test for deleteEmployee  method")
         public void givenemployeeId_whenDeleteById_thenNothing(){
             // given- precondition
             Long employeeId = employee.getId();
             willDoNothing().given(repository).deleteById(employeeId);
             //when- action
             service.deleteEmployee(employeeId);
             //then- output expected
             verify(repository, times(1)).deleteById(employeeId);
         }
}