package com.bcit.teng_prateek;

import android.support.v4.app.INotificationSideChannel;

public class Reading {

    private String id;
    private int systolic;
    private int diastolic;
    private String datetime;

    public Reading() { }

    public Reading(String id, int systolic, int diastolic, String datetime) {
        this.id = id;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.datetime = datetime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSystolic() {
        return systolic;
    }

    public void setSystolic(int systolic) {
        this.systolic = systolic;
    }

    public int getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(int diastolic) {
        this.diastolic = diastolic;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
