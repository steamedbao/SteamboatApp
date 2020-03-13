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

public class addActivity extends AppCompatActivity {

    private FirebaseDatabase FD;
    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener AuthListen;
    private DatabaseReference myRef;
    private String userID;

    private Button but_add;
    private EditText activity_name;
    private int TripID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent from_home = getIntent();
        TripID = from_home.getIntExtra("TripID",100000);

        but_add = (Button) findViewById(R.id.Button_add);

        Auth = FirebaseAuth.getInstance();
        FD = FirebaseDatabase.getInstance();
        final FirebaseUser user = Auth.getCurrentUser();
        userID = user.getUid();
        activity_name = (EditText) findViewById(R.id.activityname);


        but_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name = activity_name.getText().toString();

                Activity a1 = new Activity(name);

                Log.v("E_VALUE","--------  Activity Name : "+ a1.getName() + "---------------------------");

                Log.v("E_VALUE","--------  Activity Name : "+ a1.getName() + "---------------------------");


                myRef = FD.getReference().child("Trips").child(Integer.toString(TripID)).child("activities").child(name);

                myRef.setValue(a1);
            }
        });

    }
}
