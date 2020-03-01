package com.SE.steamedboat;

import androidx.appcompat.app.AppCompatActivity;

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


                int numone = Integer.parseInt(num1.getText().toString());
                int numtwo = Integer.parseInt(num2.getText().toString());
                boolean res = (numone == numtwo);

            }

        });

    }


}
