package com.scammer101.Virya.Models;

public class UserModel
{
    private String name, number, uid, email, profileImage, dob, currentWeight, startWeight, goalWeight, height, gender;
    private int asanas, trainingCompleted, totalTimeCompleted, totalAsanasCompleted;
    private boolean ban, subscribe;

    public UserModel() {
    }

    public UserModel(String name, String number, String uid, String email, String profileImage, String dob, String currentWeight, String startWeight, String goalWeight, String height, String gender, int asanas, int trainingCompleted, int totalTimeCompleted, int totalAsanasCompleted, boolean ban, boolean subscribe) {
        this.name = name;
        this.number = number;
        this.uid = uid;
        this.email = email;
        this.profileImage = profileImage;
        this.dob = dob;
        this.currentWeight = currentWeight;
        this.startWeight = startWeight;
        this.goalWeight = goalWeight;
        this.height = height;
        this.gender = gender;
        this.asanas = asanas;
        this.trainingCompleted = trainingCompleted;
        this.totalTimeCompleted = totalTimeCompleted;
        this.totalAsanasCompleted = totalAsanasCompleted;
        this.ban = ban;
        this.subscribe = subscribe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(String currentWeight) {
        this.currentWeight = currentWeight;
    }

    public String getStartWeight() {
        return startWeight;
    }

    public void setStartWeight(String startWeight) {
        this.startWeight = startWeight;
    }

    public String getGoalWeight() {
        return goalWeight;
    }

    public void setGoalWeight(String goalWeight) {
        this.goalWeight = goalWeight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAsanas() {
        return asanas;
    }

    public void setAsanas(int asanas) {
        this.asanas = asanas;
    }

    public int getTrainingCompleted() {
        return trainingCompleted;
    }

    public void setTrainingCompleted(int trainingCompleted) {
        this.trainingCompleted = trainingCompleted;
    }

    public int getTotalTimeCompleted() {
        return totalTimeCompleted;
    }

    public void setTotalTimeCompleted(int totalTimeCompleted) {
        this.totalTimeCompleted = totalTimeCompleted;
    }

    public int getTotalAsanasCompleted() {
        return totalAsanasCompleted;
    }

    public void setTotalAsanasCompleted(int totalAsanasCompleted) {
        this.totalAsanasCompleted = totalAsanasCompleted;
    }

    public boolean isBan() {
        return ban;
    }

    public void setBan(boolean ban) {
        this.ban = ban;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }
}
