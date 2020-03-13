package com.SE.steamedboat;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import android.util.Log;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Ref;
import java.util.ArrayList;

public class Homepage extends AppCompatActivity {
    Button btnLogout;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase FD;
    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener AuthListen;
    private DatabaseReference myRef;
    private DatabaseReference TripRef;
    private String userID;
    private Trip currentTrip;
    private ArrayList<Trip> ALtrip = new ArrayList<>();
    private ArrayList<Member> ALmember = new ArrayList<>();
    private ArrayList<String> ALmembernames = new ArrayList<>();
    private ArrayList<String> AL_activity_names = new ArrayList<>();

    private TextView TVtripname;
    private ListView LV;
    private ListView LVactivity;
    private Button addMember;
    private Button addActivity;
    private int TripID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Intent from_createORjoin = getIntent();
        TripID = from_createORjoin.getIntExtra("TripID",100000);
        addMember = findViewById(R.id.addmember);
        btnLogout = findViewById(R.id.logout);

        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(Homepage.this, MainActivity.class);
                startActivity(intToMain);
            }

        });
        Auth = FirebaseAuth.getInstance();
        FD = FirebaseDatabase.getInstance();
        myRef = FD.getReference();
        FirebaseUser user = Auth.getCurrentUser();
        userID = user.getUid();
        LV = (ListView) findViewById(R.id.membersLV);
        LVactivity = (ListView) findViewById(R.id.LVactivity);
        final ArrayAdapter<String> actAdapter = new ArrayAdapter<String>(this, R.layout.cust_list_view, AL_activity_names);
        final ArrayAdapter<String> memAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ALmembernames);
        LV.setAdapter(memAdapter);
        LVactivity.setAdapter(actAdapter);
        TVtripname = (TextView) findViewById(R.id.hometripname);
        TripRef = myRef.child("Trips").child(Integer.toString(TripID));

        currentTrip = new Trip();

        Log.v("E_VALUE", "-------------------AL size is: "+ ALtrip.size()+"  ---------------------------");

        if (ALtrip.size()==0){

            /*TripRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    currentTrip=dataSnapshot.getValue(Trip.class);
                    ALtrip.add(currentTrip);
                    Log.v("E_VALUE", "-------------------Trip Name is: "+ currentTrip.getTripName() +"  ---------------------------");

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            }); */

            TripRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currentTrip=dataSnapshot.getValue(Trip.class);
                    ALtrip.add(currentTrip);
                    Log.v("E_VALUE", "-------------AFTER ADD ------AL size is: "+ ALtrip.size()+"  ---------------------------");

                    Log.v("E_VALUE", "-------------------Trip Name is: "+ currentTrip.getTripName() +"  ---------------------------");
                    Log.v("E_VALUE", "-------------------dataSnapshot.getValue() is: "+ dataSnapshot.getValue() +"  ---------------------------");
                    TVtripname.setText(currentTrip.getTripName());
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            TripRef.child("members").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {

                    Member mm = new Member();
                    mm = dataSnapshot.getValue(Member.class);
                    ALmember.add(mm);
                    ALmembernames.add(mm.getMemberName());
                    memAdapter.notifyDataSetChanged();
                    Log.v("E_VALUE", "-------------------ADDED MEMBER: "+ mm.getMemberName() +"  ---------------------------");

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


            TripRef.child("activities").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    Activity a = new Activity();
                    a = dataSnapshot.getValue(Activity.class);
                    AL_activity_names.add(a.getName()+ " " + a.getActivityCurrency() +": " + a.getActivityExpense() +"  " + a.getSplit() );
                    actAdapter.notifyDataSetChanged();

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



        addActivity = findViewById(R.id.addActivity);
        addMember = findViewById(R.id.addmember);

        addActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Goto_addActivity(TripID);
            }
        });

        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Goto_addMember(TripID);
            }
        });

        //Log.v("E_VALUE", "-------------------Outside listener AL Trip Name is: "+ ALtrip.get(0).getTripName() +"  ---------------------------");


        // By here the trip object is obtained ----------------WHY TF is this NOT running ??????????

        while (ALtrip.size()!=0) {

            TVtripname.setText(currentTrip.getTripName());
            Log.v("E_VALUE", "-------------------Home page trip name is set to: "+ currentTrip.getTripName() +"  ---------------------------");
            break;


        }

    }

    public void Action(){

        while (ALtrip.size()!=0) {

            TVtripname.setText(currentTrip.getTripName());
            Log.v("E_VALUE", "-------------------Home page trip name is set to: "+ currentTrip.getTripName() +"  ---------------------------");
            break;


        }

    };

    public void Goto_addMember(int id){
            Intent addM = new Intent(this, AddMember.class);
            addM.putExtra("TripID", id);
            startActivity(addM);
    }

    public void Goto_addActivity(int id){
        Intent gotoadd = new Intent(this, addActivity.class);
        gotoadd.putExtra("TripID", id);
        startActivity(gotoadd);
    }

}
