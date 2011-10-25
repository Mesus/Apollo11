package com.inmeta.champs.model;

import org.springframework.stereotype.Component;

/**
 * @author Guri Lunnan
 */

public class MonthRepresentation {
    private String month_name;
    private int month_number;

    public String getMonth_name() {
        return month_name;
    }

    public void setMonth_name(String month_name) {
        this.month_name = month_name;
    }

    public int getMonth_number() {
        return month_number;
    }

    public void setMonth_number(int month_number) {
        this.month_number = month_number;
    }
}
