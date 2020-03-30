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
import androidx.fragment.app.FragmentManager;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditActivity extends AppCompatActivity implements Custom_expense_dialog.ExpenseDialogListener{

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
    private String currency="SGD";

    private EditText activity_name;
    private int TripID;
    private Button back;
    private Button but_date;
    private TextView mDisplayDate;
    private static final String TAG = "MainActivity";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String HomeCurrency;
    private Spinner select_payer;
    private Member memtemp;
    private ArrayList<String> selected_names = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private boolean date_is_set = false;
    private Calendar cal = Calendar.getInstance();
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private float rate=1;
    private String exp_got_from_dialog;
    private int LV_pos=0;
    private ArrayList<Float> ALexp = new ArrayList<>();
    private ArrayList<String> ALdisplay = new ArrayList<>();
    private boolean custom_split=false;
    private float baseRate = 1;
    private float quotedRate = 1;
    private String activityname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("check rate", "------------------------ line 94 rate is: " +rate);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        memberLV = findViewById(R.id.LV_mem_names);
        arrayAdapter = new ArrayAdapter(this, R.layout.cust_list_view_2, ALdisplay);
        memberLV.setAdapter(arrayAdapter);
        Intent from_home = getIntent();
        if (from_home.getStringArrayListExtra("memberlist") != null)
            memberlist = from_home.getStringArrayListExtra("memberlist");
        TripID = from_home.getIntExtra("ID", 1);
        HomeCurrency = from_home.getStringExtra("HC");
        activityname = from_home.getStringExtra("name");

       /* gotoedit.putStringArrayListExtra("Participants",Participants);
        gotoedit.putExtra("Exp",thisAct.getActivityExpense());
        gotoedit.putExtra("Payer",thisAct.getPayer());
        gotoedit.putExtra("Date",thisAct.getDateTime());
        gotoedit.putExtra("Indiv_exp",thisAct.getIndividualExpense());
        gotoedit.putExtra("Currency", thisAct.getActivityCurrency());*/

       selected_names = from_home.getStringArrayListExtra("Participants");
        ALexp = (ArrayList<Float>)from_home.getSerializableExtra("Indiv_exp");
        exp = from_home.getFloatExtra("Exp", 0);
        selected_payer = from_home.getStringExtra("Payer");
        currency = from_home.getStringExtra("Currency");
        d1 = (Date)from_home.getSerializableExtra("Date");
        date_is_set = true;

        but_add = (Button) findViewById(R.id.Button_add);
        back = (Button) findViewById(R.id.discardactivity);
        but_selectmember = findViewById(R.id.but_selectmem);
        expense = findViewById(R.id.expense);
        cd = Calendar.getInstance();
        expense.setText(Float.toString(exp));
        mDisplayDate = (TextView) findViewById(R.id.selectedDate);
        mDisplayDate.setText(d1.toString());


        checkeditems = new boolean[memberlist.size()];
        for (int i = 0; i < memberlist.size(); i++) {
            checkeditems[i] = false;
        }

        Auth = FirebaseAuth.getInstance();
        FD = FirebaseDatabase.getInstance();
        tripRef = FD.getReference().child("Trips").child(Integer.toString(TripID));

        final FirebaseUser user = Auth.getCurrentUser();
        userID = user.getUid();

        activity_name = (EditText) findViewById(R.id.activityname);
        activity_name.setText(activityname);

        if (expense.getText().length()>0){
            exp = Float.parseFloat(expense.getText().toString());}

        for (int i = 0; i < selected_names.size(); i++) {
            ALdisplay.add(selected_names.get(i)+",   expense: "+Float.toString(ALexp.get(i)));
            arrayAdapter.notifyDataSetChanged();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // for date picker
        but_date = (Button) findViewById(R.id.datePicker);


// for spinner
        select_payer = (Spinner) findViewById(R.id.payername);


// for spinner

        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this,
                R.layout.spinnerview, memberlist);
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
                        EditActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
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

                d1 = new Date(year - 1900, month - 1, day);
                date_is_set = true;
                Log.v("date", "---------  ------------------d1:" + d1);

            }
        };



        memberLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Call dialog to display detail
                //create dialog
                if (custom_split){
                    String name = arrayAdapter.getItem(position);
                    Log.v("Dialog inputs", "----------------------------- name is "+ name + "-------------------------");
                    LV_pos=position;

                    openExpDialog();}


                //create string called expense and payment here then pass it to the dialog box throught the below code



                //intent.putExtra("expensedetail",expense);
                //intent.putExtra("paymentdetail",payment);
            }
        });



        but_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name = activity_name.getText().toString();

                float indiv_total=0;
                for (int i = 0; i<ALexp.size();i++){
                    indiv_total += ALexp.get(i);
                }

                if (Math.abs((exp-indiv_total))>1){
                    Toast.makeText(EditActivity.this, "Please make sure sum of individual expenses = total", Toast.LENGTH_SHORT).show();
                }

                else if (activity_name.getText()!=null && expense.getText().length()>0 && date_is_set ==true&& selected_payer!=null&&currency!=null&&memberSelected.size()!=0)//need more checks. but rn cant pass in the values for the others yet
                {

                    setRate();
                    Activity a1 = new Activity(name);

                    Log.v("E_VALUE", "--------  Activity Name : " + a1.getName() + "---------------------------");

                    exp = Float.parseFloat(expense.getText().toString());
                    a1.setName(name + "__" + Integer.toString(a1.getId()));
                    a1.setDateTime(d1);
                    a1.setPayer(selected_payer);
                    a1.setActivityCurrency(currency);
                    a1.setActivityExpense(exp);
                    a1.setExchangeRate(rate);
                    a1.setHomeWorth(exp*rate);
                    Log.v("check rate", "------------------------ line 253 rate is: " +rate);
                    a1.setSplit((!custom_split));
                    a1.setIndividualExpense(ALexp);

                    for (int n = 0; n < memberSelected.size(); n++) {
                        a1.addParticipant(memberlist.get(memberSelected.get(n)));
                        /*
                        DatabaseReference memRef;
                        memRef = FD.getReference().child("Trips").child(Integer.toString(TripID)).child("members").child(memberlist.get(memberSelected.get(n)));
                        */
                    }
                    myRef = FD.getReference().child("Trips").child(Integer.toString(TripID)).child("activities").child(a1.getName());
                    myRef.setValue(a1);

                    selected_names = (ArrayList<String>) a1.getParticipant().clone();

                    memtemp = new Member();

                    // this is trying only split evenly !!!!! ----------------

                    FD.getReference().child("Trips").child(Integer.toString(TripID)).child("members").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            memtemp = dataSnapshot.getValue(Member.class);

                            if (selected_names.contains(memtemp.getMemberName())) {
                                memtemp.addAmountIncurred(rate*ALexp.get(selected_names.indexOf(memtemp.getMemberName()))); // according to ALexp here -----------==========
                            }
                            if (memtemp.getMemberName().equals(selected_payer)) {
                                memtemp.addAmountPaid(rate*exp);
                            }
                            FD.getReference().child("Trips").child(Integer.toString(TripID)).child("members").child(memtemp.getMemberName()).setValue(memtemp);
                            Log.v("Member", "----------------updated:" + memtemp.getMemberName() + "-------------------------");

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


                    Toast.makeText(EditActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();

                    Intent gohome = new Intent(getApplicationContext(), Homepage.class);
                    gohome.putExtra("TripID", TripID);
                    startActivity(gohome);


                } else {

                    Toast.makeText(EditActivity.this, "Please enter all fields!", Toast.LENGTH_SHORT).show();
                }

            }


        });


        but_selectmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditActivity.this);
                mBuilder.setTitle("Select the participating members: ");

                membername = new String[memberlist.size()];

                // ArrayList to Array Conversion
                for (int j = 0; j < memberlist.size(); j++) {

                    // Assign each value to String array
                    membername[j] = memberlist.get(j);
                }

                mBuilder.setMultiChoiceItems(membername, checkeditems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if (isChecked) {
                            memberSelected.add(position);
                        } else {
                            memberSelected.remove((Integer.valueOf(position)));

                        }
                    }

                });


                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        selected_names.clear();
                        ALdisplay.clear();
                        ALexp.clear();

                        if (expense.getText().length()>0){
                            exp = Float.parseFloat(expense.getText().toString());}

                        for (int i = 0; i < memberSelected.size(); i++) {
                            item = item + membername[memberSelected.get(i)];
                            if (i != memberSelected.size() - 1) {
                                item = item + ", ";
                            }
                            selected_names.add(memberlist.get(memberSelected.get(i)));
                            ALexp.add(exp/memberSelected.size());
                            ALdisplay.add(memberlist.get(memberSelected.get(i))+",   expense: "+Float.toString(exp/memberSelected.size()));
                            arrayAdapter.notifyDataSetChanged();
                            Log.v("Select Member", "----------------size:" + selected_names.size() + selected_names.get(i) + "-------------------------");

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
                R.array.currency, R.layout.spinnerview);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                currency = parent.getSelectedItem().toString();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.exchangeratesapi.io/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

                getQuoted(currency);// pass in string from select spinner
                getBase(HomeCurrency);
                rate = (quotedRate/baseRate);
                Log.v("check rate", "------------------------ line 452 rate  is: " +rate);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        // for selecting splitting method
        Spinner spinnersplit = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> splitadapter = ArrayAdapter.createFromResource(this,
                R.array.splitmethods, R.layout.spinnerview);
        splitadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnersplit.setAdapter(splitadapter);
        splitadapter.notifyDataSetChanged();


        spinnersplit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected_splitmethod = parent.getSelectedItem().toString();
                if (selected_splitmethod.equals("Others")) {
                    /*
                    ArrayList<Consumer> consumers = new ArrayList<>();
                    for(int n=0; n < memberlist.size(); n++){
                        Consumer c = new Consumer(memberlist.get(n), 0);
                        consumers.add(c);
                    }*/
                    custom_split = true;

                    /*Customer_List_Adaptor adapter = new Customer_List_Adaptor(addActivity.this, R.layout.adaptor_spenderamount, consumers);
                    mListView.setAdapter(adapter);

                     */
                }

                if (selected_splitmethod.equals("Evenly")) {

                    custom_split = false;

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
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    public void setRate()
    {
        rate = quotedRate/baseRate;
        if(HomeCurrency==currency){rate=1;}
        Log.v("check rate", "------------------------ line 517 rate is: " +rate);

    }


    @Override
    public void applyText(String inputexp) {
        exp_got_from_dialog = inputexp;
        Log.v("Dialog inputs", "-----------------------------the exp is "+ exp_got_from_dialog + "-------------------------");

        String name = selected_names.get(LV_pos);
        ALexp.set(LV_pos,Float.parseFloat(inputexp));

        String newString = name+" expense: "+inputexp;
        //selected_names.remove(position);

        ALdisplay.set(LV_pos, newString);

        arrayAdapter.notifyDataSetChanged();

        Log.v("Dialog inputs", "----------------------------- in AL new String is "+ arrayAdapter.getItem(LV_pos) + "-------------------------");

    }

    public void openExpDialog(){
        Custom_expense_dialog expense_dialog = new Custom_expense_dialog();
        expense_dialog.show(getSupportFragmentManager(),"expense dialog");
    }

    private void getQuoted(String currency) {
        Call<CurrencyExchange2> call = jsonPlaceHolderApi.getCurrencyExchange2(currency);

        call.enqueue(new Callback<CurrencyExchange2>() {
            @Override
            public void onResponse(Call<CurrencyExchange2> call, Response<CurrencyExchange2> response) {
//                if(!response.isSuccessful()){
//                    System.out.println("Response:");
//                    System.out.println(response);
//                    textViewResult.setText("Code: " + response.code());
//                    return;
//                }


                //System.out.println("Page Found!!!!!!!!!!!!!");

                CurrencyExchange2 CurrencyRates = response.body();
                Log.v("check rate", "------------------------ line 94 rate is: " +CurrencyRates.getRates());
                quotedRate = CurrencyRates.getRates();
            }

            @Override
            public void onFailure(Call<CurrencyExchange2> call, Throwable t) {
                Toast.makeText(EditActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getBase(String currency) {
        Call<CurrencyExchange2> call = jsonPlaceHolderApi.getCurrencyExchange2(currency);

        call.enqueue(new Callback<CurrencyExchange2>() {
            @Override
            public void onResponse(Call<CurrencyExchange2> call, Response<CurrencyExchange2> response) {
//                if(!response.isSuccessful()){
//                    System.out.println("Response:");
//                    System.out.println(response);
//                    textViewResult.setText("Code: " + response.code());
//                    return;
//                }


                //System.out.println("Page Found!!!!!!!!!!!!!");

                CurrencyExchange2 CurrencyRates = response.body();
                Log.v("check rate", "------------------------ line 94 rate is: " +CurrencyRates.getRates());
                baseRate = CurrencyRates.getRates();
            }

            @Override
            public void onFailure(Call<CurrencyExchange2> call, Throwable t) {
                Toast.makeText(EditActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
