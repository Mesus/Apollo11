package com.inmeta.champs.model;

public class User {
	private String firstname;
	private String lastname;
	private String userRole;
	private String email;
	private String password;
	
	public void setFirstName(String firstname) {
		this.firstname = firstname;
	}
	
	public String getFirstName() {
		return firstname;
	}
	
	public void setLastName(String lastname) {
		this.lastname = lastname;
	}

	public String getLastName() {
		return lastname;
	}
	
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	
	public String getUserRole() {
		return userRole;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
}

