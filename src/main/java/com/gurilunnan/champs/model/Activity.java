package com.gurilunnan.champs.model;

import com.sun.org.apache.xerces.internal.impl.dv.xs.MonthDayDV;

/**
 * Created by IntelliJ IDEA.
 * User: gurlunna
 * Date: 20.09.11
 * Time: 08:13
 * To change this template use File | Settings | File Templates.
 */
public class Activity {
    private ActivityType activityType;
    private String month;
    private Employee employee;
    private String year;

    public Activity(Employee e, ActivityType a, String mnd) {
        employee = e;
        activityType = a;
        month = mnd;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
