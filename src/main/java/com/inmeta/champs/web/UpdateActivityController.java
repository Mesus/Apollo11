package com.inmeta.champs.web;

import com.inmeta.champs.model.Activity;
import com.inmeta.champs.model.ActivityType;
import com.inmeta.champs.model.Employee;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Controller
public class UpdateActivityController extends BaseController {

    List<Activity> activityList;
    List<Employee> employeeList;
    List<ActivityType> activityTypeList;
    ModelAndView modelAndView = new ModelAndView("admin/activitiesPrMonth");
    String message = "";
    int year = 0;

    @RequestMapping("/admin/updateActivities.htm")
    public ModelAndView getActivitiesView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("Year") != null) {
            year = Integer.parseInt(request.getParameter("Year"));
        }
        String month = request.getParameter("Month");
        if (year == 0) {
            return new ModelAndView("admin/home");
        }

        List<String> inputFromTextBoxes = new ArrayList<String>();
        activityList = activityDao.findActivities(year, month);
        activityTypeList = activityDao.findActivityTypes();
        employeeList = activityDao.findEmployees();
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
                if (activityName != null && !(activityName.equals(tmp[0]))) {
                    message = activityDao.deleteActivity(tmp[0], tmp[2], tmp[3], Integer.parseInt(tmp[4]));
                    boolean registered = false;
                    if (!activityName.equals("")) {
                        List<ActivityType> activityNames = activityDao.findActivityTypes();
                        for (ActivityType activityType : activityNames) {
                            if (activityType.getActivityName() != null && activityType.getActivityName().equals(activityName)) {
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
                            message = activityDao.addActivityType(activityName, tmp[1], isNumeric, isVisible);
                        }
                        message = activityDao.addActivity(activityName, tmp[2], month, year);
                    }
                }
            }
            if (activityName != null && !(activityName.equals(""))) {
                if (tmp.length == 4) {
                    boolean registered = false;
                    activityTypeList = activityDao.findActivityTypes();
                    for (ActivityType activityType : activityTypeList) {
                        if (activityType.getActivityName() != null && activityType.getActivityName().equals(activityName)) {
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
                        message = activityDao.addActivityType(activityName, tmp[0], isNumeric, isVisible);
                    }
                    message = activityDao.addActivity(activityName, tmp[1], month, year);
                }
            }
            activityList = activityDao.findActivities(year, month);
            activityTypeList = activityDao.findActivityTypes();
        }
        modelAndView.addObject(activityList);
        modelAndView.addObject(activityTypeList);
        modelAndView.addObject(employeeList);
        modelAndView.addObject("Year", year);
        modelAndView.addObject("Month", month);
        modelAndView.addObject("message", message);
        return modelAndView;
    }

    @RequestMapping("/admin/activitiesCancel.htm")
    public ModelAndView getActivitiesCancelView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("Year") != null) {
            year = Integer.parseInt(request.getParameter("Year"));
        }
        String month = request.getParameter("Month");
        if (year == 0) {
            return new ModelAndView("admin/adminhome");
        }

        activityList = activityDao.findActivities(year, month);
        activityTypeList = activityDao.findActivityTypes();
        employeeList = activityDao.findEmployees();
        message = "Canceled changes.";
        modelAndView.addObject(activityList);
        modelAndView.addObject(activityTypeList);
        modelAndView.addObject(employeeList);
        modelAndView.addObject("Year", year);
        modelAndView.addObject("Month", month);
        modelAndView.addObject("message", message);
        return modelAndView;

    }

    @RequestMapping("/admin/updateActivityTypes.htm")
    public ModelAndView getUpdateActivitiesView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("Year") != null) {
            year = Integer.parseInt(request.getParameter("Year"));
        }
        String month = request.getParameter("Month");
        if (year == 0) {
            return new ModelAndView("admin/home");
        }
        activityTypeList = activityDao.findActivityTypes();
        List<Activity> toBeDeleted = new ArrayList<Activity>();

        for (ActivityType a : activityTypeList) {
            if ((request.getParameter("Delete").equals(a.getCategory()))) {
                toBeDeleted = activityDao.findActivities(a.getCategory());
                if (toBeDeleted != null) {
                    for (Activity activity : toBeDeleted) {
                        message = activityDao.deleteActivity(activity.getActivityType().getActivityName());
                    }
                }
                message = activityDao.deleteActivityType(a.getCategory());
            }
        }
        activityTypeList = activityDao.findActivityTypes();
        activityList = activityDao.findActivities(year, month);
        employeeList = activityDao.findEmployees();

        modelAndView.addObject(activityList);
        modelAndView.addObject(activityTypeList);
        modelAndView.addObject(employeeList);
        modelAndView.addObject("Month", month);
        modelAndView.addObject("Year", year);
        modelAndView.addObject("message", message);
        return modelAndView;
    }

    @RequestMapping("/admin/addActivityType.htm")
    public ModelAndView getAddActivityTypeView(HttpServletRequest request, HttpServletResponse response) {
        if (request.getParameter("Year") != null) {
            year = Integer.parseInt(request.getParameter("Year"));
        }
        String month = request.getParameter("Month");
        if (year == 0) {
            return new ModelAndView("admin/home");
        }

        if (!(request.getParameter("CategoryName")).equals("")) {
            int isNumeric = 0;
            int isVisible = 0;
            if ((request.getParameter("number") != null) && (request.getParameter("number").equals("1"))) {
                isNumeric = 1;
            }
            if ((request.getParameter("visible") != null) && (request.getParameter("visible").equals("1"))) {
                isVisible = 1;
            }
            message = activityDao.addActivityType("", request.getParameter("CategoryName"), isNumeric, isVisible);
        }
        activityList = activityDao.findActivities(year, month);
        activityTypeList = activityDao.findActivityTypes();
        employeeList = activityDao.findEmployees();
        modelAndView.addObject(activityList);
        modelAndView.addObject(activityTypeList);
        modelAndView.addObject(employeeList);
        modelAndView.addObject("Month", month);
        modelAndView.addObject("Year", year);
        modelAndView.addObject("message", message);
        return modelAndView;
    }
}


