package com.inmeta.champs.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class LoginController implements Controller {

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		String email = request.getParameter("email");
		if(request.getRequestURI().equals("/callback.htm")) {
			ModelAndView mav = new ModelAndView("home");
			mav.addObject("email", email);
			return mav;
		}
		return null;
	}

}
