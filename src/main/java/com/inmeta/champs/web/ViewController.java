package com.inmeta.champs.web;

import com.google.apps.easyconnect.easyrp.client.basic.logic.login.LoginResponse;
import com.inmeta.champs.model.User;
import org.cloudfoundry.org.codehaus.jackson.JsonEncoding;
import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.spi.IIOServiceProvider;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@org.springframework.stereotype.Controller
public class ViewController extends BaseController {

    /* Sets the attribute email and user, if the user exists in the repository. If not, redirect to loginError, where there's a link to signup
    * and login. Redirects to homepage if user is registered. */
    @RequestMapping("/callback.htm")
    public ModelAndView callBack(HttpServletRequest req, HttpServletResponse res) {
        String email = req.getParameter("openid.ext1.value.attr0");
        User user = userRepository.getUser(email);
        ModelAndView mv = new ModelAndView();
        boolean registered = true;
        if (user == null) {
            user = new User();
            user.setEmail(email);
        }
        req.getSession().setAttribute("user", user);
        mv.addObject("registered", registered);
        mv.addObject("email", email);
        return mv;
    }

    @RequestMapping("/permissionDenied.htm")
    public void permissionDenied() {
    }

    @RequestMapping("/admin/permissionDenied.htm")
    public ModelAndView noAccess() {
        return new ModelAndView("permissionDenied");
    }


    @RequestMapping("/admin/home.htm")
    public ModelAndView adminHomeHandler(HttpServletRequest request, HttpServletResponse response) {
        if (userService.isAuthorized(request, roleAdmin)) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("Current_Year", current_year);
            String this_month = activityRepository.findMonth(current_month);
            modelAndView.addObject("Current_Month", this_month);
            int[] years = getYears();
            String[] months = activityRepository.findMonthList();
            modelAndView.addObject("Years", years);
            modelAndView.addObject("Months", months);
            return modelAndView;
        } else return new ModelAndView("permissionDenied");

    }

    @RequestMapping("/home.htm")
    public ModelAndView homeHandler(HttpServletRequest req, HttpServletResponse res) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            return loginHandler(req, res);
        } else if (user.getUserRole() == null) {
            return new ModelAndView("loginError");
        }
        if (userService.isAuthorized(req, roleMember) || userService.isAuthorized(req, roleAdmin)) {
            modelAndView.addObject("user", user);
            int[] years = getYears();
            modelAndView.addObject("Years", years);
            modelAndView.addObject("Current_Year", current_year);
            return modelAndView;
        } else {
            return new ModelAndView("permissionDenied");
        }

    }

    @RequestMapping("/login.htm")
    public ModelAndView loginHandler(HttpServletRequest request, HttpServletResponse response) {
        CloudEnvironment env = new CloudEnvironment();
        if (env.getInstanceInfo() != null) {
            String cloudCallback = "http://champs.cloudfoundry.com/callback.htm";
            request.getSession().setAttribute("CallbackUrl", cloudCallback);
        } else {
            String localCallback = "http://localhost:8080/callback.htm";
            request.getSession().setAttribute("CallbackUrl", localCallback);
        }
        String url = (String) request.getSession().getAttribute("CallbackUrl");
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("CallbackUrl", url);
        return modelAndView;
    }

    @RequestMapping("/loginError.htm")
    public void loginErrorhandler() {
    }

    @RequestMapping("/register.htm")
    public ModelAndView registerHandler(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView("signup");
        String message = "";
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        User user = new User();
        if (email.equals("")) {
            message = "Vennligst fyll ut epostadresse og prøv igjen. ";
        }
        if (username.equals("")) {
            message += "Vennligst skriv inn navn, og prøv igjen.";
        }
        user.setEmail(email);
        if (userRepository.isRegistered(user)) {
            message = "Brukeren med epost " + email + " er allerede registrert.";
        }
        if (userRepository.requestAccess(email, username)) {
            message = "Din forespørsel er registrert. Behandling av forespørsel kan ta litt tid, vennligst prøv å logge på senere.";
        } else {
            message = "Feil ved registrering, vennligst prøv igjen senere.";
        }
        modelAndView.addObject("message", message);
        return modelAndView;
    }

    @RequestMapping("/signup.htm")
    public ModelAndView signupHandler() {
        String message = "Vi støtter foreløpig kun gmail-kontoer.";
        ModelAndView modelAndView = new ModelAndView("signup");
        modelAndView.addObject("message", message);
        return modelAndView;
    }

    @RequestMapping("/signout.htm")
    public ModelAndView signoutHandler(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        if (request.getSession().getAttribute("user") != null) {
            request.getSession().setAttribute("user", null);
        }
        return loginHandler(request, response);
    }

    @RequestMapping("/admin/signout.htm")
    public ModelAndView signoutAdmin(HttpServletRequest request, HttpServletResponse response) {
        return signoutHandler(request, response);
    }

    @RequestMapping("/userStatus.htm")
    public void checkUserStatus(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        try {
            response.getWriter().write("{\"registered\": false}");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}

