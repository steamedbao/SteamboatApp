package com.SE.steamedboat;

import android.view.SurfaceControl;

import java.util.ArrayList;
import java.util.Date;

/*enum status_choice {
    SETTLED, PENDING;
}

enum split_chioce {
    SPLIT_EVEN, CUSTOMISE;
}*/

public class Activity {

    private String name;
    private int id;
    private Date dateTime = new Date();
    private float activityExpense = 0;
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private ArrayList<Integer> participant = new ArrayList<>();
    private ArrayList<Float> individualExpense= new ArrayList<>();
    private boolean status = true;
    private boolean split = true;
    private String activityCurrency = "SGD";
    private float exchangeRate = 1;
    private float homeWorth = 0;

    public Activity(String n) {
        name = n;
        id = (int)(Math.random()*(500000))+100000;
    }

    public Activity(){}

    public void updateMember(){};

    public void updateActivity(){};

    public ArrayList getActivityInfo(){
        ArrayList<Object> activityInfo = new ArrayList<Object>();
        activityInfo.add(this.name);
        activityInfo.add(this.id);
        activityInfo.add(this.dateTime);
        activityInfo.add(this.activityExpense);
        activityInfo.add(this.individualExpense);
        activityInfo.add(this.participant);
        activityInfo.add(this.split);
        activityInfo.add(this.status);
        activityInfo.add(this.exchangeRate);
        activityInfo.add(this.activityCurrency);
        activityInfo.add(this.homeWorth);
        activityInfo.add(this.transactions);
        return activityInfo;
    };

    public float getActivityExpense() {
        return activityExpense;
    }

    public void setActivityExpense(float activityExpense) {
        this.activityExpense = activityExpense;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public ArrayList<Integer> getParticipant() {
        return participant;
    }

    public void setParticipant(ArrayList<Integer> participant) {
        this.participant = participant;
    }

    public ArrayList<Float> getIndividualExpense() {
        return individualExpense;
    }

    public void setIndividualExpense(ArrayList<Float> individualExpense) {
        this.individualExpense = individualExpense;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getSplit() {
        return split;
    }

    public void setSplit(boolean split) {
        this.split = split;
    }

    public String getActivityCurrency() {
        return activityCurrency;
    }

    public void setActivityCurrency(String activityCurrency) {
        this.activityCurrency = activityCurrency;
    }

    public float getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(float exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public float getHomeWorth() {
        return homeWorth;
    }

    public void setHomeWorth(float homeWorth) {
        this.homeWorth = homeWorth;
    }
}
