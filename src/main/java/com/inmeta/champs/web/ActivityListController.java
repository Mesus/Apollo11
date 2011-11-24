package com.inmeta.champs.web;

import com.inmeta.champs.model.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


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
            for (String mnd : months) {
                monthActivitites.add(activityRepository.findActivities(year, mnd));
            }

            int[] years = getYears();

            Map<String,List<Champion>> topResults = new HashMap<String, List<Champion>>();

            for (ActivityType activityType : activityTypeList) {
                topResults.put(activityType.getCategory(), getTopList(resultList, activityType.getCategory(), 3));
            }
            List<Employee> employees = activityRepository.findEmployees();
            int employeeSize = employees.size();
            List<Champion> topThreeTotal = findTopThreeTotal(resultList, employeeSize);

            employeeList = sortResult(resultList, category);
            modelAndView.addObject("topThreeTotal", topThreeTotal);
            modelAndView.addObject("topResults", topResults);
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

    /* This method takes a list of ActivityResults, a category and how many to show (top 3? 4? etc). First we get the activityresults that
    *  match the given category. Then we sort that array based on the count paramater, which tells us how many activities are registered.
    *  Then we make a list of Champions, where we set the count, the name of the contestant, the score (which is the place - 1st, 2nd etc)
    *  and the points this place gives. If two champions have the same count of activities, they get the same score and pointsum.
    *  We add the champions to the result, and return it. */
    private List<Champion> getTopList(List<ActivityResult> unsortedList, String category, int toShow) throws IOException, ServletException {
        List<ActivityResult> temp = new ArrayList<ActivityResult>();
        List<Champion> result = new ArrayList<Champion>();

        for (ActivityResult a : unsortedList) {
            if (a.getActivityType().getCategory().equals(category) && a.getCount() > 0 && !(a.getEmployee().getName().equals("Glenn"))) {
                temp.add(a);
            }
        }

        Collections.sort(temp);

        for (int i = 1; i <= temp.size() && i <= toShow; i++) {
            Champion c = new Champion();
            c.setCount(temp.get(i - 1).getCount());
            c.setName(temp.get(i - 1).getEmployee().getName());
            c.setScore(i);
            c.setPoints((toShow-i+1));
            result.add(i - 1, c);
            int j = i;
            while (j < temp.size() && temp.get(i - 1).getCount() == temp.get(j).getCount()) {
                Champion c2 = new Champion();
                c2.setName(temp.get(j).getEmployee().getName());
                c2.setCount(temp.get(j).getCount());
                c2.setScore(i);
                c2.setPoints((toShow-i+1));
                result.add(j, c2);
                j++;
            }
            i = j;
        }
        return result;
    }

    /* This method finds the three champions which have the overall highest scores, by adding scores for each category and sorting. Returns top 3 champions. */
    private List<Champion> findTopThreeTotal(List<ActivityResult> unsortedList, int numberOfContestants) throws IOException, ServletException {
        List<ActivityType> activityTypeList = activityRepository.findActivityTypes();
        List<Champion> championResultList = new ArrayList<Champion>();
        Map<String, List<Champion>> championLists = new HashMap<String, List<Champion>>();
        List<Champion> result = new ArrayList<Champion>();

        for(ActivityType activityType : activityTypeList) {
            championLists.put(activityType.getCategory(), getTopList(unsortedList, activityType.getCategory(), numberOfContestants));
        }

        boolean isInResult;
        for(ActivityType activityType : activityTypeList) {
            for(Champion champion : championLists.get(activityType.getCategory())) {
                isInResult = false;
                for(Champion c : championResultList) {
                    if(c.getName().equals(champion.getName())) {
                        isInResult = true;
                        c.setPoints((c.getPoints() + champion.getPoints()));
                    }
                }
                if(isInResult == false) {
                    championResultList.add(champion);
                }
            }
        }

        Collections.sort(championResultList);

        for(int i = 1; i <= championResultList.size() && i <= 3; i++) {
            championResultList.get(i-1).setScore(i);
            result.add(i-1, championResultList.get(i-1));
            int j = i;
            while(j < championResultList.size() && championResultList.get(i-1).getPoints() == championResultList.get(j).getPoints()) {
                championResultList.get(j).setScore(i);
                result.add(j, championResultList.get(j));
                j++;
            }
            i = j;
        }
        return result;
    }
}



