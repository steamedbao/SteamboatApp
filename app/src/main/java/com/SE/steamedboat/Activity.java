package com.SE.steamedboat;

import android.view.SurfaceControl;

import java.util.ArrayList;
import java.util.Date;

public class Activity {
    private String name;
    private int id;
    private Date dateTime;
    private float activityExpense;
    private ArrayList<Transaction> transactions;
    private ArrayList<Integer> participant;
    private ArrayList<Float> individualExpense;
    private Enum status;
    private Enum split;
    private String activityCurrency;
    private float exchangeRate;
    private float homeWorth;

    public Activity(String n) {
        name = n;
    }

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

    public Enum getStatus() {
        return status;
    }

    public void setStatus(Enum status) {
        this.status = status;
    }

    public Enum getSplit() {
        return split;
    }

    public void setSplit(Enum split) {
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
