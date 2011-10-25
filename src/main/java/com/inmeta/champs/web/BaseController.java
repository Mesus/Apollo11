package com.inmeta.champs.web;

import com.inmeta.champs.persistence.ActivityRepository;
import com.inmeta.champs.persistence.UserRepository;
import com.inmeta.champs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Calendar;

@Controller
public class BaseController {
    protected final String roleAdmin = "Admin";
    protected final String roleMember = "Member";
    private int get_year = 2011;
    protected final int current_month = 10;
    protected final int current_year = get_year;
    protected final int start_year = 2010;


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

    protected int[] getYears() {
        int j = 0;
        int[] years = new int[(current_year + 2 - start_year)];
        for (int i = start_year; i < current_year + 2; i++) {
            years[j] = i;
            j++;
        }
        return years;
    }

}
