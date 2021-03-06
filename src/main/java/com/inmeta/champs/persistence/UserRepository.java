package com.inmeta.champs.persistence;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.inmeta.champs.model.User;

@Repository
public class UserRepository {

	// private InitialContext initialContext;

	private JdbcTemplate jdbcTemplate;

	public UserRepository() throws ServletException, IOException {
	}

	/*
	 * This method checks if the user is registered in the database. Returns true if the user is registered.
	 */
	public boolean isRegistered(User user) {
		try {
			jdbcTemplate.setQueryTimeout(30);
			User u = jdbcTemplate.queryForObject("SELECT email, userrole, username FROM USER WHERE email = ?", new UserRowMapper(), user.getEmail());
			if (u != null) {
				return true;
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return false;
	}

	/* This method returns a list of all the users. */
	public List<User> getUsers() {
		try {
			jdbcTemplate.setQueryTimeout(30);
			List<User> users = jdbcTemplate.query("SELECT email, userrole, username FROM USER", new UserRowMapper(), new Object[] {});
			return users;
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * This method returns the user with given email address. (The method returns null if it can't find any such user.)
	 */
	public User getUser(String email) {
		return DataAccessUtils.uniqueResult(jdbcTemplate.query("SELECT email, userrole, username FROM USER WHERE email = ?", new UserRowMapper(),
				new Object[] { email }));
	}

	/*
	 * This method returns a list of users who have requested access to the webpage.
	 */
	public List<User> getUserRequests() {
		try {
			jdbcTemplate.setQueryTimeout(30);
			List<User> userRequests = jdbcTemplate.query("SELECT email, username FROM USER_REQUESTS", new UserRequestRowMapper(), new Object[] {});
			return userRequests;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/*
	 * This method stores a request for access in the database, and returns true if successful.
	 */
	public boolean requestAccess(String email, String username) {
		try {
			jdbcTemplate.update("INSERT IGNORE INTO USER_REQUESTS VALUES (?, ?)", new Object[] { email, username });
			return true;
		} catch (DuplicateKeyException e) {
			return false;
		}
	}

	/* This method adds a user to the database and returns true if successful. */
	public boolean addUser(User user) {
		try {
			jdbcTemplate.update("INSERT IGNORE INTO USER VALUES (?, ?, ?)", new Object[] { user.getEmail(), user.getUserRole(), user.getUsername() });
			return true;
		} catch (DuplicateKeyException e) {
			return false;
		}
	}

	/*
	 * This method deletes a user from the database and returns true if successful.
	 */
	public boolean deleteUser(User user) {
		try {
			jdbcTemplate.update("DELETE FROM USER WHERE EMAIL = ?", new Object[] { user.getEmail() });
			return true;
		} catch (DataIntegrityViolationException e) {
			return false;
		}
	}

	/*
	 * This method deletes a request for access from the database and returns true if successful.
	 */
	public boolean deleteRequest(User user) {
		try {
			jdbcTemplate.update("DELETE FROM USER_REQUESTS WHERE EMAIL = ?", new Object[] { user.getEmail() });
			return true;
		} catch (DataIntegrityViolationException e) {
			return false;
		}
	}

	/* This class maps a row in the result set to a User object. */
	class UserRowMapper implements RowMapper<User> {
		public User mapRow(ResultSet resultSet, int i) throws SQLException {
			User user = new User();
			user.setEmail(resultSet.getString(1));
			user.setUserRole(resultSet.getString(2));
			user.setUsername(resultSet.getString(3));
			user.setRegistered(true);
			return user;
		}
	}

	/*
	 * This class maps a row in the result set to a User object, use this mapper if the user role is undetermined.
	 */
	class UserRequestRowMapper implements RowMapper<User> {
		public User mapRow(ResultSet resultSet, int i) throws SQLException {
			User user = new User();
			user.setEmail(resultSet.getString(1));
			user.setUsername(resultSet.getString(2));
			user.setRegistered(false);
			return user;
		}
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}