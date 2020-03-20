package com.SE.steamedboat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Spinner;
import android.widget.SpinnerAdapter;



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
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent from_home = getIntent();
        TripID = from_home.getIntExtra("TripID",100000);

        but_add = (Button) findViewById(R.id.Button_add);
        back = (Button) findViewById(R.id.discardactivity);

        Auth = FirebaseAuth.getInstance();
        FD = FirebaseDatabase.getInstance();
        final FirebaseUser user = Auth.getCurrentUser();
        userID = user.getUid();
        activity_name = (EditText) findViewById(R.id.activityname);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        but_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name = activity_name.getText().toString();

                if (name!="")//need more checks. but rn cant pass in the values for the others yet
                {
                    Activity a1 = new Activity(name);

                    Log.v("E_VALUE","--------  Activity Name : "+ a1.getName() + "---------------------------");


                    myRef = FD.getReference().child("Trips").child(Integer.toString(TripID)).child("activities").child(name);

                    myRef.setValue(a1);
                }
                else
                {

                    Toast.makeText(addActivity.this, "Please enter all fields!", Toast.LENGTH_SHORT).show();                }
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
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.alert)
                .setTitle("Closing Activity")
                .setMessage("Are you sure you want to close this activity without saving?")
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
}
