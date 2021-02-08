package com.ss.utopia.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.ss.utopia.model.UserRole;
import com.ss.utopia.repository.UserRoleRepository;


@SpringBootTest
public class UserRoleServiceEvaluation {
	
	@Mock
	private UserRole userRole;
	
	@Mock
	private UserRoleRepository userRoleRepository;

	@InjectMocks
	private UserRoleService userRoleService;
	
	@BeforeEach
	public void setup() throws Exception{
		userRole = new UserRole(1, "user");
		when(userRoleRepository.findUserRoleById(1)).thenReturn(userRole);
		when(userRoleRepository.findUserRoleById(0)).thenReturn(null);
	}
	
	@Test
	public void testFindUserRoleByIdSuccess() {
		assertEquals(userRoleRepository.findUserRoleById(1), userRole);
	}
	@Test
	public void testFindUserRoleByIdFailure() {
		assertEquals(userRoleRepository.findUserRoleById(0), null);
	}	

}
