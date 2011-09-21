package com.gurilunnan.champs.persistence;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Helper class for JDBC
 *
 * @author Glenn Bech
 */

public abstract class JDBCTemplate {

    static DataSource ds;

    public abstract void execute(Connection c) throws SQLException;

    public void execute() {

        Connection c = null;
        try {
            c = getConnection();
            execute(c);
            c.close();
        } catch (NamingException e) {
            throw new JDBCTemplateException(e.getMessage(), e);
        } catch (SQLException e) {
            throw new JDBCTemplateException(e.getMessage(), e);
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Connection getConnection() throws NamingException, SQLException {
        if (ds == null) {
            InitialContext context = new InitialContext();
            ds = (DataSource) context.lookup("java:comp/env/jdbc/AnsattDS");
        }
        return ds.getConnection();
    }

    static class JDBCTemplateException extends RuntimeException {

        JDBCTemplateException() {
        }

        JDBCTemplateException(String message) {
            super(message);
        }

        JDBCTemplateException(String message, Throwable cause) {
            super(message, cause);
        }

        JDBCTemplateException(Throwable cause) {
            super(cause);
        }
    }

}