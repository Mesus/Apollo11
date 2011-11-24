package com.inmeta.champs.model;

/**
 * @author Guri Lunnan
 */

public class Champion implements Comparable<Champion> {
    private String name;
    private int score;
    private int count;
    private int points;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int compareTo(Champion champion) {
        if(this.points < champion.getPoints()) {
            return 1;
        }
        if(this.points == champion.getPoints()) {
          return 0;
        }
        else {
            return -1;
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
