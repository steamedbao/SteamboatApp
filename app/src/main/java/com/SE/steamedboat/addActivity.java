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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class addActivity extends AppCompatActivity {

    private FirebaseDatabase FD;
    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener AuthListen;
    private DatabaseReference myRef;
    private String userID;

    private Button but_add;
    private Button but_selectmember;
    private ScrollView memberSV;
    private ArrayList<Member> members = new ArrayList<>();
    private ArrayList<String> memberlist = new ArrayList<>();
    private CharSequence[] membername;
    private DatabaseReference tripRef;
    private boolean[] checkeditems;
    private ArrayList<Integer> memberSelected = new ArrayList<>();

    private EditText activity_name;
    private int TripID;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent from_home = getIntent();
        if (from_home.getStringArrayListExtra("memberlist")!=null)
            memberlist = from_home.getStringArrayListExtra("memberlist");

        but_add = (Button) findViewById(R.id.Button_add);
        back = (Button) findViewById(R.id.discardactivity);
        but_selectmember = (Button) findViewById(R.id.but_selectmem);

        checkeditems = new boolean[memberlist.size()];
        for (int i=0;i<memberlist.size();i++)
        {
            checkeditems[i] = false;
        }

        Auth = FirebaseAuth.getInstance();
        FD = FirebaseDatabase.getInstance();
        tripRef=FD.getReference().child("Trips").child(Integer.toString(TripID));

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

                if (name != "")//need more checks. but rn cant pass in the values for the others yet
                {
                    Activity a1 = new Activity(name);

                    Log.v("E_VALUE", "--------  Activity Name : " + a1.getName() + "---------------------------");


                    myRef = FD.getReference().child("Trips").child(Integer.toString(TripID)).child("activities").child(name);

                    myRef.setValue(a1);
                } else {

                    Toast.makeText(addActivity.this, "Please enter all fields!", Toast.LENGTH_SHORT).show();
                }

            }


        });


        but_selectmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(addActivity.this);
                mBuilder.setTitle("Select the participating members: ");

                membername= new String[memberlist.size()];

                // ArrayList to Array Conversion
                for (int j = 0; j < memberlist.size(); j++) {

                    // Assign each value to String array
                    membername[j] = memberlist.get(j);
                }

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
