package com.gurilunnan.champs.model;

/**
 * Created by IntelliJ IDEA.
 * User: gurlunna
 * Date: 20.09.11
 * Time: 08:14
 * To change this template use File | Settings | File Templates.
 */


public class ActivityType {
    String activityName;
    String category;
    boolean isNumeric;
    boolean isVisible;

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isNumeric() {
        return isNumeric;
    }

    public void setNumeric(boolean numeric) {
        isNumeric = numeric;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
