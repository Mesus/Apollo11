package com.inmeta.champs.web;

import com.inmeta.champs.persistence.ActivityRepository;
import com.inmeta.champs.persistence.UserRepository;
import com.inmeta.champs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BaseController {
    protected final String roleAdmin = "Admin";
    protected final String roleMember = "Member";

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

}
