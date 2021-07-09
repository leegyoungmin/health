package com.Health.health.Model;

import android.app.Application;

public class Interlocker extends Application {

    private String PhoneNum;
    private String Id;


    public String getPhoneNum() {
        return PhoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        PhoneNum = phoneNum;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
