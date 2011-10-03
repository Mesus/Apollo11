package springapp.web;

import com.gurilunnan.champs.model.Activity;
import com.gurilunnan.champs.persistence.ActivityRepository;
import com.gurilunnan.champs.model.ActivityType;
import com.gurilunnan.champs.model.Employee;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

        ActivityRepository activityRepository = new ActivityRepository();
        ModelAndView modelAndView = new ModelAndView("activitiesPrMonth");

        if (request.getRequestURI().equals("/activitiesPrMonth.htm")) {
            String str_year = request.getParameter("Year");
            int year = Integer.parseInt(str_year);
            String month = request.getParameter("Month");
            try {
                List<Activity> activityList = activityRepository.findActivities(year, month);
                List<ActivityType> activityTypeList = activityRepository.findActivityTypes();
                List<Employee> employeeList = activityRepository.findEmployees();
                modelAndView.addObject(activityList);
                modelAndView.addObject(activityTypeList);
                modelAndView.addObject(employeeList);
                modelAndView.addObject("Year", year);
                modelAndView.addObject("Month", month);
            } catch (EmptyResultDataAccessException e) {
                e.printStackTrace();
            }
        }
        return modelAndView;
    }
}