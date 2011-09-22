package springapp.web;

import com.gurilunnan.champs.model.Activity;
import com.gurilunnan.champs.model.ActivityType;
import com.gurilunnan.champs.model.Employee;
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
        List<Activity> activities = new ArrayList<Activity>();

        if (request.getRequestURI().equals("/activitiesPrMonth.htm")) {
            int year = Integer.parseInt(request.getParameter("Year"));
            String month = request.getParameter("Month");
            try {
                activities = t.query("SELECT * FROM ACTIVITY WHERE month_name = ? and the_year = ?", new ActivityRowMapper(), new Object[]{month, year});
                return new ModelAndView("activitiesPrMonth", "activities", activities);
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
            ActivityType t = new ActivityType(resultSet.getString(1));
            Employee e = null;
            e = new Employee(resultSet.getString(2));
            Activity activity = new Activity(t, e, resultSet.getString(3), resultSet.getInt(4));
            return activity;
        }
    }
}