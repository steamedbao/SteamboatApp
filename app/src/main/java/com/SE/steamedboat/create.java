package com.SE.steamedboat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class create extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);


        Button create_done = (Button) findViewById(R.id.create_done);

        create_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText tripname = (EditText) findViewById(R.id.tripname);
                EditText password = (EditText) findViewById(R.id.pw);
                EditText creater = (EditText) findViewById(R.id.creater);
                TextView message = (TextView) findViewById(R.id.textView6);


                String name = tripname.getText().toString();
                String pw = password.getText().toString();
                String create = creater.getText().toString();

                if( pw.length()>2 && name.length()>2 && create.length()>2){
                    // Trip t1 = new Trip(name, pw, create);
                    // testing --> does not work
                    GoTo_home();}

                else{
                    message.setText("Make sure all fields are entered");
                }

            }
        });



    }

    public void GoTo_home(){
        Intent gohome = new Intent (this, Homepage.class);
        startActivity(gohome);}
}
