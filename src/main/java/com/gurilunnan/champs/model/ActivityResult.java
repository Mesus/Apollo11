package com.gurilunnan.champs.model;

/**
 * Created by IntelliJ IDEA.
 * User: gurlunna
 * Date: 29.09.11
 * Time: 09:27
 * To change this template use File | Settings | File Templates.
 */
public class ActivityResult implements Comparable<ActivityResult> {
    private Employee employee;
    private int year;
    private ActivityType activityType;
    private int count;

    public Employee getEmployee() {
        return employee;
    }

    public int compareTo(ActivityResult activityResult) {
        if(this.count < activityResult.getCount()) {
            return 1;
        }
        if(this.count == activityResult.getCount()) {
          return 0;
        }
        else {
            return -1;
        }
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
