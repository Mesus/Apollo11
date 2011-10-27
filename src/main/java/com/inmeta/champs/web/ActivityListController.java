package com.inmeta.champs.web;

import com.inmeta.champs.model.*;
import com.sun.org.apache.xerces.internal.impl.dv.xs.YearDV;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@org.springframework.stereotype.Controller
public class ActivityListController extends BaseController {
    List<ActivityResult> activityResults;
    List<Employee> employeeList;
    List<ActivityType> activityTypeList;
    String category;

    @RequestMapping("/admin/activityList.htm")
    public ModelAndView getAdminActivityListView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (userService.isAuthorized(request, roleAdmin)) {
            return getActivityListView(request, response);
        } else return new ModelAndView("permissionDenied");
    }

    @RequestMapping("/activitiesPrMonthList.htm")
    public ModelAndView getActivitiesPrMonth(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        if (userService.isAuthorized(request, roleAdmin) || userService.isAuthorized(request, roleMember)) {
            return getActivityListView(request, response);
        } else return new ModelAndView("permissionDenied");
    }

    @RequestMapping("/admin/activitiesPrMonthList.htm")
    public ModelAndView getAdminActivitiesPrMonth(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        if (userService.isAuthorized(request, roleAdmin)) {
            return getActivityListView(request, response);
        } else return new ModelAndView("permissionDenied");
    }

    @RequestMapping("/activityList.htm")
    public ModelAndView getActivityListView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (userService.isAuthorized(request, roleAdmin) || userService.isAuthorized(request, roleMember)) {
            ModelAndView modelAndView = new ModelAndView("activityList");
            User user = (User) request.getSession().getAttribute("user");
            int year;
            List<ActivityResult> resultList = new ArrayList<ActivityResult>();
            String str_year = request.getParameter("Year");
            if (str_year == null) {
                year = current_year;
            } else {
                year = Integer.parseInt(str_year);
            }
            int year2;
            String str_year2 = request.getParameter("Year2");
            if(str_year2 == null) {
                year2 = year;
            }else {
                year2 = Integer.parseInt(str_year2);
            }

            String month = request.getParameter("Month");
            if(month == null) {
                month = activityRepository.findMonth((current_month -1));
            }

            boolean isVisible = true;
            int count;

            employeeList = activityRepository.findEmployees();
            activityTypeList = activityRepository.findActivityTypes(isVisible);
            activityResults = activityRepository.findActivityResults(year);

            for (Employee employee : employeeList) {
                for (ActivityType activityType : activityTypeList) {
                    if (activityType.isVisible()) {
                        count = 0;
                        for (ActivityResult a : activityResults) {
                            if (employee.getName().equals(a.getEmployee().getName()) && activityType.getCategory().equals(a.getActivityType().getCategory())) {
                                if (activityType.isNumeric()) {
                                    count = count + Integer.parseInt(a.getActivityType().getActivityName());
                                } else {
                                    count = count + a.getCount();
                                }
                            }
                        }
                        ActivityResult activityResult = new ActivityResult();
                        activityResult.setCount(count);
                        activityResult.setActivityType(activityType);
                        activityResult.setEmployee(employee);
                        activityResult.setYear(year);
                        resultList.add(activityResult);
                    }
                }
            }
            category = request.getParameter("Category");
            if (category == null) {
                category = "Konsulent";
            }
            List<Activity> activities = activityRepository.findActivities(year2, month);
            int[] years = getYears();
            String[] months = activityRepository.findMonthList();
            employeeList = sortResult(resultList, category);
            List<Employee> employees = activityRepository.findEmployees();
            modelAndView.addObject(activityTypeList);
            modelAndView.addObject(employeeList);
            modelAndView.addObject("resultList", resultList);
            modelAndView.addObject("Year", year);
            modelAndView.addObject("Year2", year2);
            modelAndView.addObject("user", user);
            modelAndView.addObject("Years", years);
            modelAndView.addObject("Months", months);
            modelAndView.addObject("Month", month);
            modelAndView.addObject("activities", activities);
            modelAndView.addObject("employees", employees);
            return modelAndView;
        } else return new ModelAndView("permissionDenied");
    }

    private List<Employee> sortResult(List<ActivityResult> unsortedList, String category) throws IOException, ServletException {
        employeeList = activityRepository.findEmployees();
        List<ActivityResult> temp = new ArrayList<ActivityResult>();
        List<Employee> result = new ArrayList<Employee>();

        if (category.equals("Konsulent")) {
            return employeeList;
        }
        for (ActivityResult a : unsortedList) {
            if (a.getActivityType().getCategory().equals(category)) {

                temp.add(a);
            }
        }
        Collections.sort(temp);
        for (ActivityResult activityResult : temp) {
            result.add(activityResult.getEmployee());
        }
        return result;
    }
}

