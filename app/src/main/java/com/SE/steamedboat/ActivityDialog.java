package com.SE.steamedboat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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

public class ActivityDialog extends AppCompatActivity {

    private FirebaseDatabase FD;
    private DatabaseReference myRef;
    private DatabaseReference ActRef;

    TextView Name, Split, Expense, Paid, Status;
    Button edit;
    Button backToHome;
    Activity thisAct=new Activity();
    String UID;
    String TripID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_activity_dialog);

        Intent intent = getIntent();
        UID = intent.getStringExtra("directory");
        TripID = intent.getStringExtra("TripID");

        FD = FirebaseDatabase.getInstance();
        myRef = FD.getReference();
        ActRef = myRef.child("Trips").child(TripID).child("activities").child(UID);

        Name = (TextView) findViewById(R.id.D_act_name_value);
        Expense = (TextView) findViewById(R.id.D_act_expense_value);
        Split = (TextView) findViewById(R.id.D_act_split_value);
        Paid = (TextView) findViewById(R.id.D_act_paidby_value);
        Status = (TextView) findViewById(R.id.textView3);

        edit = (Button) findViewById(R.id.D_activity_edit);
        backToHome = (Button) findViewById(R.id.back);


        ActRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                thisAct = dataSnapshot.getValue(Activity.class);
                Name.setText(thisAct.getName());
                Expense.setText(Float.toString(thisAct.getActivityExpense()));
                Split.setText(Boolean.toString(thisAct.getSplit()));
                Paid.setText(thisAct.getPayer());
                if(thisAct.getStatus()){Status.setText("Pending");}
                else Status.setText("Settled");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /// put your own code here ------------------------------
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
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
