package com.inmeta.champs.web;

import com.inmeta.champs.persistence.ActivityRepository;
import com.inmeta.champs.persistence.UserRepository;
import com.inmeta.champs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Calendar;

@Controller
public class BaseController {
    protected final String roleAdmin = "Admin";                                         //The name of the Administrator userrole
    protected final String roleMember = "Member";                                       //The name of the Member userrole.
    protected int current_month = 1 + Calendar.getInstance().get(Calendar.MONTH);       //Index of calendar.month starts at 0, not at 1. Returns the current month number.
    protected int current_year = Calendar.getInstance().get(Calendar.YEAR);             //Returns the current year.
    protected final int start_year = 2010;                                              //The first year to be displayed in the app.


    @Autowired
    protected ActivityRepository activityRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected UserService userService;

    protected UserService getUserService() {
        return userService;
    }

    protected void setUserService(UserService userService) {
        this.userService = userService;
    }

    protected ActivityRepository getActivityRepository() {
        return activityRepository;
    }

    protected void setActivityRepository(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    protected UserRepository getUserRepository() {
        return userRepository;
    }

    protected void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /* This method returns an array of years, the first value is the start_year which is defined as a final static int in this class.
    *  The next values is the next year and so on, the last value in the array is the current year. */
    protected int[] getYears() {
        int j = 0;
        int[] years = new int[(current_year + 1 - start_year)];
        for (int i = start_year; i < current_year + 1; i++) {
            years[j] = i;
            j++;
        }
        return years;
    }

}
