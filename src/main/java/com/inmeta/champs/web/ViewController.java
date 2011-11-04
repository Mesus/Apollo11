package com.inmeta.champs.web;

import com.inmeta.champs.model.User;
import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@org.springframework.stereotype.Controller
public class ViewController extends BaseController {

    /* Google identity toolkit uses the callback jsp to determine if a user is registered.
    *  The callback method checks if the email belongs to a registered user, and adds that user to the session as an object. */
    @RequestMapping("/callback.htm")
    public ModelAndView callBack(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("openid.ext1.value.attr0");
        User user = userRepository.getUser(email);
        ModelAndView mv = new ModelAndView();
        boolean registered = true;
        if (user == null) {
            user = new User();
            user.setEmail(email);
        }
        request.getSession().setAttribute("user", user);
        mv.addObject("registered", registered);
        mv.addObject("email", email);
        return mv;
    }

    /* Returns the "permission denied" view, used when the user tries to access a webpage without the necessary authorization */
    @RequestMapping("/permissionDenied.htm")
    public ModelAndView permissionDenied() {
        return new ModelAndView("permissionDenied");
    }

    @RequestMapping("/admin/permissionDenied.htm")
    public ModelAndView adminPermissionDenied() {
        return permissionDenied();
    }

    /* Returns the administrator home page. Checks if the user is authorized to see the page, if not it returns "permission denied" jsp.
     * To dynamically display the years from start to current year, we add an array of years to display to the ModelAndView object. This method is inherited from the BaseController.
     * To display the months in norwegian in the view, we get the monthnames from the datasource. */
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
        } else return permissionDenied();

    }

    /* Returns the home view if the user is authorized. If the session has no user object, it returns the login view (by calling the loginHandler() method).
    *  If the user object exists, but has no userrole, it returns the loginError view.
    *  Else return the home view. We add the user object to the ModelAndView, since that is used in the jsp to determine what content to show (admin gets a different view from members). */
    @RequestMapping("/home.htm")
    public ModelAndView homeHandler(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return loginHandler(request, response);
        } else if (user.getUserRole() == null) {
            loginErrorhandler(request, response);
        }
        if (userService.isAuthorized(request, roleMember) || userService.isAuthorized(request, roleAdmin)) {
            modelAndView.addObject("user", user);
            int[] years = getYears();
            modelAndView.addObject("Years", years);
            modelAndView.addObject("Current_Year", current_year);
            return modelAndView;
        } else {
            return permissionDenied();
        }

    }

    /* This is the login method. First we check whether this is a local instance or if we're running in the cloud, to set the url to the callback view.
    *  Google identity toolkit needs this. We add the callback url to the ModelAndView and then return the login view. */
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

    /* Returns the loginError view. */
    @RequestMapping("/loginError.htm")
    public ModelAndView loginErrorhandler(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("loginError");
    }

    /* This method returns the signup view. If the email field is empty, add an error message and return the signup view.
    *  If the username field is empty, add another error message and return signup view.
    *  If the user is registered, add "already registered" error message and return signup view.
    *  Else add a confirm message and add the user request to the database. This is done in the userRepository.requestAccess() method.
     * Return the signup view.*/
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

    /* This method is used by Google identity Toolkit if the user tries to register a legacy account. We don't support legacy accounts,
    *  so we add an error message and return the signup view so the user can add gmail-address to request access. */
    @RequestMapping("/signup.htm")
    public ModelAndView signupHandler() {
        String message = "Vi støtter foreløpig kun gmail-kontoer.";
        ModelAndView modelAndView = new ModelAndView("signup");
        modelAndView.addObject("message", message);
        return modelAndView;
    }

    /* This method kills the session with session.invalidate() and returns the login view. */
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

    /* Google identity Toolkit uses this.*/
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

