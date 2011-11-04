package com.inmeta.champs.web;

import com.inmeta.champs.model.Employee;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@org.springframework.stereotype.Controller
public class EmployeeController extends BaseController {

    /* Returns the update Employees view, with a list of employees added as an object to the view. */
    @RequestMapping("/admin/updateEmployees.htm")
    public ModelAndView updateEmployeesHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (userService.isAuthorized(request, roleAdmin)) {
            ModelAndView modelAndView = new ModelAndView("admin/updateEmployees");
            List<Employee> employees = activityRepository.findEmployees();
            modelAndView.addObject("employees", employees);
            return modelAndView;
        } else return new ModelAndView("permissionDenied");

    }

    /* This method adds an employee to the database, and returns the updated employee list and a confirm message with the update Employee view. */
    @RequestMapping("/admin/addEmployee.htm")
    public ModelAndView addEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (userService.isAuthorized(request, roleAdmin)) {
            String name = request.getParameter("employee_name");
            List<Employee> employees = activityRepository.findEmployees();
            String message = "";
            ModelAndView modelAndView = new ModelAndView("admin/updateEmployees");
            for (Employee e : employees) {
                if (e.getName().equalsIgnoreCase(name)) {
                    message = "Employee is already registered.";
                }
            }
            message = activityRepository.addEmployee(name);
            employees = activityRepository.findEmployees();
            modelAndView.addObject("employees", employees);
            modelAndView.addObject("message", message);
            return modelAndView;
        } else return new ModelAndView("permissionDenied");

    }

    /* This method deletes an employee from the database, and returns the updated employee list and a confirm message with the view. */
    @RequestMapping("/admin/deleteEmployee.htm")
    public ModelAndView deleteEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (userService.isAuthorized(request, roleAdmin)) {
            String name = request.getParameter("Delete");
            List<Employee> employees = activityRepository.findEmployees();
            String message = "";
            ModelAndView modelAndView = new ModelAndView("admin/updateEmployees");
            for (Employee e : employees) {
                if (e.getName().equalsIgnoreCase(name)) {
                    message = activityRepository.deleteEmployee(name);
                }
            }
            employees = activityRepository.findEmployees();
            modelAndView.addObject("employees", employees);
            modelAndView.addObject("message", message);
            return modelAndView;
        } else return new ModelAndView("permissionDenied");

    }
}
