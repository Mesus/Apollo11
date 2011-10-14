package com.inmeta.champs.persistence;

import com.inmeta.champs.model.Activity;
import com.inmeta.champs.model.ActivityResult;
import com.inmeta.champs.model.ActivityType;
import com.inmeta.champs.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ActivityDAO {

    private InitialContext initialContext;

    @Autowired
    private DataSource dataSource;
    private List<Activity> activityList;
    private List<Employee> employeeList;
    private List<ActivityType> activityTypeList;
    private List<ActivityResult> activityResultList;
    private String message = "";

    public ActivityDAO() throws ServletException, IOException {
    }

    public List<Activity> findActivities(int year, String month) {
        try {
            activityList = getJdbcTemplate().query("SELECT t.act_type, a.activity_name, a.employee_name, a.month_name, a.the_year FROM ACTIVITY a LEFT JOIN ACTIVITY_TYPE t ON a.activity_name = t.activity_name WHERE a.month_name = ? AND a.the_year = ?", new ActivityRowMapper(), new Object[]{month, year});
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        return activityList;
    }

    public List<Activity> findActivities(String category) {
        try {
            activityList = getJdbcTemplate().query("SELECT t.act_type, a.activity_name, a.employee_name, a.month_name, a.the_year FROM ACTIVITY a LEFT JOIN ACTIVITY_TYPE t ON a.activity_name = t.activity_name WHERE t.act_type = ?", new ActivityRowMapper(), new Object[]{category});
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        return activityList;
    }

    public List<Employee> findEmployees() {
        try {
            employeeList = getJdbcTemplate().query("SELECT name FROM EMPLOYEE", new EmployeeRowMapper(), new Object[]{});
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        return employeeList;
    }

    public List<ActivityType> findActivityTypes() {
        try {
            activityTypeList = getJdbcTemplate().query("SELECT act_type, isnumeric, isvisible FROM ACTIVITY_TYPE GROUP BY act_type", new ActivityTypeRowMapper(), new Object[]{});
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        return activityTypeList;
    }

    public List<ActivityType> findActivityTypes(boolean isVisible) {
        try {
            activityTypeList = getJdbcTemplate().query("SELECT act_type, isnumeric, isvisible FROM ACTIVITY_TYPE WHERE isvisible = 1 GROUP BY act_type", new ActivityTypeRowMapper(), new Object[]{});
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        return activityTypeList;
    }

    public List<ActivityResult> findActivityResults(int year) {
        try {
            activityResultList = getJdbcTemplate().query("SELECT e.name, a.month_name, t.act_type, a.activity_name, COUNT(a.activity_name), a.the_year FROM EMPLOYEE e JOIN ACTIVITY a, ACTIVITY_TYPE t WHERE e.name = a.employee_name AND a.activity_name = t.activity_name AND a.the_year = ? GROUP BY e.name, t.act_type, a.month_name", new ActivityResultRowMapper(), new Object[]{year});
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        return activityResultList;
    }

    public String deleteActivity(String activityName, String employeeName, String month, int year) {
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
        try {
            getJdbcTemplate().update("INSERT IGNORE INTO ACTIVITY_TYPE VALUES (?, ?, ?, ?)", new Object[]{activityName, category, isNumeric, isVisible});
            message = "Oppdaterte aktiviteter - la til " + category + ". ";
        } catch (DuplicateKeyException e) {
            message = "Feil: duplikat - finnes allerede i databasen.";
        }
        return message;
    }

    public String addActivity(String activityName, String employeeName, String month, int year) {
        try {
            getJdbcTemplate().update("INSERT IGNORE INTO ACTIVITY VALUES (?, ?, ?, ?)", new Object[]{activityName, employeeName, month, year});
            message = "Oppdaterte activiteter - la til " + activityName + " for konsulent " + employeeName + ".";
        } catch (DuplicateKeyException e) {
            message = "Feil: duplikat - finnes allerede i databasen.";
        }
        return message;
    }

    public String addEmployee(String employeeName) {
        try {
            getJdbcTemplate().update("INSERT IGNORE INTO EMPLOYEE VALUES(?)", new Object[]{employeeName});
            message = "Successfully added " + employeeName + " to Employees.";
        } catch (DuplicateKeyException e) {
            message = "Feil: duplikat - finnes allerede i databasen.";
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