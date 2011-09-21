package springapp.web;

import com.gurilunnan.champs.model.Employee;
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

        InitialContext initialContext;
        DataSource dataSource;

        try {
            initialContext = new InitialContext();
            dataSource = (DataSource) initialContext.lookup("java:comp/env/jdbc/SimpleDS");

        } catch (NamingException e) {
            throw new ServletException(e);
        }
        SimpleJdbcTemplate t = new SimpleJdbcTemplate(dataSource);
        List<Employee> employees = new ArrayList<Employee>();
        String message = "test";

        if (request.getRequestURI().equals("/employee.htm")) {
            try {
                employees = t.query("SELECT * FROM EMPLOYEE", new EmployeeRowMapper(), new Object[]{});
                return new ModelAndView("employee", "employees", employees);
            } catch (EmptyResultDataAccessException e) {
                e.printStackTrace();
            }
        }

        if (request.getRequestURI().equals("/showEmployees.htm")) {
            try {
                employees = t.query("SELECT * FROM EMPLOYEE", new EmployeeRowMapper(), new Object[]{});
                return new ModelAndView("update", "employees", employees);
            } catch (EmptyResultDataAccessException e) {
                e.printStackTrace();
            }
        }

        if (request.getRequestURI().equals("/updateEmployee.htm")) {
            String name = request.getParameter("employee_name");
            try {
                employees = t.query("SELECT * FROM EMPLOYEE", new EmployeeRowMapper(), new Object[]{});
                System.out.print(employees);
                for (Employee e : employees) {
                    if (e.getName().equalsIgnoreCase(name)) {
                        message = "Employee is already registered.";
                        return new ModelAndView("update", "message", message);
                    }

                }
                t.update("insert into Employee values(?)", new Object[]{request.getParameter("employee_name")});
                message = "Successfully added " + name + " to Employees.";
                return new ModelAndView("update","message", message);
            } catch (EmptyResultDataAccessException e) {
                e.printStackTrace();
            }
        }

        if (request.getRequestURI().equals("/deleteEmployee.htm")) {
            String name = request.getParameter("employee_name");
            System.out.println(name);
            try {
                employees = t.query("SELECT * FROM EMPLOYEE", new EmployeeRowMapper(), new Object[]{});
                System.out.print(employees);
                for (Employee e : employees) {
                    if (e.getName().equalsIgnoreCase(name)) {
                        message = "Successfully deleted " + name + ".";
                        t.update("delete from Employee where name =? ", new Object[]{request.getParameter("employee_name")});
                        return new ModelAndView("update", "message", message);
                    }

                }

                employees = t.query("SELECT * FROM EMPLOYEE;", new EmployeeRowMapper(), new Object[]{});
                return new ModelAndView("employee", "employees", employees);
            } catch (EmptyResultDataAccessException e) {
                e.printStackTrace();
            }
        }
        return new ModelAndView("welcome");
    }

    class EmployeeRowMapper implements org.springframework.jdbc.core.RowMapper<Employee> {


        public Employee mapRow(ResultSet resultSet, int i) throws SQLException {
            Employee e = new Employee(resultSet.getString(1));
            return e;
        }
    }

}
