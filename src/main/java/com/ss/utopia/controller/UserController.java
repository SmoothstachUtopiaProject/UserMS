package com.ss.utopia.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

	@GetMapping
	public ResponseEntity<List<User>> findAllUsers() {
		List<User> userList = userService.allUsers();

		if (userList.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		} 
		return new ResponseEntity<>(userList, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<User> findById(@PathVariable Integer id){
		User user = userService.findById(id);
		if(user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(user);
		
	}
	
	

	@PostMapping("/new")
	public ResponseEntity<String> inserUser(@RequestBody User user) throws NullPointerException {
		UserRole ur = userRoleService.findUserRoleById(1);
		user.setUserRole(ur);
		userService.saveUser(user);
		return new ResponseEntity<String>("done", HttpStatus.OK);
	}
	
	

}