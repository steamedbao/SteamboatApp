package com.SE.steamedboat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button butt1 = (Button)findViewById(R.id.login);

        butt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText num1 = (EditText) findViewById(R.id.username);
                EditText num2 = (EditText) findViewById(R.id.password);
                TextView message = (TextView) findViewById(R.id.message);

                int numone = Integer.parseInt(num1.getText().toString());
                int numtwo = Integer.parseInt(num2.getText().toString());

                if (numone == numtwo) {
                    GoTo_createORjoin();
                }
                else {
                    message.setText("Please make sure your Username and Password match \n");
                }


            }

        });

    }

    public void GoTo_createORjoin(){
        Intent gocreateORjoin = new Intent (this, createORjoin.class);
        startActivity(gocreateORjoin);
    }

}
