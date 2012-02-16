package com.inmeta.champs.service;

import com.inmeta.champs.model.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Guri Lunnan
 */
@Service
public class UserService {

	public UserService() {
	};

	/* This method checks if a user has the required userrole */
	public boolean isAuthorized(HttpServletRequest request, String userRole) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null && user.getUserRole() != null) {
			return user.getUserRole().equals(userRole);
		}
		return false;
	}
}
