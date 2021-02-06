package com.ss.utopia.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.model.UserRole;
import com.ss.utopia.repository.UserRoleRepository;

/*
 * This service class of UserRole serves as an intermediate layer between the DAO layer and the controller layer.
 * It is ideally used to enforce business rules and ensuring that the controllers only job is to prepare data and send it to the dao, 
 * while the daos layer only responsibility is to query the database and return data.
 */
@Service
public class UserRoleService {

	// adding UserRoleRepository as a dependency 
	@Autowired
	UserRoleRepository userRoleRepository;

	public UserRole findUserRoleById(int id) {
		Optional<UserRole> userRole = userRoleRepository.findById(id);
		if(userRole.isPresent()) {
			return userRole.get();
		} else return null;
	}

}