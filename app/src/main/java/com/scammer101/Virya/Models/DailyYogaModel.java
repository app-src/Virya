package com.scammer101.Virya.Models;

public class DailyYogaModel
{
    private int finished, inProgress, timeSpent, warriorPoseCountPose, warriorPoseCountTimer, tPoseCountPose, tPoseCountTimer, treePoseCountPose, treePoseCountTimer;
    private String date, userAndDate, userId;

    public DailyYogaModel() {
    }

    public DailyYogaModel(int finished, int inProgress, int timeSpent, int warriorPoseCountPose, int warriorPoseCountTimer, int tPoseCountPose, int tPoseCountTimer, int treePoseCountPose, int treePoseCountTimer, String date, String userAndDate, String userId) {
        this.finished = finished;
        this.inProgress = inProgress;
        this.timeSpent = timeSpent;
        this.warriorPoseCountPose = warriorPoseCountPose;
        this.warriorPoseCountTimer = warriorPoseCountTimer;
        this.tPoseCountPose = tPoseCountPose;
        this.tPoseCountTimer = tPoseCountTimer;
        this.treePoseCountPose = treePoseCountPose;
        this.treePoseCountTimer = treePoseCountTimer;
        this.date = date;
        this.userAndDate = userAndDate;
        this.userId = userId;
    }

    public int getWarriorPoseCountPose() {
        return warriorPoseCountPose;
    }

    public void setWarriorPoseCountPose(int warriorPoseCountPose) {
        this.warriorPoseCountPose = warriorPoseCountPose;
    }

    public int getWarriorPoseCountTimer() {
        return warriorPoseCountTimer;
    }

    public void setWarriorPoseCountTimer(int warriorPoseCountTimer) {
        this.warriorPoseCountTimer = warriorPoseCountTimer;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int gettPoseCountPose() {
        return tPoseCountPose;
    }

    public void settPoseCountPose(int tPoseCountPose) {
        this.tPoseCountPose = tPoseCountPose;
    }

    public int gettPoseCountTimer() {
        return tPoseCountTimer;
    }

    public void settPoseCountTimer(int tPoseCountTimer) {
        this.tPoseCountTimer = tPoseCountTimer;
    }

    public int getTreePoseCountPose() {
        return treePoseCountPose;
    }

    public void setTreePoseCountPose(int treePoseCountPose) {
        this.treePoseCountPose = treePoseCountPose;
    }

    public int getTreePoseCountTimer() {
        return treePoseCountTimer;
    }

    public void setTreePoseCountTimer(int treePoseCountTimer) {
        this.treePoseCountTimer = treePoseCountTimer;
    }

    public String getUserAndDate() {
        return userAndDate;
    }

    public void setUserAndDate(String userAndDate) {
        this.userAndDate = userAndDate;
    }

    public int getFinished() {
        return finished;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public int getInProgress() {
        return inProgress;
    }

    public void setInProgress(int inProgress) {
        this.inProgress = inProgress;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }
}
