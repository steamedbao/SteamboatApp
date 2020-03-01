package com.SE.steamedboat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class create extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);


        Button create_done = (Button) findViewById(R.id.create_done);

        create_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GoTo_home();
            }
        });



    }

    public void GoTo_home(){
        Intent gohome = new Intent (this, Homepage.class);
        startActivity(gohome);}
}
