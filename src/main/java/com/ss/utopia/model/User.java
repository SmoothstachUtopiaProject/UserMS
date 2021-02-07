package com.ss.utopia.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {

	@Id
	@Column(name = "id")
	private int id;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "role_id")
	private UserRole userRole;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "email")
	private String email;
	@Column(name = "password")
	private String password;
	@Column(name = "phone")
	private String phone;
	
//	@OneToMany
//	@JoinTable(name = "booking_user", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
//			@JoinColumn(name = "booking_id") })
//    private List<Booking> bookings;
	
	
	public User() {
	}
	
	

	public User(int id, UserRole userRole, String firstName, String lastName, String email, String password,
			String phone) {
		super();
		this.id = id;
		this.userRole = userRole;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phone = phone;
	}
	
	public User(UserRole userRole, String firstName, String lastName, String email, String password,
			String phone) {
		super();
		this.userRole = userRole;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phone = phone;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	};

}

