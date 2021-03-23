package com.ss.utopia.controller;

import com.ss.utopia.exceptions.UserAlreadyExistsException;
import com.ss.utopia.exceptions.UserNotFoundException;
import com.ss.utopia.models.Role;
import com.ss.utopia.models.User;
import com.ss.utopia.services.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping
	public ResponseEntity<Object> findAll() {
		List<User> userList = userService.findAll();
		return !userList.isEmpty() ? new ResponseEntity<>(userList, HttpStatus.OK)
				: new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

	@PostMapping
	public ResponseEntity<Object> insert(@RequestBody User user) throws UserAlreadyExistsException {
		user.setUserRole(Role.USER);
		return new ResponseEntity<>(userService.insert(user), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> update(@PathVariable Integer id, @RequestBody User user)
			throws UserNotFoundException {
		userService.findById(id);
		return new ResponseEntity<>(userService.update(user), HttpStatus.CREATED);
	}

	@GetMapping("{userId}")
	public ResponseEntity<Object> findById(@PathVariable Integer userId) throws UserNotFoundException {
		User user = userService.findById(userId);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping("/email/{email}")
	public ResponseEntity<Object> findByEmail(@PathVariable String email) throws UserNotFoundException {
		User user = userService.findByEmail(email);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping("/phone/{phone}")
	public ResponseEntity<Object> findByPhone(@PathVariable String phone) throws UserNotFoundException {
		User user = userService.findByPhone(phone);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@DeleteMapping("{userId}")
	public ResponseEntity<Object> delete(@PathVariable Integer userId) throws UserNotFoundException {
		userService.delete(userId);
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

}
