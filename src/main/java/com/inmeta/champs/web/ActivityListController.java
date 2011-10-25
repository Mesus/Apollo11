package com.inmeta.champs.web;

import com.inmeta.champs.model.ActivityResult;
import com.inmeta.champs.model.ActivityType;
import com.inmeta.champs.model.Employee;
import com.sun.org.apache.xerces.internal.impl.dv.xs.YearDV;
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

    @RequestMapping("/activityList.htm")
    public ModelAndView getActivityListView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (userService.isAuthorized(request, roleAdmin) || userService.isAuthorized(request, roleMember)) {
            ModelAndView modelAndView = new ModelAndView("activityList");
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
            employeeList = sortResult(resultList, category);
            modelAndView.addObject(activityTypeList);
            modelAndView.addObject(employeeList);
            modelAndView.addObject("resultList", resultList);
            modelAndView.addObject("Year", year);
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

