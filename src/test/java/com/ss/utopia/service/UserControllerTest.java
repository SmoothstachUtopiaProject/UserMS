package com.ss.utopia.service;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.utopia.controller.UserController;
import com.ss.utopia.models.Role;
import com.ss.utopia.models.User;
import com.ss.utopia.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = UserController.class)
@ActiveProfiles("User Controller Test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    List<User> usersList = new ArrayList();

    private User user = new User();

    @BeforeEach
    void setUp() {
        user = new User(1, Role.USER, "Junit", "Test", "junit@gmail.com", "PasswordUnit", "776565443");
        User user1 = new User(2, Role.USER, "Spring", "Boot", "Spring@gmail.com", "SpringUnit", "435346456");
        User user2 = new User(3, Role.ADMIN, "Java", "JRE", "jre@gmail.com", "JAVAJREUnit", "sdfsdfsdfs");
        usersList.add(user);
        usersList.add(user1);
        usersList.add(user2);
    }

    @Test
    void testGetAllUsersStatusOK() throws Exception {
        given(userService.findAll()).willReturn(usersList);
        mockMvc.perform(get("/users")).andExpect(status().is(200))
                .andExpect(jsonPath("$.size()", is(usersList.size())));
    }

    @Test
    void testGetAllUsersNOCONTENT() throws Exception {
        List<User> emptyList = new ArrayList();
        given(userService.findAll()).willReturn(emptyList);
        mockMvc.perform(get("/users")).andExpect(status().isNoContent());
    }

}
