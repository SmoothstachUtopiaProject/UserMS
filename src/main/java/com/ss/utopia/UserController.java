package com.ss.utopia;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.utopia.exception.UserAlreadyExistsException;
import com.ss.utopia.exception.UserNotFoundException;
import com.ss.utopia.exception.UserRoleNotFoundException;
import com.ss.utopia.model.User;
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
	public ResponseEntity<Object> findAll() {
		try{
			List<User> userList = userService.findAll();
			return !userList.isEmpty() 
			? new ResponseEntity<>(userList, HttpStatus.OK)
			: new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

		} catch(SQLException err) {
			return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
		}
	}

	@GetMapping("{userId}")
	public ResponseEntity<Object> findById(@PathVariable Integer userId) {
		try {
			User user = userService.findById(userId);
			return new ResponseEntity<>(user, HttpStatus.OK);

		} catch(IllegalArgumentException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
		} catch(UserNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
		} catch(SQLException err) {
			return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
		}
	}

	@GetMapping("/email")
	public ResponseEntity<Object> findByEmail(@RequestBody String email) {
		try {
			User user = userService.findByEmail(email);
			return new ResponseEntity<>(user, HttpStatus.OK);

		} catch(IllegalArgumentException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
		} catch(UserNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
		} catch(SQLException err) {
			return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
		}
	}

	@GetMapping("/search")
	public ResponseEntity<Object> findByRoleId(@RequestParam Integer roleId) {
		try{
			List<User> userList = userService.findByRoleId(roleId);
			return !userList.isEmpty() 
			? new ResponseEntity<>(userList, HttpStatus.OK)
			: new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

		} catch(IllegalArgumentException | UserRoleNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
		} catch(SQLException err) {
			return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
		}
	}

	@PostMapping
	public ResponseEntity<Object> insert(@RequestBody String body) {
		try {
			User user = new ObjectMapper().readValue(body, User.class);
			Integer userRole = Integer.parseInt(body.replaceAll("[^a-zA-Z0-9,]", "").split("userRoleid")[1].split(",")[0]);
			
			User newUser = userService.insert(userRole, user.getFirstName(), 
			user.getLastName(), user.getEmail(), user.getPassword(), user.getPhone());
			return new ResponseEntity<>(newUser, HttpStatus.CREATED);

		} catch(ArrayIndexOutOfBoundsException | IllegalArgumentException | 
			JsonProcessingException | NullPointerException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
		} catch(UserAlreadyExistsException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.CONFLICT);
		} catch(SQLException err) {
			return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
		}
	}

	@PutMapping("{userId}")
	public ResponseEntity<Object> update(@PathVariable Integer userId, @RequestBody String body) {
		try {
			User user = new ObjectMapper().readValue(body, User.class);
			Integer userRole = Integer.parseInt(body.replaceAll("[^a-zA-Z0-9,]", "").split("userRoleid")[1].split(",")[0]);

			User newUser = userService.update(userId, userRole, user.getFirstName(), 
			user.getLastName(), user.getEmail(), user.getPassword(), user.getPhone());
			return new ResponseEntity<>(newUser, HttpStatus.ACCEPTED);

		} catch(ArrayIndexOutOfBoundsException | IllegalArgumentException | 
			JsonProcessingException | NullPointerException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
		} catch(SQLException err) {
			return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
		} catch(UserNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("{userId}")
	public ResponseEntity<Object> delete(@PathVariable Integer userId) {
		try {
			userService.delete(userId);
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

		} catch(SQLException err) {
			return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
		} catch(UserNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> invalidRequestContent() {
		return new ResponseEntity<>("Invalid Message Content!", HttpStatus.BAD_REQUEST);
	}
}