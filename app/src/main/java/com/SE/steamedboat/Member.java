package com.SE.steamedboat;

public class Member {
    private String memberName;

    public Member (String name) {
        setMemberName(name);
        System.out.println("Member created, name is "+name +"\n");
    }

    public void setMemberName(String name){
        memberName = name;
    }

    public String getMemberName(){return memberName;}
}
