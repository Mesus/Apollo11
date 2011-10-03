package springapp.web;

import com.gurilunnan.champs.model.Employee;
import com.gurilunnan.champs.persistence.ActivityRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.swing.tree.RowMapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.java2d.pipe.SpanShapeRenderer;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeController implements Controller {

    protected final Log logger = LogFactory.getLog(getClass());

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActivityRepository activityRepository = new ActivityRepository();
        List<Employee> employees;
        String message = "";
        ModelAndView modelAndView = new ModelAndView("updateEmployees");

        if(request.getRequestURI().equals("/updateEmployees.htm")) {
            employees = activityRepository.findEmployees();
            modelAndView.addObject("employees", employees);
            return modelAndView;
        }

        if (request.getRequestURI().equals("/addEmployee.htm")) {
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
        }

        if (request.getRequestURI().equals("/deleteEmployee.htm")) {
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
        }
        return new ModelAndView("welcome");
    }
}
