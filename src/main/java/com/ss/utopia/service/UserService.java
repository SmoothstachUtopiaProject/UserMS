package com.ss.utopia.service;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.time.DateUtils;
import org.bouncycastle.asn1.dvcs.Data;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.email.model.MailRequest;
import com.ss.utopia.email.model.MailResponse;
import com.ss.utopia.email.service.EmailService;
import com.ss.utopia.exception.ExpiredTokenExpception;
import com.ss.utopia.exception.IncorrectPasswordException;
import com.ss.utopia.exception.TokenNotFoundExpection;
import com.ss.utopia.exception.UserAlreadyExistsException;
import com.ss.utopia.exception.UserNotFoundException;
import com.ss.utopia.exception.UserRoleNotFoundException;
import com.ss.utopia.model.User;
import com.ss.utopia.model.UserRole;
import com.ss.utopia.model.UserToken;
import com.ss.utopia.repository.UserRepository;
import com.ss.utopia.repository.UserTokenRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserRoleService userRoleService;
	
	@Autowired
	UserTokenRepository userTokenRepository;
	
	@Autowired
	EmailService emailService;

	public List<User> findAll() throws ConnectException, SQLException {
		return userRepository.findAll();
	}
	
	public MailResponse sendRecoveryEmail(String email) throws ConnectException, IllegalArgumentException, SQLException, UserNotFoundException {
		
		User user = findByEmail(email);
		UserToken userToken = new UserToken(user);
		userTokenRepository.save(userToken);
		return sendEmail(user, userToken);
		
	}
	
	public void ChangePassword(UserToken userToken, String password) throws ConnectException, IllegalArgumentException, SQLException, UserNotFoundException {
		User user = findById(userToken.getUser().getId());
		user.setPassword(password);
		userRepository.save(user);
	}
	
	// verify if token is exists and if not expired. 
	public UserToken verifyToken(String token) throws ExpiredTokenExpception, TokenNotFoundExpection {
		
		UserToken uToken = null;
		Optional<UserToken> userToken = userTokenRepository.findById(token);
		if(userToken.isPresent()) {
			uToken = userToken.get();
		}
		if(!userToken.isPresent()) {
			throw new TokenNotFoundExpection("Unexpected error occured");
		}
		//date when token was issued
		Date tokenDate = uToken.getDate();
		// +15 minutes to determine id token is expired 
		tokenDate = DateUtils.addMinutes(tokenDate, 15);
		// current dateTime
		Date dateNow = new Date();
		// if current date is after token's expiration date 
		if(dateNow.after(tokenDate)) {
			throw new ExpiredTokenExpception("Link is expired");
		}
		
		return uToken;
		
		
		
		
		
		
	}
	
	
	public MailResponse sendEmail(User user, UserToken userToken) {
		Map<String, Object> modelsMap = new HashMap<>();
		
		String recoveryCode = userToken.getToken();
		String userName = user.getFirstName();
		
		modelsMap.put("name", userName);
		modelsMap.put("confirmation", recoveryCode);
		
		MailRequest mailRequest = new MailRequest(user.getEmail());
		return emailService.sendEmail(mailRequest, modelsMap);
		
	}
	
	public User verifyUser(String email, String password) throws UserNotFoundException, IncorrectPasswordException {
		System.out.println("test");
		Optional<User> checkUser = userRepository.findByEmail(email);
		
		if(!checkUser.isPresent()) {
			throw new UserNotFoundException("Invalid Email");
		} else if(!checkUser.get().getPassword().equals(password)) {
			throw new IncorrectPasswordException("Invalid password");
		} else return checkUser.get();
		
	}

	public User findByEmail(String email) throws ConnectException, 
		IllegalArgumentException,SQLException, UserNotFoundException {

		String formattedEmail = formatGeneric(email);
		if(!validateEmail(formattedEmail)) throw new IllegalArgumentException("The email: \"" + email + "\" is not valid!");

		Optional<User> optionalUser = userRepository.findByEmail(formattedEmail);
		if(!optionalUser.isPresent()) throw new UserNotFoundException("No user with email: \"" + email + "\" exist!");
		return optionalUser.get();
	}

	public User findById(Integer id) throws ConnectException, IllegalArgumentException,
		SQLException, UserNotFoundException {

		Optional<User> optionalUser = userRepository.findById(id);
		if(!optionalUser.isPresent()) throw new UserNotFoundException("No user with ID: \"" + id + "\" exist!");
		return optionalUser.get();
	}

	public User findByPhone(String phone) throws ConnectException, 
	IllegalArgumentException,SQLException, UserNotFoundException {

	String formattedPhone = formatPhone(phone);
	if(!validatePhone(formattedPhone)) throw new IllegalArgumentException("The phone: \"" + phone + "\" is not valid!");

	Optional<User> optionalUser = userRepository.findByPhone(formattedPhone);
	if(!optionalUser.isPresent()) throw new UserNotFoundException("No user with phone: \"" + phone + "\" exist!");
	return optionalUser.get();
}

	public List<User> findByRoleId(Integer userRoleId) throws ConnectException,
		IllegalArgumentException, SQLException, UserRoleNotFoundException {

		UserRole role = userRoleService.findById(userRoleId);
		return userRepository.findByRoleId(role.getId());
	}

	public List<User> findByRoleName(String userRoleName) throws ConnectException,
	IllegalArgumentException, SQLException, UserRoleNotFoundException {

		UserRole role = userRoleService.findByName(userRoleName);
		return userRepository.findByRoleId(role.getId());
	}

	public User insert(Integer userRoleId, String firstName,
		String lastName, String email, String password, String phone) 
		throws ConnectException, IllegalArgumentException, SQLException, 
		UserAlreadyExistsException {
		
		String formattedFirstName = formatGeneric(firstName);
		String formattedLastName = formatGeneric(lastName);
		String formattedEmail = formatGeneric(email);
		String formattedPhone = formatPhone(phone);

		if(!validateName(formattedFirstName)) throw new IllegalArgumentException("A name cannot exceed 255 characters!");
		if(!validateName(formattedLastName)) throw new IllegalArgumentException("A name cannot exceed 255 characters!");
		if(!validateEmail(formattedEmail)) throw new IllegalArgumentException("The email: \"" + email + "\" is not valid!");
		if(!validateName(password)) throw new IllegalArgumentException("A password cannot exceed 255 characters!");
		if(!validatePhone(formattedPhone)) throw new IllegalArgumentException("The phone number: \"" + phone + "\" is not valid!");

		try {
			Optional<User> optionalUser1 = userRepository.findByEmail(formattedEmail);
			Optional<User> optionalUser2 = userRepository.findByPhone(formattedPhone);
			
			if(optionalUser1.isPresent()) {
				throw new UserAlreadyExistsException("A user with this email already exists!");
			}
			
			if( optionalUser2.isPresent()) {
				throw new UserAlreadyExistsException("A user with this phone number already exists!");
			}
			UserRole userRole = userRoleService.findById(userRoleId);
			return userRepository.save(new User(userRole, formattedFirstName, formattedLastName, formattedEmail, password, formattedPhone));
		
		} catch(UserRoleNotFoundException err) {
			throw new IllegalArgumentException(err.getMessage());
		}		
	}

	public User update(Integer id, Integer userRoleId, String firstName,
		String lastName, String email, String password, String phone) 
		throws ConnectException, IllegalArgumentException, SQLException, 
		UserNotFoundException {
		
		String formattedFirstName = formatGeneric(firstName);
		String formattedLastName = formatGeneric(lastName);
		String formattedEmail = formatGeneric(email);
		String formattedPhone = formatPhone(phone);

		if(!validateName(formattedFirstName)) throw new IllegalArgumentException("A name cannot exceed 255 characters!");
		if(!validateName(formattedLastName)) throw new IllegalArgumentException("A name cannot exceed 255 characters!");
		if(!validateEmail(formattedEmail)) throw new IllegalArgumentException("The email: " + email + " is not valid!");
		if(!validatePassword(password)) throw new IllegalArgumentException("A password cannot exceed 255 characters!");
		if(!validatePhone(formattedPhone)) throw new IllegalArgumentException("The phone number: " + phone + " is not valid!");

		try {
			findById(id);
			UserRole userRole = userRoleService.findById(userRoleId);
			return userRepository.save(new User(id, userRole, formattedFirstName, formattedLastName, formattedEmail, password, formattedPhone));
		} catch(UserRoleNotFoundException err) {
			throw new IllegalArgumentException(err.getMessage());
		}
	}

	public void delete(Integer id) throws ConnectException, IllegalArgumentException, 
	UserNotFoundException, SQLException {
		findById(id);
		userRepository.deleteById(id);
	}

	private String formatGeneric(String name) {
		return name.trim().toUpperCase();
	}

	private String formatPhone(String phone) {
		return phone.replaceAll("[^0-9]", "");
	}

	private Boolean validateEmail(String email) {
		Pattern pattern = Pattern.compile("^(.+)@(.+)$");
		Matcher matcher = pattern.matcher(email);
		return email != null &&
		matcher.matches() && 
		email.length() < 256 && 
		!email.isEmpty();
	}

	private Boolean validateName(String name) {
		return name != null && 
		name.length() < 256 &&
		!name.isEmpty();
	}

	private Boolean validatePassword(String password) {
		return password != null && 
		password.length() < 256 &&
		!password.isEmpty();
	}

	private Boolean validatePhone(String phone) {
		return phone != null &&
		phone.length() < 46 &&
		phone.replaceAll("[^0-9#]", "").length() == phone.length() &&
		!phone.isEmpty();
	}
}