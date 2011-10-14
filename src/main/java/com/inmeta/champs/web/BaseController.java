package com.inmeta.champs.web;

import com.inmeta.champs.persistence.ActivityDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
class BaseController {
    @Autowired
    protected ActivityDAO activityDao;


    protected ActivityDAO getActivityDao() {
        return activityDao;
    }

    protected void setActivityDao(ActivityDAO activityDao) {
        this.activityDao = activityDao;
    }
}
