package com.SE.steamedboat;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class addActivity extends AppCompatActivity {

    private FirebaseDatabase FD;
    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener AuthListen;
    private DatabaseReference myRef;
    private String userID;
    private Calendar cd;

    private Button but_add;
    private Button but_selectmember;
    private ListView memberLV;
    private ArrayList<Member> members = new ArrayList<>();
    private ArrayList<String> memberlist = new ArrayList<>();
    private CharSequence[] membername;
    private DatabaseReference tripRef;
    private boolean[] checkeditems;
    private ArrayList<Integer> memberSelected = new ArrayList<>();
    private Date d1;
    private String selected_payer;
    private EditText expense;
    private float exp;
    private String currency;

    private EditText activity_name;
    private int TripID;
    private Button back;
    private Button but_date;
    private TextView mDisplayDate;
    private static final String TAG = "MainActivity";
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private Spinner select_payer;
    private Member memtemp;
    private ArrayList<String> selected_names=new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private boolean date_is_set = false;
    private Calendar cal = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        memberLV = findViewById(R.id.LV_mem_names);
        arrayAdapter = new ArrayAdapter(this,R.layout.cust_list_view_2,selected_names);
        memberLV.setAdapter(arrayAdapter);
        Intent from_home = getIntent();
        if (from_home.getStringArrayListExtra("memberlist")!=null)
            memberlist = from_home.getStringArrayListExtra("memberlist");
        TripID = from_home.getIntExtra("ID", 1);

        but_add = (Button) findViewById(R.id.Button_add);
        back = (Button) findViewById(R.id.discardactivity);
        but_selectmember = findViewById(R.id.but_selectmem);
        expense = findViewById(R.id.expense);
        cd = Calendar.getInstance();

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

        // for date picker
        but_date = (Button) findViewById(R.id.datePicker);
        mDisplayDate = (TextView) findViewById(R.id.selectedDate);

// for spinner
        select_payer = (Spinner) findViewById(R.id.payername);


// for spinner

        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, memberlist);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        select_payer.setAdapter(spinner_adapter);

        select_payer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//display user info
                selected_payer = (String) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        but_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        addActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);

                d1 = new Date(year-1900, month-1, day);
                date_is_set = true;
                Log.v("date", "---------  ------------------d1:"+d1);

            }
        };



        but_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name = activity_name.getText().toString();
                exp = Float.parseFloat(expense.getText().toString());

                if (name != "" && expense.getText().length()>0 && date_is_set ==true)//need more checks. but rn cant pass in the values for the others yet
                {


                    Activity a1 = new Activity(name);

                    Log.v("E_VALUE", "--------  Activity Name : " + a1.getName() + "---------------------------");

                    a1.setName(name+"__"+Integer.toString(a1.getId()));
                    a1.setDateTime(d1);
                    a1.setPayer(selected_payer);
                    a1.setActivityExpense(exp);
                    a1.setActivityCurrency(currency);
                    for(int n = 0; n < memberSelected.size(); n++){
                        a1.addParticipant(memberlist.get(memberSelected.get(n)));
                        /*
                        DatabaseReference memRef;
                        memRef = FD.getReference().child("Trips").child(Integer.toString(TripID)).child("members").child(memberlist.get(memberSelected.get(n)));
                        */
                    }
                    myRef = FD.getReference().child("Trips").child(Integer.toString(TripID)).child("activities").child(a1.getName());
                    myRef.setValue(a1);

                    selected_names = (ArrayList<String>)a1.getParticipant().clone();

                    memtemp = new Member();

                    // this is trying only split evenly !!!!! ----------------

                    FD.getReference().child("Trips").child(Integer.toString(TripID)).child("members").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            memtemp = dataSnapshot.getValue(Member.class);
                            if (selected_names.contains(memtemp.getMemberName())){
                                memtemp.addAmountIncurred(exp/selected_names.size()); // evenly here -----------==========
                                }
                            if (memtemp.getMemberName().equals(selected_payer)){
                                memtemp.addAmountPaid(exp);
                            }
                            FD.getReference().child("Trips").child(Integer.toString(TripID)).child("members").child(memtemp.getMemberName()).setValue(memtemp);
                            Log.v("Member", "----------------updated:"+memtemp.getMemberName()+"-------------------------");

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


                    Toast.makeText(addActivity.this, "ba Successfully", Toast.LENGTH_SHORT).show();

                    Intent gohome = new Intent (getApplicationContext(), Homepage.class);
                    gohome.putExtra("TripID", TripID);
                    startActivity(gohome);




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
                            selected_names.add(memberlist.get(memberSelected.get(i)));
                            arrayAdapter.notifyDataSetChanged();
                            Log.v("Select Member", "----------------size:"+selected_names.size()+ selected_names.get(i) +"-------------------------");

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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currency = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // for selecting splitting method
        Spinner spinnersplit = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> splitadapter = ArrayAdapter.createFromResource(this,
                R.array.splitmethods, android.R.layout.simple_spinner_item);
        splitadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnersplit.setAdapter(splitadapter);
        splitadapter.notifyDataSetChanged();




        spinnersplit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected_splitmethod = parent.getSelectedItem().toString();
                if(selected_splitmethod.equals("Others")){

                    /*
                    ArrayList<Consumer> consumers = new ArrayList<>();
                    for(int n=0; n < memberlist.size(); n++){
                        Consumer c = new Consumer(memberlist.get(n), 0);
                        consumers.add(c);
                    }*/

                    Intent gotosplit = new Intent(getApplicationContext(), customise_split.class);
                    gotosplit.putStringArrayListExtra("memberlist", memberlist);
                    startActivity(gotosplit);

                    /*Customer_List_Adaptor adapter = new Customer_List_Adaptor(addActivity.this, R.layout.adaptor_spenderamount, consumers);
                    mListView.setAdapter(adapter);

                     */
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // for selecting splitting method end


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
