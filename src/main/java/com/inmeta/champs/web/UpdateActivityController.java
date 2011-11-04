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


    /* This method gets the input from the activity table in the view, and updates the activities for the employee + category where there were changes. */
    @RequestMapping("/admin/updateActivities.htm")
    public ModelAndView getActivitiesView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (userService.isAuthorized(request, roleAdmin)) {
            int year = 0;
            if (request.getParameter("Year") != null) {
                year = Integer.parseInt(request.getParameter("Year"));
            }
            String month = request.getParameter("Month");
            if (year == 0) {
                return new ModelAndView("admin/home");
            }

            List<String> inputFromTextBoxes = new ArrayList<String>();
            List<Activity> activityList = activityRepository.findActivities(year, month);
            List<ActivityType> activityTypeList = activityRepository.findActivityTypes();
            List<Employee> employeeList = activityRepository.findEmployees();
            String message = "";

            /* The name of the empty textboxes in the view are named like this: "category,employee name,month,year"
            *  To get the input from the textboxes, we need a list of the textbox names. */
            for (ActivityType activityType : activityTypeList) {
                for (Employee employee : employeeList) {
                    String textBoxName = activityType.getCategory() + "," + employee.getName() + "," + month + "," + year;
                    inputFromTextBoxes.add(textBoxName);
                }
            }

            /* The name of the textboxes with values (activity names) in the view are named like this: "activity name,category,employee name,month,year"
            *  To get the input from these textboxes as well, we need to add these names to the list */
            for (Activity a : activityList) {
                String textBoxName = a.getActivityType().getActivityName() + "," + a.getActivityType().getCategory() + "," + a.getEmployee().getName() + "," + a.getMonth() + "," + a.getYear();
                inputFromTextBoxes.add(textBoxName);
            }

            /* For every textbox name we check if the textbox was filled with some value.*/
            for (String textBoxName : inputFromTextBoxes) {
                String activityName = request.getParameter(textBoxName);
                String[] tmp = textBoxName.split(",");

                /* We split the textbox name into a string array, if the name has 5 parameters it's a field that already had some value (activity name).
                *  We check that the textbox still has some value, by checking that the activityName parameter != null.
                *  We also check that it actually has been changed, by checking that it's not equal to the first part of the textbox name.
                *  If the value has been changed, the old value is deleted from the database.
                *  We then check if the activity name already exists in the activity type table, if not we add it. The database needs this to determine which activity belongs to which category.
                *  The boolean variable registered is true if the activity name already exists in the activity type table.
                *  If the activity is not registered, we register it under the right category, and then we add it to the activity table as well.*/
                if (tmp.length == 5) {
                    if (activityName != null && !(activityName.equals(tmp[0]))) {
                        message = activityRepository.deleteActivity(tmp[0], tmp[2], tmp[3], Integer.parseInt(tmp[4]));
                        boolean registered = false;
                        if (!activityName.equals("")) {
                            List<ActivityType> activityNames = activityRepository.findActivityTypes();
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
                                message = activityRepository.addActivityType(activityName, tmp[1], isNumeric, isVisible);
                            }
                            message = activityRepository.addActivity(activityName, tmp[2], month, year);
                        }
                    }
                }

                /* If the activity name isn't empty (it has some value), and the box it was added to was an empty box:
                *  The textbox name will have 4 parts. (tmp.lenght ==4)
                *  If the activity name already exists in the activity type table, set registered to true.
                *  If not, add the activity name first to the activity type table under the correct category.
                *  Then add the activity to the activities table.*/
                if (activityName != null && !(activityName.equals(""))) {
                    if (tmp.length == 4) {
                        boolean registered = false;
                        activityTypeList = activityRepository.findActivityTypes();
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
                            message = activityRepository.addActivityType(activityName, tmp[0], isNumeric, isVisible);
                        }
                        message = activityRepository.addActivity(activityName, tmp[1], month, year);
                    }
                }
                activityList = activityRepository.findActivities(year, month);      //We need an updated list to add to the view
                activityTypeList = activityRepository.findActivityTypes();
            }
            int[] years = getYears();
            String[] months = activityRepository.findMonthList();
            ModelAndView modelAndView = new ModelAndView("admin/activitiesPrMonth");
            modelAndView.addObject(activityList);
            modelAndView.addObject(activityTypeList);
            modelAndView.addObject(employeeList);
            modelAndView.addObject("Year", year);
            modelAndView.addObject("Month", month);
            modelAndView.addObject("Years", years);
            modelAndView.addObject("Months", months);
            modelAndView.addObject("message", message);
            return modelAndView;
        } else return new ModelAndView("permissionDenied");
    }


    /* This method returns the activitiesPrMonth view without saving any changes made to the textboxes. */
    @RequestMapping("/admin/activitiesCancel.htm")
    public ModelAndView cancelActivityUpdates(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (userService.isAuthorized(request, roleAdmin)) {
            int year = 0;
            if (request.getParameter("Year") != null) {
                year = Integer.parseInt(request.getParameter("Year"));
            } else {
                year = current_year;
            }
            String month = request.getParameter("Month");
            if (month == null)
                month = activityRepository.findMonth(current_month);

            List<Activity> activityList = activityRepository.findActivities(year, month);
            List<ActivityType> activityTypeList = activityRepository.findActivityTypes();
            List<Employee> employeeList = activityRepository.findEmployees();
            String message = "Canceled changes.";
            int[] years = getYears();
            String[] months = activityRepository.findMonthList();
            ModelAndView modelAndView = new ModelAndView("admin/activitiesPrMonth");
            modelAndView.addObject(activityList);
            modelAndView.addObject(activityTypeList);
            modelAndView.addObject(employeeList);
            modelAndView.addObject("Year", year);
            modelAndView.addObject("Month", month);
            modelAndView.addObject("Months", months);
            modelAndView.addObject("Years", years);
            modelAndView.addObject("message", message);
            return modelAndView;
        } else return new ModelAndView("permissionDenied");

    }


    /* This method checks if some activity type is marked for deletion.
    *  Then it deletes the activities of the type from the Activites table.
    *  Then it deletes the category from the Activity_type table.
    *  Finally it adds the updated activity type list and activity list to the view, and returns the activitiesPrMonth view. */
    @RequestMapping("/admin/updateActivityTypes.htm")
    public ModelAndView deleteActivityType(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (userService.isAuthorized(request, roleAdmin)) {
            int year = 0;
            if (request.getParameter("Year") != null) {
                year = Integer.parseInt(request.getParameter("Year"));
            }
            String month = request.getParameter("Month");
            if (year == 0) {
                return new ModelAndView("admin/home");
            }
            List<ActivityType> activityTypeList = activityRepository.findActivityTypes();
            List<Activity> toBeDeleted = new ArrayList<Activity>();
            String message = "";
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
            List<Activity> activityList = activityRepository.findActivities(year, month);
            List<Employee> employeeList = activityRepository.findEmployees();
            int[] years = getYears();
            String[] months = activityRepository.findMonthList();
            ModelAndView modelAndView = new ModelAndView("admin/activitiesPrMonth");
            modelAndView.addObject(activityList);
            modelAndView.addObject(activityTypeList);
            modelAndView.addObject(employeeList);
            modelAndView.addObject("Month", month);
            modelAndView.addObject("Year", year);
            modelAndView.addObject("Years", years);
            modelAndView.addObject("Months", months);
            modelAndView.addObject("message", message);
            return modelAndView;
        } else return new ModelAndView("permissionDenied");
    }

    /* This method adds an activity category to the database. The variable isNumeric describes if the category only has number values, which should
    *  be added together in the activityList view. The isVisible variable describes if the category should be visible in the activityList view.*/
    @RequestMapping("/admin/addActivityType.htm")
    public ModelAndView addActivityType(HttpServletRequest request, HttpServletResponse response) {
        if (userService.isAuthorized(request, roleAdmin)) {
            int year = 0;
            if (request.getParameter("Year") != null) {
                year = Integer.parseInt(request.getParameter("Year"));
            }
            String month = request.getParameter("Month");
            if (year == 0) {
                return new ModelAndView("admin/home");
            }
            String message = "";
            if (!(request.getParameter("CategoryName")).equals("")) {
                int isNumeric = 0;
                int isVisible = 0;
                if ((request.getParameter("number") != null) && (request.getParameter("number").equals("1"))) {
                    isNumeric = 1;
                }
                if ((request.getParameter("visible") != null) && (request.getParameter("visible").equals("1"))) {
                    isVisible = 1;
                }
                message = activityRepository.addActivityType("", request.getParameter("CategoryName"), isNumeric, isVisible);
            }
            List<Activity> activityList = activityRepository.findActivities(year, month);
            List<ActivityType> activityTypeList = activityRepository.findActivityTypes();
            List<Employee> employeeList = activityRepository.findEmployees();
            int[] years = getYears();
            String[] months = activityRepository.findMonthList();
            ModelAndView modelAndView = new ModelAndView("admin/activitiesPrMonth");
            modelAndView.addObject(activityList);
            modelAndView.addObject(activityTypeList);
            modelAndView.addObject(employeeList);
            modelAndView.addObject("Month", month);
            modelAndView.addObject("Year", year);
            modelAndView.addObject("Years", years);
            modelAndView.addObject("Months", months);
            modelAndView.addObject("message", message);
            return modelAndView;
        } else return new ModelAndView("permissionDenied");
    }

    /* This method changes the chosen category name "OldCategoryName" to the new category name  from the textbox with the name  "NewCategoryName"
    *  It then returns the activitiesPrMonth view. */
    @RequestMapping("/admin/changeCategoryName.htm")
    public ModelAndView changeCategory(HttpServletRequest request, HttpServletResponse response) {
        if (userService.isAuthorized(request, roleAdmin)) {
            int year = 0;
            if (request.getParameter("Year") != null)
                year = Integer.parseInt(request.getParameter("Year"));
            else
                year = current_year;
            String month = request.getParameter("Month");
            if (month == null)
                month = activityRepository.findMonth(current_month);

            String oldCategoryName = request.getParameter("OldCategoryName");
            String newCategoryName = request.getParameter("NewCategoryName");

            String message = activityRepository.changeCategoryName(oldCategoryName, newCategoryName);

            int[] years = getYears();
            String[] months = activityRepository.findMonthList();
            List<Activity> activityList = activityRepository.findActivities(year, month);
            List<ActivityType> activityTypeList = activityRepository.findActivityTypes();
            List<Employee> employeeList = activityRepository.findEmployees();
            ModelAndView modelAndView = new ModelAndView("admin/activitiesPrMonth");
            modelAndView.addObject(activityList);
            modelAndView.addObject(activityTypeList);
            modelAndView.addObject(employeeList);
            modelAndView.addObject("Month", month);
            modelAndView.addObject("Year", year);
            modelAndView.addObject("Years", years);
            modelAndView.addObject("Months", months);
            modelAndView.addObject("message", message);
            return modelAndView;
        } else return new ModelAndView("permissionDenied");
    }
}


