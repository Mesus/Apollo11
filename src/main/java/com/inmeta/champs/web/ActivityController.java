package com.inmeta.champs.web;

import com.inmeta.champs.model.Activity;
import com.inmeta.champs.model.ActivityType;
import com.inmeta.champs.model.Employee;
import com.inmeta.champs.persistence.ActivityDAO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@org.springframework.stereotype.Controller
public class ActivityController extends BaseController {
    protected final Log logger = LogFactory.getLog(getClass());
    String message = "";

    @RequestMapping("/admin/activitiesPrMonth.htm")
    public ModelAndView getActivitiesView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ModelAndView modelAndView = new ModelAndView("admin/activitiesPrMonth");
        String str_year = request.getParameter("Year");
        int year = Integer.parseInt(str_year);
        String month = request.getParameter("Month");
        modelAndView.addObject("Year", year);
        modelAndView.addObject("Month", month);
        try {
            List<Activity> activityList = activityDao.findActivities(year, month);
            List<ActivityType> activityTypeList = activityDao.findActivityTypes();
            List<Employee> employeeList = activityDao.findEmployees();
            modelAndView.addObject(activityList);
            modelAndView.addObject(activityTypeList);
            modelAndView.addObject(employeeList);
            return modelAndView;
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            message = "Tomt resultat fra databasen.";
        }
        modelAndView.addObject("message", message);
        return modelAndView;
    }

}