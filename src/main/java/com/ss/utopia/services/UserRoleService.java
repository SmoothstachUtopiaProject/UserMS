package com.ss.utopia.services;

import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.exceptions.UserRoleNotFoundException;
import com.ss.utopia.models.UserRole;
import com.ss.utopia.repositories.UserRoleRepository;

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

	public UserRole findById(Integer id) throws SQLException, UserRoleNotFoundException {
		Optional<UserRole> optionalUserRole = userRoleRepository.findById(id);
		if(!optionalUserRole.isPresent()) throw new UserRoleNotFoundException("No UserRole with ID: \"" + id + "\" exist!");
		return optionalUserRole.get();
	}

	public UserRole findByName(String name) throws SQLException, UserRoleNotFoundException {
		Optional<UserRole> optionalUserRole = userRoleRepository.findByName(name);
		if(!optionalUserRole.isPresent()) throw new UserRoleNotFoundException("No UserRole with name: \"" + name + "\" exist!");
		return optionalUserRole.get();
	}
}