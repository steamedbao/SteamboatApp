package com.SE.steamedboat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MemberDialog extends AppCompatActivity {
    TextView memberName;
    Button deleteMem;
    Button backToHome;
    private FirebaseDatabase FD;
    private DatabaseReference myRef;
    private DatabaseReference memRef;
    private String TripID;
    private Member thisMem;
    private String name;
    TextView memberExpense;
    TextView memberPaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_memberdialog);
        Intent intent = getIntent();
        name = intent.getStringExtra("namedetail");
        TripID = intent.getStringExtra("TripID");
        thisMem = (Member)intent.getSerializableExtra("Member");


        memberName = (TextView) findViewById(R.id.member_name);
        memberName.setText(name);
        memberExpense = findViewById(R.id.member_expense);
        memberPaid = findViewById(R.id.member_paid);
        memberExpense.setText(Float.toString(thisMem.getAmountIncurred()));
        memberPaid.setText(Float.toString(thisMem.getAmountPaid()));

        FD = FirebaseDatabase.getInstance();
        myRef = FD.getReference();
        memRef = myRef.child("Trips").child(TripID).child("members").child(name);
        Log.v("Directory","-----------memRef is---------"+memRef);
        /*
        memberName = (TextView) findViewById(R.id.member_name);
        memberName.setText(name);
        memberExpense = findViewById(R.id.member_expense);
        memberPaid = findViewById(R.id.member_paid);

        memRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                thisMem = dataSnapshot.getValue(Member.class);
                memberExpense.setText(Float.toString(thisMem.getAmountIncurred()));
                memberPaid.setText(Float.toString(thisMem.getAmountPaid()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */

        deleteMem = (Button) findViewById(R.id.member_delete);
        backToHome = (Button) findViewById(R.id.back);

        deleteMem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.alert)
                .setTitle("Deleting Member")
                .setMessage("Are you sure you want to delete this member permanently?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete

                        Log.v("Try to delete","-----------memRef is---------"+memRef);

                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
