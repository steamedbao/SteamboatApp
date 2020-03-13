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

public class AddMember extends AppCompatActivity {

    private FirebaseDatabase FD;
    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener AuthListen;
    private DatabaseReference myRef;
    private String userID;
    private Button but_addmem;
    private EditText member_name;
    private int TripID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        Intent from_home = getIntent();
        TripID = from_home.getIntExtra("TripID",100000);

        but_addmem = (Button) findViewById(R.id.ButAddMem);
        Auth = FirebaseAuth.getInstance();
        FD = FirebaseDatabase.getInstance();
        final FirebaseUser user = Auth.getCurrentUser();
        userID = user.getUid();
        member_name = (EditText) findViewById(R.id.inputMemName);
        but_addmem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = member_name.getText().toString();
                Member mem = new Member(name);
                myRef = FD.getReference().child("Trips").child(Integer.toString(TripID)).child("members").child(name);
                myRef.setValue(mem);
            }
        });
    }
}
