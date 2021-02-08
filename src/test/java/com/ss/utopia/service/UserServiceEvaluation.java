package com.ss.utopia.service;

import static org.assertj.core.api.Assertions.assertThat;
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
public class UserServiceEvaluation {

	
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
		
		when(userRepository.findAllByRoleId(2)).thenReturn(uList);
		when(userRepository.findAllByRoleId(1)).thenReturn(null);
		
		when(userRepository.findByRoleIdAndUserId(2, 17)).thenReturn(user);
		when(userRepository.findByRoleIdAndUserId(0, 17)).thenReturn(null);
		
		when(userRepository.findByEmail("jUnit@test.com")).thenReturn(user);
		when(userRepository.findByEmail("jUnitFailure@test.com")).thenReturn(null);		
		
	}
	
	@Test
	public void testDeleteByUserIdSuccess() {
		userRepository.deleteByUserId(user.getId());
		assertThat(userRepository.count()).isEqualTo(0);
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
	public void testFindAllByRoleIdSuccess() {
		assertEquals(userRepository.findAllByRoleId(2), uList);
	}
	
	@Test
	public void testFindAllByRoleIdFailure() {
		assertEquals(userRepository.findAllByRoleId(1), null);
	}
	
	@Test
	public void testFindByEmailSuccess() {
		assertEquals(userRepository.findByEmail("jUnit@test.com"), user);
	}
	
	@Test
	public void testFindByEmailFailure() {
		assertEquals(userRepository.findByEmail("jUnitFailure@test.com"), null);
	}
	
}
