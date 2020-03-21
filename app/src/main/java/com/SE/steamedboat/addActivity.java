package com.SE.steamedboat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.lang.Boolean;

public class addActivity extends AppCompatActivity {

    private FirebaseDatabase FD;
    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener AuthListen;
    private DatabaseReference myRef;
    private String userID;

    private Button but_add;
    private Button but_discard;
    private Button but_selectmember;
    private ScrollView memberSV;
    private ArrayList<Member> members = new ArrayList<>();
    private ArrayList<String> memberlist = new ArrayList<>();
    private CharSequence[] membername;
    private DatabaseReference tripRef;
    private Boolean[] checkeditems;
    private ArrayList<Integer> memberSelected = new ArrayList<>();

    private EditText activity_name;
    private int TripID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent from_home = getIntent();
        TripID = from_home.getIntExtra("TripID",100000);

        but_add = (Button) findViewById(R.id.Button_add);
        but_discard = (Button) findViewById(R.id.discardactivity);
        but_selectmember = (Button) findViewById(R.id.but_selectmem);
        tripRef = myRef.child("Trips").child(Integer.toString(TripID));

        checkeditems = new Boolean[memberlist.size()];


        Auth = FirebaseAuth.getInstance();
        FD = FirebaseDatabase.getInstance();
        final FirebaseUser user = Auth.getCurrentUser();
        userID = user.getUid();
        activity_name = (EditText) findViewById(R.id.activityname);

        tripRef.child("members").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {

                Member mm = new Member();
                mm = dataSnapshot.getValue(Member.class);
                members.add(mm);
                memberlist.add(mm.getMemberName());
                for(int i = 0; i < memberlist.size(); i++){
                    membername[i] = memberlist.get(i);
                }
                Log.v("E_lVALUE", "-------------------ADDED MEMBER: "+ mm.getMemberName() +"  ---------------------------");

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        but_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name = activity_name.getText().toString();

                Activity a1 = new Activity(name);

                Log.v("E_VALUE","--------  Activity Name : "+ a1.getName() + "---------------------------");


                myRef = FD.getReference().child("Trips").child(Integer.toString(TripID)).child("activities").child(name);

                myRef.setValue(a1);

                gotohome();
            }


        });

        but_discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotohome();
            }
        });

        but_selectmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(addActivity.this);
                mBuilder.setTitle("Select the participating members: ");

                for(int i = 0; i < memberlist.size(); i++){
                    membername[i] = memberlist.get(i);
                    Log.v("E_VALUE","--------  Activity Name : ---------------------------");
                }
/*
            mBuilder.setMultiChoiceItems(membername, checkeditems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                        if(isChecked){
                            memberSelected.add(position);
                        }
                        else{
                            memberSelected.remove((Integer.valueOf(position)));
                        }
                    }

                });

*/
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for (int i = 0; i < memberSelected.size(); i++) {
                            item = item + membername[memberSelected.get(i)];
                            if (i != memberSelected.size() - 1) {
                                item = item + ", ";
                            }
                        }
// display out the members selected
                    }
                });

                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < checkeditems.length; i++) {
                            checkeditems[i] = false;
                            memberSelected.clear();
// clear all display
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });




        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        

    }

    public void gotohome(){
        Intent goback = new Intent(this, Homepage.class);
        startActivity(goback);
    }


}
