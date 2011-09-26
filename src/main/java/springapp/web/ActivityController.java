package springapp.web;

import com.gurilunnan.champs.model.Activity;
import com.gurilunnan.champs.model.ActivityType;
import com.gurilunnan.champs.model.Employee;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: gurlunna
 * Date: 22.09.11
 * Time: 12:55
 * To change this template use File | Settings | File Templates.
 */
public class ActivityController implements Controller {
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
        List<Activity> activityList;
        List<Employee> employeeList;
        List<ActivityType> activityTypeList;

        if (request.getRequestURI().equals("/activitiesPrMonth.htm")) {
            int year = Integer.parseInt(request.getParameter("Year"));
            String month = request.getParameter("Month");
            try {
                activityList = t.query("SELECT t.act_type, a.activity_name, a.employee_name, a.month_name, a.the_year FROM ACTIVITY a left join activity_type t on a.activity_name = t.activity_name WHERE a.month_name = ? and a.the_year = ?", new ActivityRowMapper(), new Object[]{month, year});
                System.out.println(activityList);
                ModelAndView modelAndView = new ModelAndView("activitiesPrMonth");
                modelAndView.addObject(activityList);
                activityTypeList = t.query("Select act_type from activity_type group by act_type", new ActivityTypeRowMapper(), new Object[]{});
                employeeList = t.query("SELECT * FROM EMPLOYEE", new EmployeeRowMapper(), new Object[]{});
                modelAndView.addObject(activityTypeList);
                modelAndView.addObject(employeeList);
                return modelAndView;
            } catch (EmptyResultDataAccessException e) {
                e.printStackTrace();
            }
            Activity activity = new Activity();
            activity.setMonth(month);
            activity.setYear(year);
            return new ModelAndView("activitiesPrMonth", "activity", activity);
        }
        return new ModelAndView("activities");
    }

    class ActivityRowMapper implements org.springframework.jdbc.core.RowMapper<Activity> {


        public Activity mapRow(ResultSet resultSet, int i) throws SQLException {
            ActivityType activityType = new ActivityType();
            activityType.setCategory(resultSet.getString(1));
            activityType.setActivityName(resultSet.getString(2));
            Employee employee = null;
            employee = new Employee(resultSet.getString(3));
            Activity activity = new Activity(activityType, employee, resultSet.getString(4), resultSet.getInt(5));
            return activity;
        }
    }

    class ActivityTypeRowMapper implements org.springframework.jdbc.core.RowMapper<ActivityType> {


        public ActivityType mapRow(ResultSet resultSet, int i) throws SQLException {
            ActivityType activityType = new ActivityType();
            activityType.setCategory(resultSet.getString(1));
            return activityType;
        }
    }

    class EmployeeRowMapper implements org.springframework.jdbc.core.RowMapper<Employee> {


        public Employee mapRow(ResultSet resultSet, int i) throws SQLException {
            Employee e = new Employee(resultSet.getString(1));
            return e;
        }
    }
}