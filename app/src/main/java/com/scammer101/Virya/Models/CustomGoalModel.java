package com.scammer101.Virya.Models;

public class CustomGoalModel
{
    private String date, name, repeat, dateAndUser;
    private int timer;

    public CustomGoalModel() {
    }

    public CustomGoalModel(String date, String name, String repeat, String dateAndUser, int timer) {
        this.date = date;
        this.name = name;
        this.repeat = repeat;
        this.dateAndUser = dateAndUser;
        this.timer = timer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getDateAndUser() {
        return dateAndUser;
    }

    public void setDateAndUser(String dateAndUser) {
        this.dateAndUser = dateAndUser;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }
}
