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
    private int year;

    public Activity(ActivityType activityType,  Employee employee, String month, int year) {
        this.activityType = activityType;
        this.employee = employee;
        this.month = month;
        this.year = year;
    }

    public Activity() {}

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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
