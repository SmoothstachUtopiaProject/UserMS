package com.ss.utopia.controller;

import java.net.ConnectException;
import java.security.Principal;
import java.sql.SQLException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ss.utopia.exceptions.UserNotFoundException;
import com.ss.utopia.jwk.JwtTokenProvider;
import com.ss.utopia.models.User;
import com.ss.utopia.services.UserService;

@RestController
@CrossOrigin()
@RequestMapping(value = "/auth")
public class AuthController {
	
	@Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserService userService;
	
	@GetMapping("/login")
	public ResponseEntity<Object> login(Principal principal) throws ConnectException, IllegalArgumentException, SQLException, UserNotFoundException{

		if(principal == null) {
			return ResponseEntity.ok(principal);
		}
		UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) principal;
		User user = userService.findByEmail(authenticationToken.getName());
		user.setUserToken(tokenProvider.generateToken(authenticationToken));
		
		return new ResponseEntity<>(user, HttpStatus.OK);
		
	};
	
	@DeleteMapping("{userId}")
	public ResponseEntity<Object> delete(@PathVariable Integer userId) throws ConnectException, IllegalArgumentException, SQLException{
		try {
			userService.delete(userId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (UserNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}
	
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Object> invalidConnection() {
		return new ResponseEntity<>("Invalid username of", HttpStatus.UNAUTHORIZED);
	}
}