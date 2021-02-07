package com.ss.utopia.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.model.User;
import com.ss.utopia.model.UserRole;
import com.ss.utopia.repository.UserRepository;
import com.ss.utopia.repository.UserRoleRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserRoleRepository userRoleRepository;

	public List<User> findAllUsers() {
		return (List<User>) userRepository.findAll();
	}

	public List<User> findAllByRole(Integer id) {
		return userRepository.findAllByRoleId(id);
	}

	public User findByRoleIdAndUserId(Integer roleId, Integer userId) {
		User user = (User) userRepository.findByRoleIdAndUserId(roleId, userId);
		return user != null ? user : null;
	}

	public User saveUser(UserRole userRole, User user) {
		User verifyIfUserExist = userRepository.findByEmail(user.getEmail());
		if (verifyIfUserExist == null) {
			user.setUserRole(userRole);
			userRepository.save(user);
			return user;
		}
		return null;

	}

	public User update(User user) throws SQLException {
		userRepository.save(user);
		return user;
	}

	public void deteleUser(Integer id) throws SQLException {
		userRepository.deleteByUserId(id);
	}
}