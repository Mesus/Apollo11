package springapp.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: gurlunna
 * Date: 21.09.11
 * Time: 12:04
 * To change this template use File | Settings | File Templates.
 */
public class ViewController implements Controller {
    protected final Log logger = LogFactory.getLog(getClass());

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ModelAndView modelAndView = new ModelAndView("home");

        if (request.isUserInRole("ROLE_ADMIN") && request.getRequestURI().equals("/admin/adminhome.htm")) {
            return new ModelAndView("admin/adminhome");
        } else if (!(request.isUserInRole("ROLE_ADMIN")) && request.getRequestURI().equals("/admin/adminhome.htm")) {
            String message = "Du må ha administratortilgang for å gå til admin-view.";
            modelAndView.addObject("message", message);
            return modelAndView;
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

        return modelAndView;

    }
}

