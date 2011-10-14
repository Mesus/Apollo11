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

        employees = activityDao.findEmployees();
        modelAndView.addObject("employees", employees);
        return modelAndView;
    }

    @RequestMapping("/admin/addEmployee.htm")
    public ModelAndView getAddEmployeeView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("employee_name");
        employees = activityDao.findEmployees();
        for (Employee e : employees) {
            if (e.getName().equalsIgnoreCase(name)) {
                message = "Employee is already registered.";
            }
        }
        message = activityDao.addEmployee(name);
        employees = activityDao.findEmployees();
        modelAndView.addObject("employees", employees);
        modelAndView.addObject("message", message);
        return modelAndView;
    }

    @RequestMapping ("/admin/deleteEmployee.htm")
    public ModelAndView getDeleteEmployeeView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("Delete");
        employees = activityDao.findEmployees();
        for (Employee e : employees) {
            if (e.getName().equalsIgnoreCase(name)) {
                message = activityDao.deleteEmployee(name);
            }
        }
        employees = activityDao.findEmployees();
        modelAndView.addObject("employees", employees);
        modelAndView.addObject("message", message);
        return modelAndView;
    }
}
