package com.ss.utopia.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.model.User;
import com.ss.utopia.repository.UserRepository;


@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	public List<User> allUsers(){
		return (List<User>) userRepository.findAll();
	}

	public void saveUser(User user) {
		userRepository.save(user);
	}
	
	public User findById(Integer id) {
		Optional<User> user = userRepository.findById(id);
		if(user.isPresent()) {
			return user.get();
		} else return null;
	}
	
	public boolean DeteleUser(Integer id) {
		Optional<User> user = userRepository.findById(id);
		if(!user.isPresent()) {
			return false;
		}
		userRepository.deleteById(id);
		return true;
	}
}