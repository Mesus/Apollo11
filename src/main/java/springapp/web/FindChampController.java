package springapp.web;

import com.gurilunnan.champs.model.Champion;
import com.gurilunnan.champs.model.Employee;
import com.sun.org.apache.xpath.internal.operations.And;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
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
 * Date: 20.09.11
 * Time: 15:01
 * To change this template use File | Settings | File Templates.
 */
public class FindChampController implements Controller {
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
        String month = request.getParameter("Month");
        String year = request.getParameter("Year");
        Champion champion = new Champion();
        champion.setYear(year);
        champion.setMonth(month);

        String champName = null;
        try {
            champName = (String) t.queryForObject("Select employee_name from Monthly_champ where month_name =? and the_year =?", String.class, new Object[]{month, year});
            System.out.println(champName);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace(); //TODO Må gjøre om slik at den tar ut en liste, og lese av, pga kan ha fler champion of the month...
        }
        if (champName == null || champName.equals("")) {
            champName = "Ingen";
        }
        Employee e = new Employee(champName);
        champion.setEmployee(e);
        return new ModelAndView("result", "champion", champion);
    }

}

