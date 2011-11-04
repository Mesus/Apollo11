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
import java.util.List;


@org.springframework.stereotype.Controller
public class ActivityController extends BaseController {

    /* This method returns the activitiesPrMonth view, where the administrator can edit activities and categories.
    *  First check if the user is authorized - if not return the permission denied view.
    *  We use the month and year the user chose in the admin/home view, to get activities from that month and year and add to a list of activities.
    *  If we somehow get to this view directly, use the current month and year as a default.
    *  We add a list of activitytypes, employees and the activities for the chosen month+year, to the ModelAndView, and then return the ModelAndView. */

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