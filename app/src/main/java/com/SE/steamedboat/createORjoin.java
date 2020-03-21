package com.SE.steamedboat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Map;

public class createORjoin extends AppCompatActivity {

    private FirebaseDatabase FD;
    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener AuthListen;
    private DatabaseReference myRef;
    private String userID;

    private ListView LV;
    private ArrayList<SimpleTrip> trips = new ArrayList<>();
    private ArrayList<String> toshow = new ArrayList<>();
    private ArrayList<Integer> IDlist= new ArrayList<>();
    private ArrayAdapter<SimpleTrip> tripArrayAdapter;
    private SimpleTrip ST;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_orjoin);

        Button btnLogout;
        btnLogout = findViewById(R.id.logout1);

        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(createORjoin.this, MainActivity.class);
                startActivity(intToMain);
            }

        });


        Auth = FirebaseAuth.getInstance();
        FD = FirebaseDatabase.getInstance();
        final FirebaseUser user = Auth.getCurrentUser();
        userID = user.getUid();
        myRef = FD.getReference().child("Users").child(userID).child("trips");
        // ---------------------------------------------
        LV = (ListView) findViewById(R.id.triplist);
        final ArrayAdapter<String> name_adapter = new ArrayAdapter<String>(this, R.layout.cust_list_view_2, toshow);
        ST = new SimpleTrip();
        LV.setAdapter(name_adapter);


        final long[] tripcount = new long[1];
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long No_of_trips = dataSnapshot.child("Users").child(userID).child("trips").getChildrenCount();
                // Map<String,String> map = dataSnapshot.child("Users").child(userID).getValue(Map.class);
                Log.v("E_VALUE", "--------  Data : " + No_of_trips + "---------------------------");
                tripcount[0] = No_of_trips;
                Log.v("E_VALUE", "--------  Data : " + tripcount[0] + "---------------------------");

               /* for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ST = ds.getValue(SimpleTrip.class);
                    trips.add(ST);

                    ////// USEFUL alternative way

                }

                LV.setAdapter(tripArrayAdapter);*/

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.v("E_VALUE","------------------- Line 98  ---------------------------");


        final int count = (int) tripcount[0];


        myRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //SimpleTrip value = new SimpleTrip();


                    SimpleTrip trip = dataSnapshot.getValue(SimpleTrip.class);
                Log.v("E_VALUE", "-------------------Trip Name read: "+ trip.getTripName() +"  ---------------------------");

                Log.v("E_VALUE", "------------------- Line 110   ---------------------------");
                    IDlist.add(trip.getID());
                    for (int i = 1; i<=IDlist.size();i++){
                        Log.v("E_VALUE", "------------------- "+ IDlist.get(i-1) +"---------------------------");

                    }
                    trips.add(trip);
                    toshow.add("Trip: "+trip.getTripName()+"    ID: "+Integer.toString(trip.getID())+"    Ongoing: "+Boolean.toString(trip.isOngoing()));

                Log.v("E_VALUE", "------------------- Line 113   ---------------------------");
                name_adapter.notifyDataSetChanged();
                Log.v("E_VALUE", "------------------- Line 115   ---------------------------");

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

        LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("E_VALUE", "-------------------CLICKED AT POS: "+ position +" ---------------------------");

                GoTo_home(IDlist.get(position));
            }
        });

        Button createbut = (Button) findViewById(R.id.createtrip);

        createbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    GoTo_create();
                }
        });



        Button join = (Button) findViewById(R.id.join);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GoTo_join();
            }
        });



    }

    /*private void showData(DataSnapshot dataSnapshot) {       // i want this to show user's existing trips, but failed
        for (DataSnapshot ds:dataSnapshot.getChildren()){
            Trip t1 = new Trip();
            t1.setTripName(ds.child(userID).getValue(Trip.class).getTripName());
            ArrayList<String> array = new ArrayList<>();
            array.add(t1.getTripName());
            ArrayAdapter adpter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,array);
            listview.setAdapter(adpter);
        }
    }*/

    public void GoTo_create(){
        Intent gocreate = new Intent (this, create.class);
        startActivity(gocreate);}

    public void GoTo_join(){
        Intent gojoin = new Intent (this, join.class);
        startActivity(gojoin);}

    public void GoTo_home(int id){
        Intent gohome = new Intent (this, Homepage.class);
        gohome.putExtra("TripID", id);
        startActivity(gohome);}

    /*                                                         FK this if involved APP will always quit and quit
    @Override
    protected void onStart(){
        super.onStart();
        Auth.addAuthStateListener(AuthListen);
    }

    @Override
    protected void onStop(){
        super.onStop();
        if (AuthListen != null){
            Auth.removeAuthStateListener(AuthListen);
        }
    }

    */
    protected void toastMessage(String message){
        Toast.makeText(this, message,Toast.LENGTH_SHORT).show();
    }
}
