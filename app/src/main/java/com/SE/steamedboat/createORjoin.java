package com.SE.steamedboat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class createORjoin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_orjoin);

        Button createbut = (Button) findViewById(R.id.createtrip);

        createbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    GoTo_create();
                }
        });

        Button join = (Button) findViewById(R.id.join);

        createbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GoTo_create();
            }
        });


    }

    public void GoTo_create(){
        Intent gocreate = new Intent (this, create.class);
        startActivity(gocreate);}

    public void GoTo_join(){
        Intent gojoin = new Intent (this, join.class);
        startActivity(gojoin);}


}
