package com.SE.steamedboat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
//must add code to check if user is already in trip
public class join extends AppCompatActivity {

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
    private EditText tripid;
    private EditText pw;
    private EditText joiner;
    private int tripID;
    private String PW;
    private String Joiner;
    private String IDstring;
    private Button buttonJoin;
    final String[] correctpw = new String[1];
    private boolean correct = false;
    private Button back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        Auth = FirebaseAuth.getInstance();
        FD = FirebaseDatabase.getInstance();
        final FirebaseUser user = Auth.getCurrentUser();
        userID = user.getUid();
        myRef = FD.getReference().child("Trips");

        //=========================
        tripid = (EditText) findViewById(R.id.tripid);
        pw = (EditText) findViewById(R.id.pw);
        joiner = (EditText) findViewById(R.id.joiner);
        buttonJoin = (Button) findViewById(R.id.join_done);
        back = findViewById(R.id.exitjoin);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonJoin.setOnClickListener(new View.OnClickListener() {    ////// ADD error handling !!!!!  and now does not prevent existing member join back own trip !!
            @Override
            public void onClick(View v) {

                IDstring = tripid.getText().toString();
                PW = pw.getText().toString();
                Joiner = joiner.getText().toString();

                myRef.child(IDstring).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue()==null) {
                            Toast.makeText(join.this, "Invalid TripID, try again", Toast.LENGTH_SHORT).show();                        }

                        correctpw[0] = dataSnapshot.child("passWord").getValue(String.class);
                        String temp = dataSnapshot.child("passWord").getValue(String.class);
                        Log.v("E_VALUE","--------  Data in : "+ dataSnapshot.child("passWord").getValue() + "---------------------------");
                        Log.v("E_VALUE","--------  temp : "+ temp + "---------------------------");
                        Log.v("E_VALUE","--------  correctpw : "+ correctpw[0] + "---------------------------");
                        correct = PW.equals(temp);
                        Log.v("E_VALUE","--------  Correct? : "+ correct + "---------------------------");

                        if (correct==false){
                            Toast.makeText(join.this, "Password incorrect, try again", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            Toast.makeText(join.this, "correct", Toast.LENGTH_SHORT).show();
                            String tripname = dataSnapshot.child("tripName").getValue(String.class);
                            SimpleTrip st = new SimpleTrip(tripname,Integer.parseInt(IDstring),true);
                            Member mem = new Member(Joiner);
                            mem.setUID(userID);
                            FD.getReference().child("Trips").child(IDstring).child("members").child(Joiner).setValue(mem);
                            FD.getReference().child("Users").child(userID).child("trips").child(IDstring).setValue(st);
                            Toast.makeText(join.this, "Join Trip Success", Toast.LENGTH_SHORT).show();
                            GoTo_home(Integer.parseInt(IDstring));


                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    protected void toastMessage(String message){
        Toast.makeText(this, message,Toast.LENGTH_SHORT).show();
    }

    public void GoTo_home(int id){
        Intent gohome = new Intent (this, Homepage.class);
        gohome.putExtra("TripID", id);
        startActivity(gohome);}

}