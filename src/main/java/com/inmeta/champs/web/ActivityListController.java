package com.inmeta.champs.web;

import com.inmeta.champs.model.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@org.springframework.stereotype.Controller
public class ActivityListController extends BaseController {

    /* Returns the activityList view if the user is authorized, otherwise returns permission denied view. */
    @RequestMapping("/admin/activityList.htm")
    public ModelAndView getAdminActivityListView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (userService.isAuthorized(request, roleAdmin)) {
            return getActivityListView(request, response);
        } else return new ModelAndView("permissionDenied");
    }

    /* Returns the activityList view if the user is authorized, otherwise returns permission denied view.
    *  For chosen year, generate a list of activityresults. Sort by chosen category. Add to view object.
    *  Also get the lists of activities for each month of the chosen year, and add these to the view object.*/
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
            boolean isVisible = true;
            int count;

            List<Employee> employeeList = activityRepository.findEmployees();
            List<ActivityType> activityTypeList = activityRepository.findActivityTypes(isVisible);
            List<ActivityResult> activityResults = activityRepository.findActivityResults(year);

            for (Employee employee : employeeList) {
                for (ActivityType activityType : activityTypeList) {
                    if (activityType.isVisible()) {                                 //We only display the categories which are set to be visible
                        count = 0;
                        for (ActivityResult a : activityResults) {
                            if (employee.getName().equals(a.getEmployee().getName()) && activityType.getCategory().equals(a.getActivityType().getCategory())) {
                                if (activityType.isNumeric()) {
                                    count = count + Integer.parseInt(a.getActivityType().getActivityName());         //If the category is numeric, we need to add the values
                                } else {                                                                             //If the category is not numeric, we only add how many entries there are
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
            String category = request.getParameter("Category");
            if (category == null) {                                                                     //We use the category to sort the result, if no category is chosen we sort on "Konsulent"
                category = "Konsulent";
            }

            String[] months = activityRepository.findReverseMonthList();
            List<List<Activity>> monthActivitites = new ArrayList<List<Activity>>();
            for(String mnd : months) {
                monthActivitites.add(activityRepository.findActivities(year, mnd));
            }

            int[] years = getYears();

            employeeList = sortResult(resultList, category);
            List<Employee> employees = activityRepository.findEmployees();
            modelAndView.addObject(activityTypeList);
            modelAndView.addObject(employeeList);
            modelAndView.addObject("resultList", resultList);
            modelAndView.addObject("Year", year);
            modelAndView.addObject("user", user);
            modelAndView.addObject("Years", years);
            modelAndView.addObject("Months", months);
            modelAndView.addObject("monthActivities", monthActivitites);
            modelAndView.addObject("employees", employees);
            return modelAndView;
        } else return new ModelAndView("permissionDenied");
    }

    /* This method sorts the result descending on the chosen category. The view is generated by the order of the employees, so we return
    *  a list of employees which are sorted in the desired order. The unsortedList is the list of activites for the requested year, and the category
    *  is the category for which we wish to sort. */
    private List<Employee> sortResult(List<ActivityResult> unsortedList, String category) throws IOException, ServletException {
        List<Employee> employeeList = activityRepository.findEmployees();
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



