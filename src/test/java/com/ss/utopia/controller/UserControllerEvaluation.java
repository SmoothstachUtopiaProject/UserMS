package com.ss.utopia.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.ss.utopia.model.User;
import com.ss.utopia.model.UserRole;
import com.ss.utopia.service.UserRoleService;
import com.ss.utopia.service.UserService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
public class UserControllerEvaluation {
	
	@MockBean
	private UserService userService;
	@MockBean
	private UserRoleService userRoleService;
	
	@InjectMocks
	private UserController userController;
	
	private User user;
	private MockMvc mockMvc;
	
	@Mock
	private UserRole userRole;
	
	
	@BeforeEach
	public void setUp() throws Exception{
		userRole = new UserRole(2, "agent");
		user = new User(userRole, "Test", "jUnit", "jUnit@test.com", "0000000000", "8888888888");
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
		
		doNothing().when(userService).saveUser(userRole, user);
		doNothing().when(userService).update(user);
		doNothing().when(userService).deteleUser(user.getId());
		
		when(userService.findByRoleIdAndUserId(2, 17)).thenReturn(user);
	}
	
	@Test
	public void getUserByUsernameTest() throws Exception {
		mockMvc.perform(get("/users/{roleId}/{userId}", user.getUserRole().getId(), user.getId())).andExpect(status().isOk())
		.andExpect(jsonPath("$.roleId", is(user.getUserRole().getId()))).andExpect(jsonPath("$.userId", is(user.getId())));
	}
	
	

}
