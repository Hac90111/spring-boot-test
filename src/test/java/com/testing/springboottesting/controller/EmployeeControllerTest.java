package com.testing.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testing.springboottesting.model.Employee;
import com.testing.springboottesting.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class EmployeeControllerTest {
    private Employee employee;
    private Employee employee1;


    @Autowired
    private MockMvc mockMvc;

    @MockBean  // tell spring to create mock object of EmployeeService
    private EmployeeService service;

    @Autowired
    private ObjectMapper objectMapper;  // to serialize and deserialize the objects

    @BeforeEach
    void setUp() {

         employee= Employee.builder()
                 .id(1L)
                .firstName("Paresh")
                .lastName("Chaudhary")
                .email("paresh_dce@yahoo.com")
                .build();

        employee1= Employee.builder()
                .id(2L)
                .firstName("Rahul")
                .lastName("Chaudhary")
                .email("rahul@yahoo.com")
                .build();
    }
    @Test
   public void givenEmployee_whenCreateEmployee_thenSavedEmployee() throws Exception {
        //given- precondition for setUp
        //ArgumentMatchers.any()--static import
        given(service.saveEmployee(any(Employee.class))).willAnswer(invocation->invocation.getArgument(0));

        //when-action or behaviour we are testing
        //MockMvcRequestBuilders.post-static import
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee))
        );
        //then-outcome expected
        //MediaType.APPLICATION_JSON - static import
        //MockMvcResultMatchers. status(), MockMvcResultMatchers.content()- static import
        //CoreMatchers.is()- static import
        response.andDo(print())     // to print the result(optional)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())))
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    public void givenEmployee_whenGetEmployees_thenEmployeeList() throws Exception {
        given(service.getAllEmployees()).willReturn(List.of(employee1, employee));

        ResultActions response = mockMvc.perform(get("/api/employees"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }

    //positive scenario
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenEmployee() throws Exception{
        given(service.getEmployeeById(employee.getId())).willReturn(Optional.of(employee));

        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    //negative scenario
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenEmptyEmployee() throws Exception{
        given(service.getEmployeeById(employee.getId())).willReturn(Optional.empty());

        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    public void givenEmployee_whenUpdateEmployee_thenUpdatedEmployee () throws Exception {
        given(service.getEmployeeById(employee.getId())).willReturn(Optional.of(employee));
        given(service.updateEmployee(any(Employee.class))).willAnswer(invocation->invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employee.getId())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee1)));

        response.andExpect(status().isOk())
                .andDo(print());
//                .andExpect(jsonPath("$.firstName", is("Rahul")));

    }

    //Negative scenario
    @Test
    public void givenEmployee_whenUpdateEmployee_then404 () throws Exception {
        given(service.getEmployeeById(employee.getId())).willReturn(Optional.empty());
        given(service.updateEmployee(any(Employee.class))).willAnswer(invocation->invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employee.getId())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee1)));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }

     //Junit test for
         @Test
         public void givenEmployeeId_whenDeleteEmplyee_then200() throws Exception{
             // given- precondition
             willDoNothing().given(service).deleteEmployee(employee.getId());

             //when- action
             ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employee.getId()));
             //then- output expected

             response.andDo(print())
                     .andExpect(status().isOk());
         }
}
