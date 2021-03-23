package com.ss.utopia.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.exceptions.UserAlreadyExistsException;
import com.ss.utopia.exceptions.UserNotFoundException;
import com.ss.utopia.models.User;
import com.ss.utopia.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User insert(User user) throws UserAlreadyExistsException {

		Optional<User> checkIfEmailExist = userRepository.findByEmail(user.getUserEmail());
		Optional<User> checkIfPhoneExist = userRepository.findByPhone(user.getUserPhone());
		if (checkIfEmailExist.isPresent()) {
			throw new UserAlreadyExistsException("A user with this email already exists!");
		}
		if (checkIfPhoneExist.isPresent()) {
			throw new UserAlreadyExistsException("A user with this phone number already exists!");
		}
		return userRepository.save(user);
	}

	public User findByEmail(String email) throws UserNotFoundException {
		Optional<User> optionalUser = userRepository.findByEmail(email);
		if (!optionalUser.isPresent()) {
			throw new UserNotFoundException("No user with email: " + email + " exist!");
		}
		return optionalUser.get();
	}

	public User findById(Integer id) throws UserNotFoundException {

		Optional<User> optionalUser = userRepository.findById(id);
		if (!optionalUser.isPresent()) {
			throw new UserNotFoundException("No user with ID: " + id + " exist!");
		}
		return optionalUser.get();
	}

	public User findByPhone(String phone) throws UserNotFoundException {
		Optional<User> optionalUser = userRepository.findByPhone(phone);
		if (!optionalUser.isPresent()) {
			throw new UserNotFoundException("No user with phone: " + phone + " exist!");
		}
		return optionalUser.get();
	}

	public User update(User user) {
		return userRepository.save(user);
	}

	public void delete(Integer id) throws UserNotFoundException {
		findById(id);
		userRepository.deleteById(id);
	}
}