package com.inmeta.champs.web;

import com.inmeta.champs.model.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@org.springframework.stereotype.Controller
public class ViewController extends BaseController {

    /* Sets the attribute email and user, if the user exists in the repository. If not, redirect to loginError, where there's a link to signup
    * and login. Redirects to homepage if user is registered. */
    @RequestMapping("/callback.htm")
    public ModelAndView callBack(HttpServletRequest req, HttpServletResponse res) {
        String email = req.getParameter("openid.ext1.value.attr0");
        User user = userRepository.getUser(email);
        if (user == null) {
            return new ModelAndView("loginError");
        }
        req.getSession().setAttribute("user", user);
        ModelAndView mv = new ModelAndView();
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
            return new ModelAndView();
        } else return new ModelAndView("permissionDenied");

    }

    @RequestMapping("/home.htm")
    public ModelAndView homeHandler(HttpServletRequest req, HttpServletResponse res) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) req.getSession().getAttribute("user");
        if (userService.isAuthorized(req, roleMember) || userService.isAuthorized(req, roleAdmin)) {
            modelAndView.addObject("user", user);
            return modelAndView;
        } else {
            return new ModelAndView("permissionDenied");
        }

    }

    @RequestMapping("/login.htm")
    public void loginHandler() {
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
    public void signupHandler() {
    }

    @RequestMapping("/signout.htm")
    public ModelAndView signoutHandler(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        if (request.getSession().getAttribute("user") != null) {
            request.getSession().setAttribute("user", null);
        }
        return new ModelAndView("login");
    }

    @RequestMapping("/admin/signout.htm")
    public ModelAndView signoutAdmin(HttpServletRequest request, HttpServletResponse response) {
        return signoutHandler(request, response);
    }
}

