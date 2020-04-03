package com.SE.steamedboat.Entity;

import java.io.Serializable;

public class Member implements Serializable {
    private String memberName;
    private String UID=null;
    private int memberID=0;
    private float amountPaid=0;
    private float amountIncurred=0;
    private boolean host=false;
    public Member () {};
    public Member (String name) {
        setMemberName(name);
        System.out.println("Member created, name is "+name +"\n");
    }

    public void setMemberName(String name){memberName = name;}

    public String getMemberName(){return memberName;}

    public String getUID() {return UID;}

    public void setUID(String UID) {this.UID = UID;}

    public int getMemberID() {
        return memberID;
    }

    protected void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public float getAmountPaid() {
        return amountPaid;
    }

    public void addAmountPaid(float amountPaid) {
        this.amountPaid += amountPaid;
    }

    public void setAmountPaid(float amountPaid) {
        this.amountPaid = amountPaid;
    }

    public float getAmountIncurred() {
        return amountIncurred;
    }

    public void addAmountIncurred(float amountIncurred) {
        this.amountIncurred += amountIncurred;
    }

    public void setAmountIncurred(float amountIncurred) {
        this.amountIncurred = amountIncurred;
    }

    public boolean isHost() {
        return host;
    }

    public void setHost(boolean host) {
        this.host = host;
    }

    public void removeMember(){} //need edit..........................

    public boolean verifyMember(){return true;}  //need edit..........................
}
