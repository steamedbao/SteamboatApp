package com.SE.steamedboat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import android.util.Log;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Ref;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Homepage extends AppCompatActivity implements AddMemberDialog.AddMemberDialogListener {
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
    protected ArrayList<String> ALmembernames = new ArrayList<>();
    private ArrayList<String> AL_activity_names = new ArrayList<>();
    private ArrayList<String> AL_activity_UID = new ArrayList<>();

    private TextView TVtripname;
    private ListView LV;
    private ListView LVactivity;
    private Button addMember;
    private Button addActivity;
    private Button ViewAll;
    private int TripID;
    private TextView TripIDDisplay;
    private TextView TripNameDisplay;
    private String TripName;
    private Button back;
    private boolean CVclick=false;
    private int chosenDay, chosenMonth, chosenYear;
    private ArrayAdapter<String> actAdapter=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Intent from_createORjoin = getIntent();
        TripID = from_createORjoin.getIntExtra("TripID",100000);
        addMember = findViewById(R.id.addmember);
        btnLogout = findViewById(R.id.logout);
        TripIDDisplay = findViewById(R.id.hometripid);
        TripIDDisplay.setText(String.valueOf(TripID));
        TripNameDisplay = findViewById(R.id.hometripname);
        TripName = from_createORjoin.getStringExtra("TripName");
        TripNameDisplay.setText(TripName);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent goback = new Intent(Homepage.this, createORjoin.class);
                startActivity(goback);
            }});


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
        actAdapter = new ArrayAdapter<String>(this, R.layout.cust_list_view, AL_activity_names);
        final ArrayAdapter<String> memAdapter = new ArrayAdapter<String>(this, R.layout.cust_list_view, ALmembernames);
        LV.setAdapter(memAdapter);
        LVactivity.setAdapter(actAdapter);
        TVtripname = (TextView) findViewById(R.id.hometripname);
        TripRef = myRef.child("Trips").child(Integer.toString(TripID));



        LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Call dialog to display detail
                //create dialog
                String name = memAdapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(),MemberDialog.class);

                //create string called expense and payment here then pass it to the dialog box throught the below code

                intent.putExtra("namedetail",name);
                //intent.putExtra("expensedetail",expense);
                //intent.putExtra("paymentdetail",payment);
                startActivity(intent);
            }
        });

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
                    currentTrip = new Trip();
                    currentTrip=dataSnapshot.getValue(Trip.class);
                    ALtrip.add(currentTrip);
                    Log.v("E_VALUE", "-------------AFTER ADD ------AL size is: "+ ALtrip.size()+"  ---------------------------");

                    //Log.v("E_VALUE", "-------------------Trip Name is: "+ currentTrip.getTripName() +"  ---------------------------");
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



            TripRef.child("activities").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {


                    // ------ use for loop to find activity of that date if the calender is clicked
                    Activity a = new Activity();
                    if (CVclick==false && dataSnapshot.hasChildren() )
                    {   a = dataSnapshot.getValue(Activity.class);
                        AL_activity_UID.add(a.getName());
                        AL_activity_names.add(a.getName() + " " + a.getActivityCurrency() + ": " + a.getActivityExpense() + "  " + a.getSplit());
                    }
                    // -----------------------------------------------------------

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


        CalendarView cv=(CalendarView)findViewById(R.id.tripCalendar);

        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                CVclick = true;
                AL_activity_names.clear();
                AL_activity_UID.clear();

                chosenDay = dayOfMonth;
                chosenMonth = month;
                chosenYear = year;

                TripRef.child("activities").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        Activity a = new Activity();
                        if (dataSnapshot.hasChildren())
                            a = dataSnapshot.getValue(Activity.class);

                        Log.v("date", "--------------When Clicked Calendar , DATA: "+ dataSnapshot.getValue() +"  ---------------------------");

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
                        String adate = sdf.format(a.getDateTime());
                        Log.v("date", "------------------- "+ adate +"  ---------------------------");

                        String cvdate = chosenDay+"/"+(chosenMonth+1)+"/"+chosenYear;
                        Log.v("date", "------------------- "+ cvdate +"  ---------------------------");

                        if (adate.compareTo(cvdate)==0) {
                            Log.v("date", "------------------- " + "true" + "  ---------------------------");
                            Log.v("date",a.getName() + " " + a.getActivityCurrency() + ": " + a.getActivityExpense() + "  " + a.getSplit());
                            AL_activity_UID.add(a.getName());
                            AL_activity_names.add(a.getName() + " " + a.getActivityCurrency() + ": " + a.getActivityExpense() + "  " + a.getSplit());

                        }
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
        });

        LVactivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Call dialog to display detail
                //create dialog
                String UID = AL_activity_UID.get(position);
                Intent intent = new Intent(getApplicationContext(),ActivityDialog.class);

                //create string called expense and payment here then pass it to the dialog box throught the below code

                intent.putExtra("directory",UID);
                intent.putExtra("TripID",Integer.toString(TripID));
                //intent.putExtra("expensedetail",expense);
                //intent.putExtra("paymentdetail",payment);
                startActivity(intent);
            }
        });


        addMember=findViewById(R.id.addmember);


        addActivity = findViewById(R.id.addActivity);

        ViewAll = (Button) findViewById(R.id.ViewALL);

        ViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CVclick=false;
                AL_activity_UID.clear();
                AL_activity_names.clear();
                Show_all_activities();

            }
        });

        addActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Goto_addActivity(ALmembernames);
            }
        });

        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
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



    public void openDialog(){
        AddMemberDialog addMemberDialog = new AddMemberDialog();
        addMemberDialog.show(getSupportFragmentManager(),"Add Member");
    }

    @Override
    public void applyTexts(String username)
    {
        Intent from_home = getIntent();
        TripID = from_home.getIntExtra("TripID",100000);
        Auth = FirebaseAuth.getInstance();
        FD = FirebaseDatabase.getInstance();
        final FirebaseUser user = Auth.getCurrentUser();
        userID = user.getUid();
        Member mem = new Member(username);
        myRef = FD.getReference().child("Trips").child(Integer.toString(TripID)).child("members").child(username);
        myRef.setValue(mem);

    }



    public void Action(){

        while (ALtrip.size()!=0) {

            TVtripname.setText(currentTrip.getTripName());
            Log.v("E_VALUE", "-------------------Home page trip name is set to: "+ currentTrip.getTripName() +"  ---------------------------");
            break;
        }
    }


    public void Show_all_activities(){

        TripRef.child("activities").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {


                // ------ use for loop to find activity of that date if the calender is clicked
                Activity a = new Activity();
                if (CVclick==false && dataSnapshot.hasChildren() )
                {   a = dataSnapshot.getValue(Activity.class);
                    AL_activity_UID.add(a.getName());
                    AL_activity_names.add(a.getName() + " " + a.getActivityCurrency() + ": " + a.getActivityExpense() + "  " + a.getSplit());
                }
                // -----------------------------------------------------------

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

    public void Goto_addActivity(ArrayList<String> names){
        Intent gotoadd = new Intent(getApplicationContext(), addActivity.class);
        ArrayList<String> name = ALmembernames;
        gotoadd.putExtra("memberlist", name);
        startActivity(gotoadd);
    }

}