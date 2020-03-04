package com.SE.steamedboat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class createORjoin extends AppCompatActivity {

    private ListView listview;
    private FirebaseDatabase FD;
    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener AuthListen;
    private DatabaseReference myRef;
    private String userID;

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
        myRef = FD.getReference();
        FirebaseUser user = Auth.getCurrentUser();
        userID = user.getUid();

        /*
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
         */

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

    private void showData(DataSnapshot dataSnapshot) {       // i want this to show user's existing trips, but failed
        for (DataSnapshot ds:dataSnapshot.getChildren()){
            Trip t1 = new Trip();
            t1.setTripName(ds.child(userID).getValue(Trip.class).getTripName());
            ArrayList<String> array = new ArrayList<>();
            array.add(t1.getTripName());
            ArrayAdapter adpter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,array);
            listview.setAdapter(adpter);
        }
    }

    public void GoTo_create(){
        Intent gocreate = new Intent (this, create.class);
        startActivity(gocreate);}

    public void GoTo_join(){
        Intent gojoin = new Intent (this, join.class);
        startActivity(gojoin);}

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
