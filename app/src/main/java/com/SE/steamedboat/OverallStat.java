package com.SE.steamedboat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.Reference;
import java.util.ArrayList;

public class OverallStat extends AppCompatActivity {

    Button back;
    TextView creatorname,expense,membercount;
    String name;
    String exp;
    String size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overall_stat);

        Intent from_Sum = getIntent();
        name = from_Sum.getStringExtra("name");
        size = from_Sum.getStringExtra("size");
        exp = from_Sum.getStringExtra("sum");


        back = findViewById(R.id.back);
        creatorname = findViewById(R.id.creatorname);
        expense = findViewById(R.id.expense);
        membercount = findViewById(R.id.membercount);

        if(name!=null)
            creatorname.setText(name);

        if (exp!=null)
            expense.setText(exp);

        if (size!=null)
            membercount.setText(size);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
