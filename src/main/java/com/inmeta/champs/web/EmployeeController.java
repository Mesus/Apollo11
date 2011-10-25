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
    private List<Employee> employees;
    private String message = "";
    ModelAndView modelAndView = new ModelAndView("admin/updateEmployees");

    @RequestMapping("/admin/updateEmployees.htm")
    public ModelAndView getUpdatedEmployeesView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (userService.isAuthorized(request, roleAdmin)) {
            employees = activityRepository.findEmployees();
            modelAndView.addObject("employees", employees);
            return modelAndView;
        } else return new ModelAndView("permissionDenied");

    }

    @RequestMapping("/admin/addEmployee.htm")
    public ModelAndView getAddEmployeeView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (userService.isAuthorized(request, roleAdmin)) {
            String name = request.getParameter("employee_name");
            employees = activityRepository.findEmployees();
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

    @RequestMapping("/admin/deleteEmployee.htm")
    public ModelAndView getDeleteEmployeeView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (userService.isAuthorized(request, roleAdmin)) {
            String name = request.getParameter("Delete");
            employees = activityRepository.findEmployees();
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
