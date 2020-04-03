package com.SE.steamedboat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.SE.steamedboat.Entity.Activity;
import com.SE.steamedboat.Entity.Member;
import com.SE.steamedboat.Entity.Trip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
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
    private Trip currentTrip = new Trip();
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
    private Button viewSummary;
    private String homeCurrency = "SGD";
    private Button inactiveTrip;
    private int pos;
    DecimalFormat numberFormat = new DecimalFormat("#.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Intent from_createORjoin = getIntent();
        TripID = from_createORjoin.getIntExtra("TripID",100000);
        pos = from_createORjoin.getIntExtra("Position",0);
        addMember = findViewById(R.id.addmember);
        btnLogout = findViewById(R.id.logout);
        TripIDDisplay = findViewById(R.id.hometripid);
        String tripIDdisplay = "ID"+Integer.toString(TripID);

        ViewAll = (Button) findViewById(R.id.ViewALL);
        TripIDDisplay.setText(tripIDdisplay);
        TripNameDisplay = findViewById(R.id.hometripname);
        TripName = from_createORjoin.getStringExtra("TripName");
        TripNameDisplay.setText(TripName);
        back = findViewById(R.id.back);
        viewSummary=findViewById(R.id.checkFinance);
        inactiveTrip= findViewById(R.id.editTrip);

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



        TripRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                currentTrip=dataSnapshot.getValue(Trip.class);

                if (!currentTrip.isOngoing())
                {
                    addMember.setEnabled(false);
                    addMember.setBackground(getDrawable(R.drawable.gradient2));
                    addActivity.setEnabled(false);
                    addActivity.setBackground(getDrawable(R.drawable.gradient2));

                }
                ALtrip.add(currentTrip);
                Log.v("E_VALUE", "-------------AFTER ADD ------AL size is: "+ ALtrip.size()+"  ---------------------------");

                //Log.v("E_VALUE", "-------------------Trip Name is: "+ currentTrip.getTripName() +"  ---------------------------");
                Log.v("E_VALUE", "-------------------dataSnapshot.getValue() is: "+ dataSnapshot.getValue() +"  ---------------------------");
                TVtripname.setText(currentTrip.getTripName());
                homeCurrency = currentTrip.getHomeCurrency();
                Log.v("E_VALUE", "-------------------Home Currency is: "+ homeCurrency +"  ---------------------------");

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (AL_activity_names.isEmpty()||ALmembernames.size()<=1) {
            viewSummary.setEnabled(false);
            viewSummary.setBackground(getDrawable(R.drawable.gradient2));
        }


        if (!AL_activity_names.isEmpty()&&ALmembernames.size()>1) {
            viewSummary.setEnabled(true);
            ViewAll.setEnabled(true);
            ViewAll.setBackground(getDrawable(R.drawable.gradient1));
            viewSummary.setBackground(getDrawable(R.drawable.gradient1));
        }

        viewSummary.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent goSummary = new Intent(Homepage.this, SummaryPage.class);
                   goSummary.putExtra("TripID",Integer.toString(TripID));
                   goSummary.putStringArrayListExtra("Membernames",ALmembernames);
                   goSummary.putExtra("ALmember", ALmember);
                   startActivity(goSummary);
               }
           });

        back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent goback = new Intent(Homepage.this, createORjoin.class);
                        startActivity(goback);
                    }
                });


        inactiveTrip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            { new AlertDialog.Builder(Homepage.this)
                    .setIcon(R.drawable.alert)
                    .setTitle("Ending Trip")
                    .setMessage("Are you sure you want to end this trip?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TripRef.child("ongoing").setValue(false);
                            myRef.child("Users").child(userID).child("trips").child(Integer.toString(pos)).child("ongoing").setValue(false);
                            Intent intToCreate = new Intent(Homepage.this, createORjoin.class);
                            startActivity(intToCreate);
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();

            }

        });


        LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Call dialog to display detail
                //create dialog
                Log.v("Click on a name", "-------------------AL for member size is: "+ ALmember.size()+"  ---------------------------");

                String name = memAdapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(),MemberDialog.class);

                //create string called expense and payment here then pass it to the dialog box throught the below code

                String tripID = Integer.toString(TripID);

                intent.putExtra("Member",ALmember.get(position));

                intent.putExtra("namedetail",name);
                intent.putExtra("TripID",tripID);

                //intent.putExtra("expensedetail",expense);
                //intent.putExtra("paymentdetail",payment);
                startActivity(intent);
            }
        });



            /*TripRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    currentTrip = new Trip();
                    currentTrip=dataSnapshot.getValue(Trip.class);
                    ALtrip.add(currentTrip);
                    TVtripname.setText(currentTrip.getTripName());
                    homeCurrency = currentTrip.getHomeCurrency();
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
            });*/




            TripRef.child("members").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {

                    Member mm = new Member();
                    mm = dataSnapshot.getValue(Member.class);
                    ALmember.add(mm);
                    ALmembernames.add(mm.getMemberName());
                    memAdapter.notifyDataSetChanged();
                    if (!AL_activity_names.isEmpty()) {
                        viewSummary.setEnabled(true);
                        ViewAll.setEnabled(true);
                        ViewAll.setBackground(getDrawable(R.drawable.gradient1));
                        viewSummary.setBackground(getDrawable(R.drawable.gradient1));
                    }
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

                    String str;

                    if (CVclick==false && dataSnapshot.hasChildren())
                    {   a = dataSnapshot.getValue(Activity.class);
                        AL_activity_UID.add(a.getName());
                        if (a.getSplit()){str = "Even split";}
                        else {str = "Custom split";}
                        AL_activity_names.add(a.getName() + " Payer: "+a.getPayer()+ ", "+homeCurrency+": "+numberFormat.format(a.getHomeWorth())+ ", " + str);
                    }
                    // -----------------------------------------------------------

                    actAdapter.notifyDataSetChanged();
                    if (ALmember.size()>1) {
                        viewSummary.setEnabled(true);
                        ViewAll.setEnabled(true);
                        ViewAll.setBackground(getDrawable(R.drawable.gradient1));
                        viewSummary.setBackground(getDrawable(R.drawable.gradient1));

                    }

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
                            String str;
                            if (a.getSplit()){str = "Even split";}
                            else {str = "Custom split";}
                            AL_activity_names.add(a.getName() + " Payer: "+a.getPayer()+ ", "+homeCurrency+": "+numberFormat.format(a.getHomeWorth())+ ", " + str);
                            //AL_activity_names.add(a.getName() + " " + a.getActivityCurrency() + ": " + a.getActivityExpense() + "  " + a.getSplit());

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
            homeCurrency = currentTrip.getHomeCurrency();

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
            homeCurrency = currentTrip.getHomeCurrency();

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
                    String str;
                    if (a.getSplit()) str = "Evenly split";
                    else str = "Custom split";
                    AL_activity_names.add(a.getName() + " Payer: "+a.getPayer()+ ", "+homeCurrency+": "+numberFormat.format(a.getHomeWorth())+ ", " + str);
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
        gotoadd.putExtra("ID", TripID);
        gotoadd.putExtra("HC",currentTrip.getHomeCurrency());
        Log.v("Home Currenct","----------------------------------"+ currentTrip.getHomeCurrency());
        startActivity(gotoadd);
    }

}