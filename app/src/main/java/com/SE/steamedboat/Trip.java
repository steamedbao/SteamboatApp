package com.SE.steamedboat;

import java.util.ArrayList;

public class Trip {
    private int tripID;
    private String tripName;
    private String passWord;
    private String createrName;
    private boolean ongoing;
    private String homeCurrency; // TBC if we use ENUM
    private ArrayList<com.SE.steamedboat.Member> members;
    private ArrayList<com.SE.steamedboat.Activity> activities;


    public Trip (String name, String pw, String creater){
        setPassWord(pw);
        setTripName(name);
        setCreaterName(creater);
        tripID = (int)(Math.random()*(50000))+10000;
        System.out.println("ID is " + tripID +"\n");
        // checking only
        com.SE.steamedboat.Member mem1 = new com.SE.steamedboat.Member(creater);
        members.add(mem1);
    }


    public int getTripID(){
       return tripID;
    }

    public void setTripName(String name){
       tripName = name;
    }

    public String getTripName(){
        return tripName;
    }

    public void setPassWord(String pw){
        passWord = pw ;
    }

    public String getPassWord(){
        return passWord;
    }

    public void  setCreaterName(String name){
        createrName=name;
    }

    public String getCreaterName(){
        return createrName;
    }


    public void setHomeCurrency(String currency){
        homeCurrency = currency;
    }

    public String getHomeCurrency(){
        return homeCurrency;
    }


}
