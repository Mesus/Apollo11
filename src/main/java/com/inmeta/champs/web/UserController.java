package com.inmeta.champs.web;

import com.inmeta.champs.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Guri Lunnan
 */

@Controller
public class UserController extends BaseController {
    private List<User> users;
    private List<User> userRequests;
    private String message = "";
    ModelAndView modelAndView = new ModelAndView("admin/updateUsers");

    @RequestMapping("/admin/updateUsers.htm")
    public ModelAndView updateUserHandler(HttpServletRequest request, HttpServletResponse response) {
        if (userService.isAuthorized(request, roleAdmin)) {
            users = userRepository.getUsers();
            userRequests = userRepository.getUserRequests();
            modelAndView.addObject("userRequests", userRequests);
            modelAndView.addObject("users", users);
            modelAndView.addObject("message", message);
            return modelAndView;
        } else return new ModelAndView("permissionDenied");
    }

    @RequestMapping("/admin/addUser.htm")
    public ModelAndView addUserHandler(HttpServletRequest request, HttpServletResponse response) {
        if (userService.isAuthorized(request, roleAdmin)) {
            String email = request.getParameter("email");
            String userRole = request.getParameter("userRole");
            User user = new User();
            user.setEmail(email);
            user.setUserRole(userRole);
            if (userRepository.isRegistered(user)) {
                message = "Brukeren er allerede registrert.";
            } else {
                user.setRegistered(userRepository.addUser(user));
                userRepository.deleteRequest(user);
                if (user.isRegistered()) {
                    message = "Registrerte bruker " + user.getEmail() + " med tilgang " + user.getUserRole() + ".";
                } else {
                    message = "Feil ved registrering.";
                }
            }

            users = userRepository.getUsers();
            userRequests = userRepository.getUserRequests();
            modelAndView.addObject("userRequests", userRequests);
            modelAndView.addObject("users", users);
            modelAndView.addObject("message", message);
            return modelAndView;
        } else return new ModelAndView("permissionDenied");
    }

    @RequestMapping("/admin/deleteUser.htm")
    public ModelAndView deleteUserHandler(HttpServletRequest request, HttpServletResponse response) {
        if (userService.isAuthorized(request, roleAdmin)) {
            String email = request.getParameter("DeleteUser");
            users = userRepository.getUsers();
            boolean success = false;
            for (User u : users) {
                if (u.getEmail().equals(email)) {
                    success = userRepository.deleteUser(u);
                }
            }
            if (success) {
                message = "Deleted " + email + " from users.";
            } else {
                message = "Could not delete " + email + " from users.";
            }
            users = userRepository.getUsers();
            userRequests = userRepository.getUserRequests();
            modelAndView.addObject("users", users);
            modelAndView.addObject("userRequests", userRequests);
            modelAndView.addObject("message", message);
            return modelAndView;
        } else return new ModelAndView("permissionDenied");
    }

    @RequestMapping("/admin/deleteRequest.htm")
    public ModelAndView deleteRequestHandler(HttpServletRequest request, HttpServletResponse response) {
        if (userService.isAuthorized(request, roleAdmin)) {
            String email = request.getParameter("Delete");
            userRequests = userRepository.getUserRequests();
            boolean success = false;
            for (User u : userRequests) {
                if (u.getEmail().equals(email)) {
                    success = userRepository.deleteRequest(u);
                }
            }
            if (success) {
                message = "Deleted " + email + " from requests.";
            } else {
                message = "Could not delete " + email + " from requests.";
            }
            users = userRepository.getUsers();
            userRequests = userRepository.getUserRequests();
            modelAndView.addObject("users", users);
            modelAndView.addObject("userRequests", userRequests);
            modelAndView.addObject("message", message);
            return modelAndView;
        } else return new ModelAndView("permissionDenied");
    }

}
