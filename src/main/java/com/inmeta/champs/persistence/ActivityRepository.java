package com.inmeta.champs.persistence;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.inmeta.champs.model.Activity;
import com.inmeta.champs.model.ActivityResult;
import com.inmeta.champs.model.ActivityType;
import com.inmeta.champs.model.Employee;
import com.inmeta.champs.model.MonthRepresentation;

@Repository
public class ActivityRepository {

	private JdbcTemplate jdbcTemplate;

	public ActivityRepository() throws ServletException, IOException {
	}

	/* This method returns a list with all the activities for the given month+year. */
	public List<Activity> findActivities(int year, String month) {
		try {
			List<Activity> activityList = jdbcTemplate
					.query("SELECT t.act_type, a.activity_name, a.employee_name, a.month_name, a.the_year FROM ACTIVITY a LEFT JOIN ACTIVITY_TYPE t ON a.activity_name = t.activity_name WHERE a.month_name = ? AND a.the_year = ?",
							new ActivityRowMapper(), new Object[] { month, year });
			return activityList;
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	/* This method returns all the activities with the given category. */
	public List<Activity> findActivities(String category) {
		try {
			List<Activity> activityList = jdbcTemplate
					.query("SELECT t.act_type, a.activity_name, a.employee_name, a.month_name, a.the_year FROM ACTIVITY a LEFT JOIN ACTIVITY_TYPE t ON a.activity_name = t.activity_name WHERE t.act_type = ?",
							new ActivityRowMapper(), new Object[] { category });
			return activityList;
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/* This method returns a list of all the employees. */
	public List<Employee> findEmployees() {
		try {
			List<Employee> employeeList = jdbcTemplate.query("SELECT name FROM EMPLOYEE", new EmployeeRowMapper(), new Object[] {});
			return employeeList;
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/* This method returns a list of all the activity categories. */
	public List<ActivityType> findActivityTypes() {
		try {
			List<ActivityType> activityTypeList = jdbcTemplate.query("SELECT act_type, isnumeric, isvisible FROM ACTIVITY_TYPE GROUP BY act_type",
					new ActivityTypeRowMapper(), new Object[] {});
			return activityTypeList;
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/* This method returns a list of categories with visible=1, which means they should be visible in the activityList view. */
	public List<ActivityType> findActivityTypes(boolean isVisible) {
		try {
			List<ActivityType> activityTypeList = jdbcTemplate.query(
					"SELECT act_type, isnumeric, isvisible FROM ACTIVITY_TYPE WHERE isvisible = 1 GROUP BY act_type", new ActivityTypeRowMapper(),
					new Object[] {});
			return activityTypeList;
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/* This method returns a list of activityresults, for a given year. */
	public List<ActivityResult> findActivityResults(int year) {
		try {
			List<ActivityResult> activityResultList = jdbcTemplate
					.query("SELECT e.name, a.month_name, t.act_type, a.activity_name, COUNT(a.activity_name), a.the_year FROM EMPLOYEE e JOIN ACTIVITY a, ACTIVITY_TYPE t WHERE e.name = a.employee_name AND a.activity_name = t.activity_name AND a.the_year = ? GROUP BY e.name, t.act_type, a.month_name",
							new ActivityResultRowMapper(), new Object[] { year });
			return activityResultList;
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/* This method deletes an activity with given name for given employee, month and year, and returns a status message. */
	public String deleteActivity(String activityName, String employeeName, String month, int year) {
		String message = "";
		try {
			jdbcTemplate.update("DELETE FROM ACTIVITY WHERE activity_name = ? AND employee_name = ? AND month_name = ? AND the_year = ?", new Object[] {
					activityName, employeeName, month, year });
			message = "Slettet aktivitet " + activityName + " for " + employeeName + ".";
		} catch (DataIntegrityViolationException e) {
			message = "Kunne ikke slette " + activityName + " for konsulent " + employeeName + " fra databasen.";
			e.printStackTrace();
		}
		return message;
	}

	/* This method deletes an activity with given name and returns a status message. */
	public String deleteActivity(String activityName) {
		String message = "";
		try {
			jdbcTemplate.update("DELETE FROM ACTIVITY WHERE activity_name = ?", new Object[] { activityName });
			message = "Slettet aktivitet " + activityName + ".";
		} catch (DataIntegrityViolationException e) {
			message = "Kunne ikke slette " + activityName + " fra databasen.";
			e.printStackTrace();
		}
		return message;
	}

	/* This method deletes a category with given name and returns a status message. */
	public String deleteActivityType(String category) {
		String message = "";
		try {
			jdbcTemplate.update("DELETE FROM ACTIVITY_TYPE WHERE act_type = ?", new Object[] { category });
			message = "Successfully deleted the category " + category + " from the database.";
		} catch (DataIntegrityViolationException e) {
			message = "Kunne ikke slette " + category + " fra databasen.";
			e.printStackTrace();
		}
		return message;
	}

	/* This method deletes an employee with given name, and all related activities. The method returns a status message. */
	public String deleteEmployee(String employeeName) {
		String message = "";
		try {
			jdbcTemplate.update("DELETE FROM ACTIVITY WHERE employee_name = ?", new Object[] { employeeName });
			jdbcTemplate.update("DELETE FROM EMPLOYEE WHERE name = ?", new Object[] { employeeName });
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
			jdbcTemplate.update("INSERT IGNORE INTO ACTIVITY_TYPE VALUES (?, ?, ?, ?)", new Object[] { activityName, category, isNumeric, isVisible });
			message = "Oppdaterte aktiviteter - la til " + category + ". ";
		} catch (DuplicateKeyException e) {
			message = "Feil: duplikat - finnes allerede i databasen.";
		}
		return message;
	}

	/* This method adds an activity to the database and returns a status message. */
	public String addActivity(String activityName, String employeeName, String month, int year) {
		String message = "";
		try {
			jdbcTemplate.update("INSERT IGNORE INTO ACTIVITY VALUES (?, ?, ?, ?)", new Object[] { activityName, employeeName, month, year });
			message = "Oppdaterte activiteter - la til " + activityName + " for konsulent " + employeeName + ".";
		} catch (DuplicateKeyException e) {
			message = "Feil: duplikat - finnes allerede i databasen.";
		}
		return message;
	}

	/* This method adds an employee to the database and returns a status message. */
	public String addEmployee(String employeeName) {
		String message = "";
		try {
			jdbcTemplate.update("INSERT IGNORE INTO EMPLOYEE VALUES(?)", new Object[] { employeeName });
			message = "Successfully added " + employeeName + " to Employees.";
		} catch (DuplicateKeyException e) {
			message = "Feil: duplikat - finnes allerede i databasen.";
		}
		return message;
	}

	/* This method changes the name of a category, and returns a status message. */
	public String changeCategoryName(String oldname, String newname) {
		String message = "";
		try {
			jdbcTemplate.update("UPDATE ACTIVITY_TYPE SET act_type=? WHERE act_type=?", newname, oldname);
			message = "Changed category name " + oldname + " to " + newname + ".";
		} catch (Exception e) {
			message = "Could not change the category name.";
		}
		return message;
	}

	/* This method returns the month name for given month number. */
	public String findMonth(int month) {
		try {
			return jdbcTemplate.queryForObject("SELECT month_name FROM MONTH WHERE month_number=?", String.class, new Object[] { month });
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/* This method returns a list of month names sorted from january to december. */
	public String[] findMonthList() {
		try {
			List<MonthRepresentation> months = jdbcTemplate.query("SELECT month_name FROM MONTH ORDER BY month_number ASC", new MonthRowMapper());
			String[] strings = new String[12];
			for (int i = 0; i < months.size(); i++) {
				strings[i] = months.get(i).getMonth_name();
			}
			return strings;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/* This method returns a list of month names sorted from december and backwards to january. */
	public String[] findReverseMonthList() {
		try {
			List<MonthRepresentation> months = jdbcTemplate.query("SELECT month_name FROM MONTH ORDER BY month_number DESC", new MonthRowMapper());
			String[] strings = new String[12];
			for (int i = 0; i < months.size(); i++) {
				strings[i] = months.get(i).getMonth_name();
			}
			return strings;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/* This class maps rows in the result set to Activity objects. */
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

	/* This class maps rows in the result set to ActivityType objects. */
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

	/* This class maps rows in the result set to Employee objects. */
	class EmployeeRowMapper implements org.springframework.jdbc.core.RowMapper<Employee> {
		public Employee mapRow(ResultSet resultSet, int i) throws SQLException {
			Employee e = new Employee(resultSet.getString(1));
			return e;
		}
	}

	/* This class maps rows in the result set to ActivityResult objects. */
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

	/* This class maps rows in the result set to MonthRepresentation objects. */
	class MonthRowMapper implements RowMapper<MonthRepresentation> {
		public MonthRepresentation mapRow(ResultSet resultSet, int i) throws SQLException {
			MonthRepresentation monthRepresentation = new MonthRepresentation();
			monthRepresentation.setMonth_name(resultSet.getString(1));
			return monthRepresentation;
		}
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}