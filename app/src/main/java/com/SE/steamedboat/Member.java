package com.SE.steamedboat;

public class Member {
    private String memberName;
    private String UID=null;

    public Member (String name) {
        setMemberName(name);
        System.out.println("Member created, name is "+name +"\n");
    }

    public void setMemberName(String name){
        memberName = name;
    }

    public String getMemberName(){return memberName;}

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
