package com.inmeta.champs.persistence;

import com.inmeta.champs.model.User;
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
public class UserRepository {

    private InitialContext initialContext;

    @Autowired
    private DataSource dataSource;
    private List<User> users;
    private List<User> userRequests;

    public UserRepository() throws ServletException, IOException {
    }

    public boolean isRegistered(User user) {
        try {
            users = getJdbcTemplate().query("SELECT email, userrole, username FROM USER", new UserRowMapper(), new Object[]{});
            for (User u : users) {
                if (user.getEmail().equals(u.getEmail())) {
                    user.setRegistered(true);
                }
            }
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return false;
        }
        return user.isRegistered();
    }

    public List<User> getUsers() {
        try {
            users = getJdbcTemplate().query("SELECT email, userrole, username FROM USER", new UserRowMapper(), new Object[]{});
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        return users;
    }

    public User getUser(String email) {
        try {
            users = getJdbcTemplate().query("SELECT email, userrole, username FROM USER WHERE email = ?", new UserRowMapper(), new Object[]{email});
            if (!users.isEmpty()) {
                return users.get(0);
            }
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return null;
    }

    public List<User> getUserRequests() {
        try {
            userRequests = getJdbcTemplate().query("SELECT email, username FROM USER_REQUESTS", new UserRequestRowMapper(), new Object[]{});
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return userRequests;
    }

    public boolean requestAccess(String email, String username) {
        try {
            getJdbcTemplate().update("INSERT IGNORE INTO USER_REQUESTS VALUES (?, ?)", new Object[]{email, username});
            return true;
        } catch (DuplicateKeyException e) {
            return false;
        }
    }

    public boolean addUser(User user) {
        try {
            getJdbcTemplate().update("INSERT IGNORE INTO USER VALUES (?, ?, ?)", new Object[]{user.getEmail(), user.getUserRole(), user.getUsername()});
            return true;
        } catch (DuplicateKeyException e) {
            return false;
        }
    }

    public boolean deleteUser(User user) {
        try {
            getJdbcTemplate().update("DELETE FROM USER WHERE EMAIL = ?", new Object[]{user.getEmail()});
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

    public boolean deleteRequest(User user) {
        try {
            getJdbcTemplate().update("DELETE FROM USER_REQUESTS WHERE EMAIL = ?", new Object[]{user.getEmail()});
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

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

    class UserRequestRowMapper implements RowMapper<User> {
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            User user = new User();
            user.setEmail(resultSet.getString(1));
            user.setUsername(resultSet.getString(2));
            user.setRegistered(false);
            return user;
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