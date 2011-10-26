package com.inmeta.champs.persistence;

import com.inmeta.champs.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ActivityRepository {

    private InitialContext initialContext;

    @Autowired
    private DataSource dataSource;


    public ActivityRepository() throws ServletException, IOException {
    }

    public List<Activity> findActivities(int year, String month) {
        try {
            List<Activity> activityList = getJdbcTemplate().query("SELECT t.act_type, a.activity_name, a.employee_name, a.month_name, a.the_year FROM ACTIVITY a LEFT JOIN ACTIVITY_TYPE t ON a.activity_name = t.activity_name WHERE a.month_name = ? AND a.the_year = ?", new ActivityRowMapper(), new Object[]{month, year});
            return activityList;
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Activity> findActivities(String category) {
        try {
            List<Activity> activityList = getJdbcTemplate().query("SELECT t.act_type, a.activity_name, a.employee_name, a.month_name, a.the_year FROM ACTIVITY a LEFT JOIN ACTIVITY_TYPE t ON a.activity_name = t.activity_name WHERE t.act_type = ?", new ActivityRowMapper(), new Object[]{category});
            return activityList;
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Employee> findEmployees() {
        try {
            List<Employee> employeeList = getJdbcTemplate().query("SELECT name FROM EMPLOYEE", new EmployeeRowMapper(), new Object[]{});
            return employeeList;
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ActivityType> findActivityTypes() {
        try {
            List<ActivityType> activityTypeList = getJdbcTemplate().query("SELECT act_type, isnumeric, isvisible FROM ACTIVITY_TYPE GROUP BY act_type", new ActivityTypeRowMapper(), new Object[]{});
            return activityTypeList;
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ActivityType> findActivityTypes(boolean isVisible) {
        try {
            List<ActivityType> activityTypeList = getJdbcTemplate().query("SELECT act_type, isnumeric, isvisible FROM ACTIVITY_TYPE WHERE isvisible = 1 GROUP BY act_type", new ActivityTypeRowMapper(), new Object[]{});
            return activityTypeList;
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ActivityResult> findActivityResults(int year) {
        try {
            List<ActivityResult> activityResultList = getJdbcTemplate().query("SELECT e.name, a.month_name, t.act_type, a.activity_name, COUNT(a.activity_name), a.the_year FROM EMPLOYEE e JOIN ACTIVITY a, ACTIVITY_TYPE t WHERE e.name = a.employee_name AND a.activity_name = t.activity_name AND a.the_year = ? GROUP BY e.name, t.act_type, a.month_name", new ActivityResultRowMapper(), new Object[]{year});
            return activityResultList;
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String deleteActivity(String activityName, String employeeName, String month, int year) {
        String message = "";
        try {
            getJdbcTemplate().update("DELETE FROM ACTIVITY WHERE activity_name = ? AND employee_name = ? AND month_name = ? AND the_year = ?", new Object[]{activityName, employeeName, month, year});
            message = "Slettet aktivitet " + activityName + " for " + employeeName + ".";
        } catch (DataIntegrityViolationException e) {
            message = "Kunne ikke slette " + activityName + " for konsulent " + employeeName + " fra databasen.";
            e.printStackTrace();
        }
        return message;
    }

    public String deleteActivity(String activityName) {
        String message = "";
        try {
            getJdbcTemplate().update("DELETE FROM ACTIVITY WHERE activity_name = ?", new Object[]{activityName});
            message = "Slettet aktivitet " + activityName + ".";
        } catch (DataIntegrityViolationException e) {
            message = "Kunne ikke slette " + activityName + " fra databasen.";
            e.printStackTrace();
        }
        return message;
    }

    public String deleteActivityType(String category) {
        String message = "";
        try {
            getJdbcTemplate().update("DELETE FROM ACTIVITY_TYPE WHERE act_type = ?", new Object[]{category});
            message = "Successfully deleted the category " + category + " from the database.";
        } catch (DataIntegrityViolationException e) {
            message = "Kunne ikke slette " + category + " fra databasen.";
            e.printStackTrace();
        }
        return message;
    }

    public String deleteEmployee(String employeeName) {
        String message = "";
        try {
            getJdbcTemplate().update("DELETE FROM ACTIVITY WHERE employee_name = ?", new Object[]{employeeName});
            getJdbcTemplate().update("DELETE FROM EMPLOYEE WHERE name = ?", new Object[]{employeeName});
            message = "Deleted " + employeeName + " from the database.";
        } catch (DataIntegrityViolationException e) {
            message = "Kunne ikke slette " + employeeName + " fra databasen.";
            e.printStackTrace();
        }
        return message;
    }

    public String addActivityType(String activityName, String category, int isNumeric, int isVisible) {
        String message = "";
        try {
            getJdbcTemplate().update("INSERT IGNORE INTO ACTIVITY_TYPE VALUES (?, ?, ?, ?)", new Object[]{activityName, category, isNumeric, isVisible});
            message = "Oppdaterte aktiviteter - la til " + category + ". ";
        } catch (DuplicateKeyException e) {
            message = "Feil: duplikat - finnes allerede i databasen.";
        }
        return message;
    }

    public String addActivity(String activityName, String employeeName, String month, int year) {
        String message = "";
        try {
            getJdbcTemplate().update("INSERT IGNORE INTO ACTIVITY VALUES (?, ?, ?, ?)", new Object[]{activityName, employeeName, month, year});
            message = "Oppdaterte activiteter - la til " + activityName + " for konsulent " + employeeName + ".";
        } catch (DuplicateKeyException e) {
            message = "Feil: duplikat - finnes allerede i databasen.";
        }
        return message;
    }

    public String addEmployee(String employeeName) {
        String message = "";
        try {
            getJdbcTemplate().update("INSERT IGNORE INTO EMPLOYEE VALUES(?)", new Object[]{employeeName});
            message = "Successfully added " + employeeName + " to Employees.";
        } catch (DuplicateKeyException e) {
            message = "Feil: duplikat - finnes allerede i databasen.";
        }
        return message;
    }

    public String changeCategoryName(String oldname, String newname) {
        String message = "";
        try {
            getJdbcTemplate().update("UPDATE ACTIVITY_TYPE SET act_type=? WHERE act_type=?", newname, oldname);
            message = "Changed category name " + oldname + " to " + newname + ".";
        } catch (Exception e) {
            message = "Could not change the category name.";
        }
        return message;
    }

    public String findMonth(int month) {
        try {
            return getJdbcTemplate().queryForObject("SELECT month_name FROM MONTH WHERE month_number=?", String.class, new Object[]{month});
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public String[] findMonthList() {
        try {
            List<MonthRepresentation> months = getJdbcTemplate().query("SELECT month_name FROM MONTH ORDER BY month_number ASC", new MonthRowMapper());
            String[] strings = new String[12];
            for (int i = 0; i < months.size(); i++) {
                strings[i] = months.get(i).getMonth_name();
            }
            return strings;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
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

    class MonthRowMapper implements RowMapper<MonthRepresentation> {
        public MonthRepresentation mapRow(ResultSet resultSet, int i) throws SQLException {
            MonthRepresentation monthRepresentation = new MonthRepresentation();
            monthRepresentation.setMonth_name(resultSet.getString(1));
            return monthRepresentation;
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }


}