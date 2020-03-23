package com.SE.steamedboat;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Rate {

    @SerializedName("SGD")
    @Expose
    private float SGD;

    public Rate(float SGD) {
//        this.name = name;
        this.SGD = SGD;
    }

    public float getSGD() {
        return SGD;

    }
}
