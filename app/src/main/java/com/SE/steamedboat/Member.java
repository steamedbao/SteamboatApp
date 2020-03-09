package com.SE.steamedboat;

public class Member {
    private String memberName;
    private int memberID;
    private float amountPaid;
    private float amountIncurred;
    private boolean host;

    public Member (String name) {
        setMemberName(name);
        System.out.println("Member created, name is "+name +"\n");
    }

    public void setMemberName(String name){
        memberName = name;
    }

    public String getMemberName(){return memberName;}

    public int getMemberID() {return memberID;}

    public void setMemberID(int memberID) {this.memberID = memberID;}

    public float getAmountPaid() {return amountPaid;}

    protected void setAmountPaid(float amountPaid) {this.amountPaid = amountPaid;}

    public float getAmountIncurred() {return amountIncurred;}

    protected void setAmountIncurred(float amountIncurred) {this.amountIncurred = amountIncurred;}

    public boolean isHost() {return host;}

    public void setHost(boolean host) {this.host = host;}

    public void removeMember(){} //need edit!!!!!!!!!!!!!!!

    public boolean verifyMember(){return true;} //need edit!!!!!!!!!!!!!!!
}
