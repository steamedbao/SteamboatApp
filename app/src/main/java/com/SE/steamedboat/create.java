package com.SE.steamedboat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class create extends AppCompatActivity {
    private FirebaseDatabase FD;
    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener AuthListen;
    private DatabaseReference myRef;
    private String userID;
    private int TripID;
    private Button backButton;
    private Spinner spinner3;
    String currency;
    String trip_count;
    String DBtrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        Auth = FirebaseAuth.getInstance();
        FD = FirebaseDatabase.getInstance();
        myRef = FD.getReference();
        FirebaseUser user = Auth.getCurrentUser();
        userID = user.getUid();
        backButton=findViewById(R.id.discardtrip);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        final long[] tripcount = new long[2];
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long No_of_trips = dataSnapshot.child("Users").child(userID).child("trips").getChildrenCount();
                long DB_total_trips = dataSnapshot.child("Trips").getChildrenCount();

                // Map<String,String> map = dataSnapshot.child("Users").child(userID).getValue(Map.class);
                Log.v("E_VALUE","--------  Data : "+ No_of_trips + "---------------------------");
                tripcount[0] = No_of_trips;
                tripcount[1] = DB_total_trips;
                Log.v("E_VALUE","--------  Data : "+ tripcount[0] + "---------------------------");

            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        spinner3 = (Spinner) findViewById(R.id.homespinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency, R.layout.spinnerview);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner3.setAdapter(adapter);

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currency = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Button create_done = (Button) findViewById(R.id.create_done);

        create_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EditText tripname = (EditText) findViewById(R.id.tripname);
                EditText password = (EditText) findViewById(R.id.pw);
                EditText creater = (EditText) findViewById(R.id.creater);

                Log.v("E_VALUE","--------  Data : "+ tripcount[0] + "---------------------------");

                trip_count = Long.toString(tripcount[0]);
                DBtrip = Long.toString(tripcount[1]+100000);
                Log.v("E_VALUE","--------  Data : "+ DBtrip + "---------------------------");

                String name = tripname.getText().toString();
                String pw = password.getText().toString();
                String create = creater.getText().toString();
                TripID = Integer.parseInt(DBtrip);


                if( !pw.equals("") && !name.equals("") && !create.equals("")){
                    Trip t1 = new Trip(name, pw, create,TripID);
                    int ID = t1.getTripID();
                    com.SE.steamedboat.Member m1 = new Member(create);
                    m1.setUID(userID);
                    m1.setHost(true);
                    t1.addMember(m1);
                    t1.setCreaterUID(userID);
                    t1.setHomeCurrency(currency);
                    com.SE.steamedboat.SimpleTrip s1 = new SimpleTrip (name,ID,true);
                    myRef.child("Trips").child(DBtrip).setValue(t1);
                    myRef.child("Trips").child(DBtrip).child("members").child(create).setValue(m1);
                    myRef.child("Users").child(userID).child("trips").child(trip_count).setValue(s1);

                    //myRef.child("Users").child(userID).child("trips").child(trip_count).child("TripName").setValue(create);
                    //myRef.child("Users").child(userID).child("trips").child(trip_count).child("ID").setValue(id);
                    //myRef.child("Users").child(userID).child("trips").child(trip_count).child("Ongoing").setValue("true");

                    /* testing if this member is saved
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            showData(dataSnapshot, id);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    */


                    GoTo_home();
                }

                else{
                    Toast.makeText(create.this, "Make sure all fields are entered", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.alert)
                .setTitle("Closing Trip")
                .setMessage("Are you sure you want to close this Trip without saving?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    public void GoTo_home(){
        Intent gohome = new Intent (this, Homepage.class);
        gohome.putExtra("TripID", TripID);
        startActivity(gohome);}

    public void GoTo_main(){
        Intent gologin = new Intent (this, MainActivity.class);
        startActivity(gologin);}

    private void showData(DataSnapshot dataSnapshot, String id) {       // i want this to show user's existing trips, but failed
        for (DataSnapshot ds:dataSnapshot.getChildren()){
            System.out.println("added member name is "+ds.child(id).getValue(Member.class).getMemberName()+"\n");
            //listview.setAdapter(adpter);
        }
    }
}
