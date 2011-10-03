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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: gurlunna
 * Date: 27.09.11
 * Time: 11:26
 * To change this template use File | Settings | File Templates.
 */
public class UpdateActivityController implements Controller {
    protected final Log logger = LogFactory.getLog(getClass());

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ActivityRepository activityRepository = new ActivityRepository();
        List<Activity> activityList;
        List<Employee> employeeList;
        List<ActivityType> activityTypeList;
        int year = Integer.parseInt(request.getParameter("Year"));
        String month = request.getParameter("Month");
        String message = "";
        ModelAndView modelAndView = new ModelAndView("activitiesPrMonth");

        if (request.getRequestURI().equals("/updateActivities.htm")) {
            List<String> inputFromTextBoxes = new ArrayList<String>();
            activityList = activityRepository.findActivities(year, month);
            activityTypeList = activityRepository.findActivityTypes();
            employeeList = activityRepository.findEmployees();
            for (ActivityType activityType : activityTypeList) {
                for (Employee employee : employeeList) {
                    String textBoxName = activityType.getCategory() + "," + employee.getName() + "," + month + "," + year;
                    inputFromTextBoxes.add(textBoxName);
                }
            }
            for (Activity a : activityList) {
                String textBoxName = a.getActivityType().getActivityName() + "," + a.getActivityType().getCategory() + "," + a.getEmployee().getName() + "," + a.getMonth() + "," + a.getYear();
                inputFromTextBoxes.add(textBoxName);
            }
            for (String textBoxName : inputFromTextBoxes) {
                String activityName = request.getParameter(textBoxName);
                String[] tmp = textBoxName.split(",");
                if (tmp.length == 5) {
                    if (!(activityName.equals(tmp[0]))) {
                        message = activityRepository.deleteActivity(tmp[0], tmp[2], tmp[3], Integer.parseInt(tmp[4]));
                        boolean registered = false;
                        if (!activityName.equals("")) {
                            List<ActivityType> activityNames = activityRepository.findActivityTypes();
                            for (ActivityType activityType : activityNames) {
                                if (activityType.getActivityName().equals(activityName)) {
                                    registered = true;
                                }
                            }
                            if (!registered) {
                                int isNumeric = 0;
                                int isVisible = 0;
                                for (ActivityType activityType : activityTypeList) {
                                    if (activityType.getCategory().equals(tmp[1])) {
                                        if (activityType.isNumeric()) {
                                            isNumeric = 1;
                                        }
                                        if (activityType.isVisible()) {
                                            isVisible = 1;
                                        }
                                        break;
                                    }
                                }
                                message = activityRepository.insertActivityType(activityName, tmp[1], isNumeric, isVisible);
                            }
                            message = activityRepository.insertActivity(activityName, tmp[2], month, year);
                        }
                    }
                }
                if (!(activityName.equals("")) && !(activityName == null)) {
                    if (tmp.length == 4) {
                        boolean registered = false;
                        activityTypeList = activityRepository.findActivityTypes();
                        for (ActivityType activityType : activityTypeList) {
                            if (activityType.getActivityName().equals(activityName)) {
                                registered = true;
                            }
                        }
                        if (!registered) {
                            int isNumeric = 0;
                            int isVisible = 0;
                            for (ActivityType activityType : activityTypeList) {
                                if (activityType.getCategory().equals(tmp[0])) {
                                    if (activityType.isNumeric()) {
                                        isNumeric = 1;
                                    }
                                    if (activityType.isVisible()) {
                                        isVisible = 1;
                                    }
                                    break;
                                }
                            }
                            message = activityRepository.insertActivityType(activityName, tmp[0], isNumeric, isVisible);
                        }
                        message = activityRepository.insertActivity(activityName, tmp[1], month, year);
                    }
                }
                activityList = activityRepository.findActivities(year, month);
                activityTypeList = activityRepository.findActivityTypes();
            }
            modelAndView.addObject(activityList);
            modelAndView.addObject(activityTypeList);
            modelAndView.addObject(employeeList);
            modelAndView.addObject("Year", year);
            modelAndView.addObject("Month", month);
            modelAndView.addObject("message", message);
        }

        if (request.getRequestURI().equals("/activitiesCancel.htm")) {
            try {
                activityList = activityRepository.findActivities(year, month);
                activityTypeList = activityRepository.findActivityTypes();
                employeeList = activityRepository.findEmployees();
                message = "Canceled changes.";
                modelAndView.addObject(activityList);
                modelAndView.addObject(activityTypeList);
                modelAndView.addObject(employeeList);
                modelAndView.addObject("Year", year);
                modelAndView.addObject("Month", month);
                modelAndView.addObject("message", message);
            } catch (EmptyResultDataAccessException e) {
                e.printStackTrace();
            }
        }

        //Checkboxes sendes bare hvis de er checked, så kan sjekke for om navnet på de fins som parameter for å se om noe skal slettes.
        if (request.getRequestURI().equals("/updateActivityTypes.htm")) {

            activityTypeList = activityRepository.findActivityTypes();
            List<Activity> toBeDeleted = new ArrayList<Activity>();

            for (ActivityType a : activityTypeList) {
                if ((request.getParameter("Delete").equals(a.getCategory()))) {
                    toBeDeleted = activityRepository.findActivities(a.getCategory());
                    if (toBeDeleted != null) {
                        for (Activity activity : toBeDeleted) {
                            message = activityRepository.deleteActivity(activity.getActivityType().getActivityName());
                        }
                    }
                    message = activityRepository.deleteActivityType(a.getCategory());
                }
            }
            activityTypeList = activityRepository.findActivityTypes();
            activityList = activityRepository.findActivities(year, month);
            employeeList = activityRepository.findEmployees();

            modelAndView.addObject(activityList);
            modelAndView.addObject(activityTypeList);
            modelAndView.addObject(employeeList);
            modelAndView.addObject("Month", month);
            modelAndView.addObject("Year", year);
            modelAndView.addObject("message", message);
        }

        if (request.getRequestURI().equals("/addActivityType.htm")) {
            if ((request.getParameter("CategoryName")) != "") {
                int isNumeric = 0;
                int isVisible = 0;
                if ((request.getParameter("number") != null) && (request.getParameter("number").equals("1"))) {
                    isNumeric = 1;
                }
                if ((request.getParameter("visible") != null) && (request.getParameter("visible").equals("1"))) {
                    isVisible = 1;
                }
                message = activityRepository.insertActivityType(request.getParameter("ActivityName"), request.getParameter("CategoryName"), isNumeric, isVisible);
            }
            activityList = activityRepository.findActivities(year, month);
            activityTypeList = activityRepository.findActivityTypes();
            employeeList = activityRepository.findEmployees();
            modelAndView.addObject(activityList);
            modelAndView.addObject(activityTypeList);
            modelAndView.addObject(employeeList);
            modelAndView.addObject("Month", month);
            modelAndView.addObject("Year", year);
            modelAndView.addObject("message", message);
        }

        return modelAndView;
    }
}
