package com.testing.springboottesting.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testing.springboottesting.model.Employee;
import com.testing.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        employeeRepository.deleteAll();
    }

    @Test
    public void givenEmployee_whenCreateEmployee_thenSavedEmployee() throws Exception {
       Employee employee= Employee.builder()
                .firstName("Paresh")
                .lastName("Chaudhary")
                .email("paresh_dce@yahoo.com")
                .build();
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee))
        );
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())))
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    public void givenEmployee_whenGetEmployees_thenEmployeeList() throws Exception {
        List<Employee> list=new ArrayList<>();
        list.add(Employee.builder().firstName("Paresh")
                .lastName("Chaudhary")
                .email("paresh_dce@yahoo.com")
                .build());
        list.add(Employee.builder().firstName("ballu")
                .lastName("lalu")
                .email("lallu@yahoo.com")
                .build());
        employeeRepository.saveAll(list);

        ResultActions response = mockMvc.perform(get("/api/employees"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }





}
