package com.inmeta.champs.persistence;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.inmeta.champs.model.User;

public class UserDAO {
	private InitialContext initialContext;
	private DataSource dataSource;
	private JdbcTemplate t;
	private List<User> users;

	public UserDAO() throws ServletException, IOException {
		try {
			initialContext = new InitialContext();
			dataSource = (DataSource) initialContext.lookup("java:comp/env/jdbc/SimpleDS");

		} catch (NamingException e) {
			throw new ServletException(e);
		}

		t = new JdbcTemplate(dataSource);
	}

	public boolean isRegistered(String email) {
		users = getUsers();
		for(User user : users) {
			if(email.equals(user.getEmail())) {
				return true;
			}
		}
		return false;
	}

	public List<User> getUsers() {
		try {
			users = t.query("Select email, firstname, lastname, userRole, password from User", new UserRowMapper(), new Object[]{});
		}catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
		}
		return users;
	}

}

class UserRowMapper implements RowMapper<User> {
	public User mapRow(ResultSet resultSet, int i) throws SQLException {
		User user = new User();
		user.setEmail(resultSet.getString(1));
		user.setFirstName(resultSet.getString(2));
		user.setLastName(resultSet.getString(3));
		user.setUserRole(resultSet.getString(4));
		user.setPassword(resultSet.getString(5));
		return user;
	}
}