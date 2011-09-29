package springapp.web;

import com.gurilunnan.champs.model.Activity;
import com.gurilunnan.champs.model.ActivityListHelper;
import com.gurilunnan.champs.model.ActivityType;
import com.gurilunnan.champs.model.Employee;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
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
 * Date: 29.09.11
 * Time: 09:06
 * To change this template use File | Settings | File Templates.
 */
public class ActivityListController implements Controller {
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
        List<ActivityListHelper> activityListHelpers;
        List<ActivityListHelper> resultList = new ArrayList<ActivityListHelper>();
        List<Employee> employeeList;
        List<ActivityType> activityTypeList;
        ModelAndView modelAndView = new ModelAndView("activityList");

        String str_year = request.getParameter("Year");
        int year = Integer.parseInt(str_year);

        try {
            employeeList = t.query("SELECT * FROM EMPLOYEE", new EmployeeRowMapper(), new Object[]{});
            activityTypeList = t.query("Select act_type from activity_type group by act_type", new ActivityTypeRowMapper(), new Object[]{});
            activityListHelpers = t.query("select e.name, a.month_name, t.act_type, count(a.activity_name), a.the_year from employee e join activity a, activity_type t where e.name = a.employee_name and a.activity_name = t.activity_name and a.the_year = ? group by e.name, t.act_type, a.month_name", new ActivityListHelperRowMapper(), new Object[]{year});

            int count;
            Employee employee;
            ActivityType activityType;
            for (Employee e : employeeList) {
                employee = e;
                for (ActivityType aType : activityTypeList) {
                    activityType = aType;
                    count = 0;
                    for (ActivityListHelper activityListHelper : activityListHelpers) {
                        if (e.getName().equals(activityListHelper.getEmployee().getName()) && aType.getCategory().equals(activityListHelper.getActivityType().getCategory())) {
                            count = count + activityListHelper.getCount();
                        }
                    }
                    if (count > 0) {
                        ActivityListHelper helper = new ActivityListHelper();
                        helper.setCount(count);
                        helper.setActivityType(activityType);
                        helper.setEmployee(employee);
                        helper.setYear(year);
                        resultList.add(helper);
                    }
                }

            }
            for (ActivityListHelper h : resultList) {
                System.out.println(h.getEmployee().getName() + h.getActivityType().getCategory() + h.getCount());
            }
            modelAndView.addObject(activityTypeList);
            modelAndView.addObject(employeeList);
            modelAndView.addObject("resultList", resultList);
            modelAndView.addObject("Year", year);
            return modelAndView;
        } catch (
                EmptyResultDataAccessException e
                )

        {
            e.printStackTrace();
        }

        return modelAndView;

    }


    class EmployeeRowMapper implements org.springframework.jdbc.core.RowMapper<Employee> {
        public Employee mapRow(ResultSet resultSet, int i) throws SQLException {
            Employee e = new Employee(resultSet.getString(1));
            return e;
        }
    }

    class ActivityTypeRowMapper implements org.springframework.jdbc.core.RowMapper<ActivityType> {
        public ActivityType mapRow(ResultSet resultSet, int i) throws SQLException {
            ActivityType activityType = new ActivityType();
            activityType.setCategory(resultSet.getString(1));
            return activityType;
        }
    }

    class ActivityListHelperRowMapper implements org.springframework.jdbc.core.RowMapper<ActivityListHelper> {

        public ActivityListHelper mapRow(ResultSet resultSet, int i) throws SQLException {
            ActivityListHelper activityListHelper = new ActivityListHelper();
            ActivityType activityType = new ActivityType();
            activityType.setCategory(resultSet.getString(3));
            activityListHelper.setActivityType(activityType);
            Employee employee = new Employee(resultSet.getString(1));
            activityListHelper.setEmployee(employee);
            activityListHelper.setYear(resultSet.getInt(5));
            activityListHelper.setCount(resultSet.getInt(4));
            return activityListHelper;

        }
    }
}
