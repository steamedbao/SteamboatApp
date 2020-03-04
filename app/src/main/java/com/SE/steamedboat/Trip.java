package com.SE.steamedboat;

import java.util.ArrayList;

public class Trip {
    private int tripID;
    private String tripName;
    private String passWord;
    private String createrName;
    private boolean ongoing;
    private String homeCurrency; // TBC if we use ENUM
    private ArrayList<Member> members = null;
    private ArrayList<com.SE.steamedboat.Activity> activities=null;

    public Trip(){}

    public Trip (String name, String pw, String creater){
        setPassWord(pw);
        setTripName(name);
        setCreaterName(creater);
        tripID = (int)(Math.random()*(500000))+100000;
        System.out.println("ID is " + tripID +"\n");
        members = new ArrayList<Member>();
        activities = new ArrayList<Activity>();
        // checking only
        // the code below got error !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
       // Member mem1 = new Member(creater);
       // members.add(mem1);
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

    public void addMember(Member M) {members.add(M);}
}
