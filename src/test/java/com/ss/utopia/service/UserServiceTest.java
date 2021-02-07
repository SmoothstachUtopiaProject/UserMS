package com.ss.utopia.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.ss.utopia.model.User;
import com.ss.utopia.model.UserRole;
import com.ss.utopia.repository.UserRepository;

@SpringBootTest
public class UserServiceTest {

	
	private User user;
	private UserRole userRole;
	List<User> uList = new ArrayList<>();
	

	@Mock
	private UserRepository userRepository;
	
	
	@InjectMocks
	private UserService userService;
	
	@BeforeEach
	public void setup() throws Exception{
		userRole = new UserRole(2, "agent");
		user = new User(userRole, "Test", "jUnit", "jUnit@test.com", "0000000000", "8888888888");
		uList.add(user);
		when(userRepository.findByRoleIdAndUserId(2, 17)).thenReturn(user);
		when(userRepository.findByRoleIdAndUserId(0, 17)).thenReturn(null);
		
		when(userRepository.findAllByRoleId(2)).thenReturn(uList);
	}
	
	@Test
	public void testFindByRoleIdAndUserIdSuccess() {
		assertEquals(userRepository.findByRoleIdAndUserId(2, 17), user);
	}
	
	@Test
	public void testFindByRoleIdAndUserIdFailure() {
		assertEquals(userRepository.findByRoleIdAndUserId(0, 17), null);
	}
	
	@Test
	public void testfindAllByRoleIdSuccess() {
		assertEquals(userRepository.findAllByRoleId(2), uList);
	}
	
	
	
	
	
	
	
}
