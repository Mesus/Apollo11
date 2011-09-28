package springapp.web;

import com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import sun.security.x509.CertAttrSet;
import sun.util.calendar.LocalGregorianCalendar;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: gurlunna
 * Date: 21.09.11
 * Time: 12:04
 * To change this template use File | Settings | File Templates.
 */
public class ViewController implements Controller {
    protected final Log logger = LogFactory.getLog(getClass());

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getRequestURI().equals("/update.htm"))
            return new ModelAndView("update");
        if (request.getRequestURI().equals("/champ.htm"))
            return new ModelAndView("champ");

        ModelAndView modelAndView = new ModelAndView("welcome");

        return modelAndView;

    }
}

