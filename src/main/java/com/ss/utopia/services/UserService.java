package com.ss.utopia.services;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ss.utopia.email.models.MailRequest;
import com.ss.utopia.email.models.MailResponse;
import com.ss.utopia.email.services.EmailService;
import com.ss.utopia.exceptions.IncorrectPasswordException;
import com.ss.utopia.exceptions.PasswordNotAllowedException;
import com.ss.utopia.exceptions.TokenAlreadyIssuedException;
import com.ss.utopia.exceptions.UserAlreadyExistsException;
import com.ss.utopia.exceptions.UserNotFoundException;
import com.ss.utopia.models.User;
import com.ss.utopia.models.UserToken;
import com.ss.utopia.repositories.UserRepository;
import com.ss.utopia.repositories.UserTokenRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserTokenRepository userTokenRepository;

	@Autowired
	UserTokenService userTokenService;

	@Autowired
	EmailService emailService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<User> findAll() throws ConnectException, SQLException {
		return userRepository.findAll();
	}

	public User insert(User user) throws UserAlreadyExistsException {

		Optional<User> checkIfEmailExist = userRepository.findByEmail(user.getUserEmail());
		Optional<User> checkIfPhoneExist = userRepository.findByPhone(user.getUserPhone());
		if (checkIfEmailExist.isPresent()) {
			System.out.println("email");
			throw new UserAlreadyExistsException("A user with this email already exists!");
		}
		if (checkIfPhoneExist.isPresent()) {
			System.out.println("phone");
			throw new UserAlreadyExistsException("A user with this phone number already exists!");
		}

		user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
		return userRepository.save(user);
	}

	public MailResponse sendRecoveryEmail(String email) throws ConnectException, IllegalArgumentException, SQLException,
			UserNotFoundException, TokenAlreadyIssuedException {

		User user = findByEmail(email);
		// getting current date and subtracting 15 minutes to check if token already
		// issued
		Date currentDateTimeMinuts15Minutes = new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(15));
		boolean userTokens = userTokenService.verifyIfTokenBeenIssuedin15Min(user.getUserId(),
				currentDateTimeMinuts15Minutes);
		if (!userTokens)
			throw new TokenAlreadyIssuedException("You can only request a link once every 15 minutes!");

		// if token has't been issued in the last 15 minutes, issue a token and send an
		// email to user.
		UserToken userToken = new UserToken(user);
		userTokenRepository.save(userToken);
		return sendEmail(user, userToken);

	}

	public void ChangePassword(UserToken userToken, String password) throws ConnectException, IllegalArgumentException,
			SQLException, UserNotFoundException, PasswordNotAllowedException {
		User user = findById(userToken.getUser().getUserId());
		if (user.getUserPassword().equals(password))
			throw new PasswordNotAllowedException("Previously used password not allowed");
		user.setUserPassword(password);
		userRepository.save(user);
	}

	public MailResponse sendEmail(User user, UserToken userToken) {
		Map<String, Object> modelsMap = new HashMap<>();

		String recoveryCode = userToken.getToken();
		String userName = user.getUserFirstName();

		modelsMap.put("name", userName);
		modelsMap.put("confirmation", recoveryCode);

		MailRequest mailRequest = new MailRequest(user.getUserEmail());
		return emailService.sendEmail(mailRequest, modelsMap);

	}

	public User verifyUser(String email, String password) throws UserNotFoundException, IncorrectPasswordException {
		Optional<User> checkUser = userRepository.findByEmail(email);

		if (!checkUser.isPresent()) {
			throw new UserNotFoundException("Invalid Email");
		} else if (!checkUser.get().getUserPassword().equals(password)) {
			throw new IncorrectPasswordException("Invalid password");
		} else
			return checkUser.get();

	}

	public User findByEmail(String email)
			throws ConnectException, IllegalArgumentException, SQLException, UserNotFoundException {

		String formattedEmail = formatGeneric(email);
		if (!validateEmail(formattedEmail))
			throw new IllegalArgumentException("The email: \"" + email + "\" is not valid!");

		Optional<User> optionalUser = userRepository.findByEmail(formattedEmail);
		if (!optionalUser.isPresent())
			throw new UserNotFoundException("No user with email: \"" + email + "\" exist!");
		return optionalUser.get();
	}

	public User findById(Integer id)
			throws ConnectException, IllegalArgumentException, SQLException, UserNotFoundException {

		Optional<User> optionalUser = userRepository.findById(id);
		if (!optionalUser.isPresent())
			throw new UserNotFoundException("No user with ID: \"" + id + "\" exist!");
		return optionalUser.get();
	}

	public User findByPhone(String phone)
			throws ConnectException, IllegalArgumentException, SQLException, UserNotFoundException {

		String formattedPhone = formatPhone(phone);
		if (!validatePhone(formattedPhone))
			throw new IllegalArgumentException("The phone: \"" + phone + "\" is not valid!");

		Optional<User> optionalUser = userRepository.findByPhone(formattedPhone);
		if (!optionalUser.isPresent())
			throw new UserNotFoundException("No user with phone: \"" + phone + "\" exist!");
		return optionalUser.get();
	}

	// public List<User> findByRoleId(Integer userRoleId) throws ConnectException,
	// IllegalArgumentException, SQLException, UserRoleNotFoundException {
	//
	// UserRole role = userRoleService.findById(userRoleId);
	// return userRepository.findByRoleId(role.getId());
	// }
	//
	// public List<User> findByRoleName(String userRoleName) throws
	// ConnectException,
	// IllegalArgumentException, SQLException, UserRoleNotFoundException {
	//
	// UserRole role = userRoleService.findByName(userRoleName);
	// return userRepository.findByRoleId(role.getId());
	// }

	// public User insert(Role userRole, String firstName,
	// String lastName, String email, String password, String phone)
	// throws ConnectException, IllegalArgumentException, SQLException,
	// UserAlreadyExistsException, UserRoleNotFoundException {
	//
	// String formattedFirstName = formatGeneric(firstName);
	// String formattedLastName = formatGeneric(lastName);
	// String formattedEmail = formatGeneric(email);
	// String formattedPhone = formatPhone(phone);
	//
	// Optional<User> optionalUser1 = userRepository.findByEmail(formattedEmail);
	// Optional<User> optionalUser2 = userRepository.findByPhone(formattedPhone);
	//
	// if(optionalUser1.isPresent()) {
	// throw new UserAlreadyExistsException("A user with this email already
	// exists!");
	// }
	//
	// if( optionalUser2.isPresent()) {
	// throw new UserAlreadyExistsException("A user with this phone number already
	// exists!");
	// }
	// return userRepository.save(new User(userRole, formattedFirstName,
	// formattedLastName, formattedEmail, passwordEncoder.encode(password),
	// formattedPhone ));
	// }

	public User update(Integer id, User user)
			throws ConnectException, IllegalArgumentException, SQLException, UserNotFoundException {
		User u = findById(id);
		user.setUserRole(u.getUserRole());
		return userRepository.save(user);
	}

	public void delete(Integer id)
			throws ConnectException, IllegalArgumentException, UserNotFoundException, SQLException {
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
		return email != null && matcher.matches() && email.length() < 256 && !email.isEmpty();
	}

	private Boolean validatePhone(String phone) {
		return phone != null && phone.length() < 46 && phone.replaceAll("[^0-9#]", "").length() == phone.length()
				&& !phone.isEmpty();
	}
}