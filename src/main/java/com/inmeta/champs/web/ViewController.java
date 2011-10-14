package com.inmeta.champs.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.PriorityQueue;


@org.springframework.stereotype.Controller
public class ViewController extends BaseController {


    @RequestMapping("/callback.htm")
    public ModelAndView Callback(HttpServletRequest req, HttpServletResponse res) {


        Enumeration<String> params = req.getParameterNames();
        StringBuffer sb = new StringBuffer();
        while (params.hasMoreElements()) {
            String parameterName = params.nextElement();
            String value = req.getParameter(parameterName);
            sb.append("  " + parameterName + ", " + value);
        }

        // Kommer man hit ved feil?

        String email = req.getParameter("email");
        req.getSession().setAttribute("email", email);


        ModelAndView mv = new ModelAndView();
        mv.addObject("callbackParams", sb.toString());
        return mv;
    }

    @RequestMapping("/rawtest.htm")
    public void rawResponseTest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String result = "{\"registered\": true }";
        res.setContentType("application/json");
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(res.getOutputStream()));
        pw.println(result);
        pw.close();
        res.getOutputStream().close();
    }

    @RequestMapping("/simpleWayOut.htm")
    public void simpleWayOut() {
    }


    @RequestMapping("/admin/home.htm")
    public void adminHomeHandler() {
    }

    @RequestMapping("/home.htm")
    public void homeHandler() {
    }

    @RequestMapping("/login.htm")
    public void loginHandler() {
    }

    /*
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ModelAndView modelAndView = new ModelAndView("home");

        if (request.getRequestURI().equals("/admin/home.htm")) {
            return new ModelAndView("admin/home");
        }

        if (request.getRequestURI().equals("/updateEmployees.htm")) {
            return new ModelAndView("admin/updateEmployees");
        }

        if (request.getRequestURI().equals("/login.htm")) {
            return new ModelAndView("login");
        }

        if (request.getRequestURI().equals("/loginError.htm")) {
            return new ModelAndView("loginError");
        }
        
        String userType = "ROLE_ADMIN";
        modelAndView.addObject("userType", userType);
        return modelAndView;


	}
	*/
}

