package com.SE.steamedboat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class Payment_solution extends AppCompatActivity {

    ArrayList<String> sol = new ArrayList<>();
    ListView LV;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_solution);
        Intent from_Sum = getIntent();
        sol = from_Sum.getStringArrayListExtra("sol");
        LV = findViewById(R.id.summaryLv);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.cust_list_view_2, sol);
        LV.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        for (int i =0; i<sol.size();i++){
            Log.v("SOL", "-------------------line is: "+ sol.get(i));
        }

    }
}
