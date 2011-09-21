package com.gurilunnan.champs.model;

/**
 * Created by IntelliJ IDEA.
 * User: gurlunna
 * Date: 21.09.11
 * Time: 10:30
 * To change this template use File | Settings | File Templates.
 */
public class Champion {
    Employee employee;
    String month;
    String year;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
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
