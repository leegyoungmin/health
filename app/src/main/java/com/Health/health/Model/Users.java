package com.Health.health.Model;

import java.util.ArrayList;

public class Users {

    private String id;
    private String username;
    private String imageURL;
    private String status;
    private String AccountType;
    private String PhoneNumber;
    private ArrayList<String> Trainee;
    private String email;
    private String name;
    private String uid;

    // 생성자

    public Users(){

    }

    public Users(String id, String username, String imageURL, String status) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.status = status;
    }

    public Users(String accountType, String phoneNumber, String email, String name, String uid) {
        this.AccountType = accountType;
        this.PhoneNumber = phoneNumber;
        this.email = email;
        this.name = name;
        this.uid = uid;
    }

    //Getter , Setter


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public ArrayList<String> getTrainee() {
        return Trainee;
    }

    public void setTrainee(ArrayList<String> trainee) {
        Trainee = trainee;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
