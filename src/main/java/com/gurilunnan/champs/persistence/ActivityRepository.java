package com.gurilunnan.champs.persistence;

import com.gurilunnan.champs.model.Activity;
import com.gurilunnan.champs.model.ActivityResult;
import com.gurilunnan.champs.model.ActivityType;
import com.gurilunnan.champs.model.Employee;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: gurlunna
 * Date: 03.10.11
 * Time: 09:10
 * To change this template use File | Settings | File Templates.
 */
public class ActivityRepository {
    private InitialContext initialContext;
    private DataSource dataSource;
    private SimpleJdbcTemplate t;
    private List<Activity> activityList;
    private List<Employee> employeeList;
    private List<ActivityType> activityTypeList;
    private List<ActivityResult> activityResultList;
    private String message = "";

    public ActivityRepository() throws ServletException, IOException {
        try {
            initialContext = new InitialContext();
            dataSource = (DataSource) initialContext.lookup("java:comp/env/jdbc/SimpleDS");

        } catch (NamingException e) {
            throw new ServletException(e);
        }

        t = new SimpleJdbcTemplate(dataSource);
    }

    public List<Activity> findActivities(int year, String month) {
        try {
            activityList = t.query("SELECT t.act_type, a.activity_name, a.employee_name, a.month_name, a.the_year FROM ACTIVITY a left join activity_type t on a.activity_name = t.activity_name WHERE a.month_name = ? and a.the_year = ?", new ActivityRowMapper(), new Object[]{month, year});
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        return activityList;
    }

    public List<Activity> findActivities(String category) {
        try {
            activityList = t.query("SELECT t.act_type, a.activity_name, a.employee_name, a.month_name, a.the_year FROM ACTIVITY a left join activity_type t on a.activity_name = t.activity_name WHERE t.act_type = ?", new ActivityRowMapper(), new Object[]{category});
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        return activityList;
    }

    public List<Employee> findEmployees() {
        try {
            employeeList = t.query("SELECT name FROM EMPLOYEE", new EmployeeRowMapper(), new Object[]{});
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        return employeeList;
    }

    public List<ActivityType> findActivityTypes() {
        try {
            activityTypeList = t.query("Select act_type, isnumeric, isvisible_year from activity_type group by act_type", new ActivityTypeRowMapper(), new Object[]{});
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        return activityTypeList;
    }

    public List<ActivityType> findActivityTypes(boolean isVisible) {
        try {
            activityTypeList = t.query("Select act_type, isnumeric, isvisible_year from activity_type where isvisible_year = 1 group by act_type", new ActivityTypeRowMapper(), new Object[]{});
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        return activityTypeList;
    }

    public List<ActivityResult> findActivityResults(int year) {
        try {
            activityResultList = t.query("select e.name, a.month_name, t.act_type, a.activity_name, count(a.activity_name), a.the_year from employee e join activity a, activity_type t where e.name = a.employee_name and a.activity_name = t.activity_name and a.the_year = ? group by e.name, t.act_type, a.month_name", new ActivityResultRowMapper(), new Object[]{year});
        }catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        return activityResultList;
    }

    public String deleteActivity(String activityName, String employeeName, String month, int year) {
        try {
            t.update("delete from activity where activity_name = ? and employee_name = ? and month_name = ? and the_year = ?", new Object[]{activityName, employeeName, month, year});
            message = "Slettet aktivitet " + activityName + " for " + employeeName + ".";
        } catch (DataIntegrityViolationException e) {
            message = "Cannot delete " + activityName + " for employee " + employeeName + " from the database.";
            e.printStackTrace();
        }
        return message;
    }

    public String deleteActivity(String activityName) {
        try {
            t.update("Delete from Activity where activity_name = ?", new Object[]{activityName});
            message = "Slettet aktivitet " + activityName + ".";
        } catch (DataIntegrityViolationException e) {
            message = "Cannot delete " + activityName + " from the database.";
            e.printStackTrace();
        }
        return message;
    }

    public String deleteActivityType(String category) {
        try {
            t.update("Delete from activity_type where act_type = ?", new Object[]{category});
            message = "Successfully deleted the category " + category + " from the database.";
        } catch (DataIntegrityViolationException e) {
            message = "Cannot delete " + category + " from the database.";
            e.printStackTrace();
        }
        return message;
    }

    public String deleteEmployee(String employeeName) {
        try {
            t.update("Delete from activity where employee_name = ?", new Object[] {employeeName});
            t.update("Delete from employee where name = ?", new Object[] {employeeName});
            message = "Deleted " + employeeName + " from the database.";
        } catch (DataIntegrityViolationException e) {
            message = "Cannot delete " + employeeName + " from the database.";
            e.printStackTrace();
        }
        return message;
    }

    public String addActivityType(String activityName, String category, int isNumeric, int isVisible) {
        try {
            t.update("insert into activity_type values (?, ?, ?, ?)", new Object[]{activityName, category, isNumeric, isVisible});
            message = "Oppdaterte aktiviteter - la til " + activityName + ". ";
        } catch (DuplicateKeyException e) {
            message = "Error - finnes allerede i databasen.";
            e.printStackTrace();
        }
        return message;
    }

    public String addActivity(String activityName, String employeeName, String month, int year) {
        try {
            t.update("insert into activity values (?, ?, ?, ?)", new Object[]{activityName, employeeName, month, year});
            message = "Oppdaterte activiteter - la til " + activityName + " for konsulent " + employeeName + ".";
        } catch (DuplicateKeyException e) {
            message = "Error - finnes allerede i databasen.";
            e.printStackTrace();
        }
        return message;
    }

    public String addEmployee(String employeeName) {
        try {
            t.update("insert into Employee values(?)", new Object[]{employeeName});
            message = "Successfully added " + employeeName + " to Employees.";
        } catch (DuplicateKeyException e) {
            message = "Error - finnes allerede i databasen.";
            e.printStackTrace();
        }
        return message;
    }




    class ActivityRowMapper implements org.springframework.jdbc.core.RowMapper<Activity> {
        public Activity mapRow(ResultSet resultSet, int i) throws SQLException {
            ActivityType activityType = new ActivityType();
            activityType.setCategory(resultSet.getString(1));
            activityType.setActivityName(resultSet.getString(2));
            Employee employee = new Employee(resultSet.getString(3));
            Activity activity = new Activity(activityType, employee, resultSet.getString(4), resultSet.getInt(5));
            return activity;
        }
    }

    class ActivityTypeRowMapper implements org.springframework.jdbc.core.RowMapper<ActivityType> {
        public ActivityType mapRow(ResultSet resultSet, int i) throws SQLException {
            ActivityType activityType = new ActivityType();
            activityType.setCategory(resultSet.getString(1));
            boolean numeric = false;
            boolean visible = false;
            if (resultSet.getInt(2) == 1) {
                numeric = true;
            }
            if (resultSet.getInt(3) == 1) {
                visible = true;
            }
            activityType.setVisible(visible);
            activityType.setNumeric(numeric);
            return activityType;
        }
    }

    class EmployeeRowMapper implements org.springframework.jdbc.core.RowMapper<Employee> {
        public Employee mapRow(ResultSet resultSet, int i) throws SQLException {
            Employee e = new Employee(resultSet.getString(1));
            return e;
        }
    }

    class ActivityResultRowMapper implements org.springframework.jdbc.core.RowMapper<ActivityResult> {
        public ActivityResult mapRow(ResultSet resultSet, int i) throws SQLException {
            ActivityResult activityResult = new ActivityResult();
            ActivityType activityType = new ActivityType();
            activityType.setCategory(resultSet.getString(3));
            activityType.setActivityName(resultSet.getString(4));
            activityResult.setActivityType(activityType);
            Employee employee = new Employee(resultSet.getString(1));
            activityResult.setEmployee(employee);
            activityResult.setYear(resultSet.getInt(6));
            activityResult.setCount(resultSet.getInt(5));
            return activityResult;
        }
    }
}