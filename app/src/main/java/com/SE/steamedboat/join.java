package com.SE.steamedboat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class join extends AppCompatActivity {

    private int TripID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
    }




    public void GoTo_home(){
        Intent gohome = new Intent (this, Homepage.class);
        gohome.putExtra("TripID", TripID);
        startActivity(gohome);}
}
