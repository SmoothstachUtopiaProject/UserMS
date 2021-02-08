package com.ss.utopia.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ss.utopia.model.User;
import com.ss.utopia.model.UserRole;
import com.ss.utopia.service.UserRoleService;
import com.ss.utopia.service.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	UserRoleService userRoleService;

	@GetMapping()
	public ResponseEntity<List<User>> findAll() {
		List<User> userList = userService.findAllUsers();
		return !userList.isEmpty() ? new ResponseEntity<>(userList, HttpStatus.OK)
				: new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

	@GetMapping("{roleId}")
	public ResponseEntity<List<User>> findAllByRoleId(@PathVariable Integer roleId) {
		List<User> userList = userService.findAllByRole(roleId);
		return !userList.isEmpty() ? new ResponseEntity<>(userList, HttpStatus.OK)
				: new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

	@PostMapping(path = "{roleId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Void> insert(@PathVariable Integer roleId, @RequestBody User user) {
		UserRole userRole = userRoleService.findUserRoleById(roleId);
		if (userRole == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		User newUser = userService.saveUser(userRole, user);
		return newUser != null ? new ResponseEntity<>(HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.CONFLICT);
	}

	@GetMapping("{roleId}/{userId}")
	public ResponseEntity<User> findByRoleIdAndUserId(@PathVariable Integer roleId, @PathVariable Integer userId) {
		User user = userService.findByRoleIdAndUserId(roleId, userId);
		return user != null ? new ResponseEntity<>(user, HttpStatus.OK)
				: new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

	@PutMapping(path = "{roleId}/{userId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<User> update(@PathVariable Integer roleId, @PathVariable Integer userId,
			@RequestBody User user) throws SQLException {
		User verifyUser = userService.findByRoleIdAndUserId(roleId, userId);
		if (verifyUser == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		User updateduser = userService.update(user);
		return updateduser != null ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<>(HttpStatus.CONFLICT);
	}

	@DeleteMapping(path = "{roleId}/{userId}")
	public ResponseEntity<Void> delete(@PathVariable Integer roleId, @PathVariable Integer userId) throws SQLException {
		User verifyUser = userService.findByRoleIdAndUserId(roleId, userId);
		if (verifyUser == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		userService.deteleUser(userId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);

	}
}