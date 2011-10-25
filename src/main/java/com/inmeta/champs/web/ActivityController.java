package com.inmeta.champs.web;

import com.inmeta.champs.model.Activity;
import com.inmeta.champs.model.ActivityType;
import com.inmeta.champs.model.Employee;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@org.springframework.stereotype.Controller
public class ActivityController extends BaseController {
    protected final Log logger = LogFactory.getLog(getClass());

    @RequestMapping("/admin/activitiesPrMonth.htm")
    public ModelAndView getActivitiesView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (userService.isAuthorized(request, roleAdmin)) {
            ModelAndView modelAndView = new ModelAndView("admin/activitiesPrMonth");
            String str_year = request.getParameter("Year");
            int year;
            if(str_year == null) {
                year = current_year;
            } else {
                year = Integer.parseInt(str_year);
            }
            String month = request.getParameter("Month");
            if(month == null) {
                String this_month = activityRepository.findMonth(current_month);
                month = this_month;
            }
            int[] years = getYears();
            String[] months = activityRepository.findMonthList();
            modelAndView.addObject("Year", year);
            modelAndView.addObject("Month", month);
            List<Activity> activityList = activityRepository.findActivities(year, month);
            List<ActivityType> activityTypeList = activityRepository.findActivityTypes();
            List<Employee> employeeList = activityRepository.findEmployees();
            modelAndView.addObject(activityList);
            modelAndView.addObject(activityTypeList);
            modelAndView.addObject(employeeList);
            modelAndView.addObject("Years", years);
            modelAndView.addObject("Months", months);
            return modelAndView;
        } else return new ModelAndView("permissionDenied");
    }

}