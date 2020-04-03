package com.SE.steamedboat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.SE.steamedboat.Entity.Activity;
import com.SE.steamedboat.Entity.Member;
import com.SE.steamedboat.Entity.Trip;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityDialog extends AppCompatActivity {

    private FirebaseDatabase FD;
    private DatabaseReference myRef;
    private DatabaseReference ActRef;

    TextView Name, Split, Expense, Paid, Status, Currency;
    Button edit;
    Button settle;
    Button backToHome;
    Activity thisAct=new Activity();
    String UID;
    String TripID;
    Member temp = new Member();
    ArrayList<String> Participants = new ArrayList<>();
    ArrayList<Float> Expenses = new ArrayList<>();
    ArrayList<Float> TotalExp = new ArrayList<>();
    ArrayList<Float> Totalpaid = new ArrayList<>();
    Float PayerTotalPaid;
    String HC;
    Trip tempTrip = new Trip();
    ArrayList<String> memberAL = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_activity_dialog);

        Intent intent = getIntent();
        UID = intent.getStringExtra("directory");
        TripID = intent.getStringExtra("TripID");

        FD = FirebaseDatabase.getInstance();
        myRef = FD.getReference();
        ActRef = myRef.child("Trips").child(TripID).child("activities").child(UID);

        Name = (TextView) findViewById(R.id.D_act_name_value);
        Expense = (TextView) findViewById(R.id.D_act_expense_value);
        Split = (TextView) findViewById(R.id.D_act_split_value);
        Paid = (TextView) findViewById(R.id.D_act_paidby_value);
        Status = (TextView) findViewById(R.id.textView3);

        edit = (Button) findViewById(R.id.D_activity_edit);
        settle = (Button) findViewById(R.id.d_activity_settle);
        backToHome = (Button) findViewById(R.id.back);
        Currency = findViewById(R.id.textView5);

        myRef.child("Trips").child(TripID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tempTrip = dataSnapshot.getValue(Trip.class);
                HC = tempTrip.getHomeCurrency();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ActRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                thisAct = dataSnapshot.getValue(Activity.class);
                if (thisAct!=null){
                    Name.setText(thisAct.getName());
                    Expense.setText(Float.toString(thisAct.getActivityExpense()));
                    Currency.setText(thisAct.getActivityCurrency());
                    Participants = thisAct.getParticipant();
                    Expenses = thisAct.getIndividualExpense();

                    float zero =0;
                    for (int i =0;i<Expenses.size();i++){
                        TotalExp.add(zero);
                        Totalpaid.add(zero);
                    }


                    if (thisAct.getSplit()==true)
                        Split.setText("Evenly");
                    else
                        Split.setText("Customised Splitting");

                    Paid.setText(thisAct.getPayer());
                    if(thisAct.getStatus()){Status.setText("Pending");}
                    else Status.setText("Settled");

                    myRef.child("Trips").child(TripID).child("members").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            temp = dataSnapshot.getValue(Member.class);
                            memberAL.add(temp.getMemberName());
                            if (Participants.contains(temp.getMemberName()))
                            {
                                int index = Participants.indexOf(temp.getMemberName());
                                TotalExp.set(index,temp.getAmountIncurred());
                                Totalpaid.set(index, temp.getAmountPaid());
                                if (thisAct.getPayer().equals(temp.getMemberName())){
                                    PayerTotalPaid = temp.getAmountPaid();
                                }

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
                    Log.v("Updating AL","-----------------Total Expenses size is" + TotalExp.size());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /// put your own code here ------------------------------

                onEditPressed();

            }
        });

        settle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /// put your own code here ------------------------------

                // set activity status to false

                settle();


            }
        });

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onEditPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.alert)
                .setTitle("Edit Activity")
                .setMessage("Confirm to edit this activity by replacing it with a new one?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent gotoedit = new Intent(getApplicationContext(), EditActivity.class);
                        gotoedit.putStringArrayListExtra("Participants",Participants);
                        gotoedit.putExtra("Exp",thisAct.getActivityExpense());
                        gotoedit.putExtra("Payer",thisAct.getPayer());
                        gotoedit.putExtra("Date",thisAct.getDateTime());
                        gotoedit.putExtra("Indiv_exp",Expenses);
                        gotoedit.putExtra("Currency", thisAct.getActivityCurrency());
                        gotoedit.putExtra("name", thisAct.getName());

                        gotoedit.putExtra("ID",Integer.parseInt(TripID));
                        gotoedit.putExtra("HC", HC);
                        gotoedit.putStringArrayListExtra("memberlist",memberAL);

                        for (int i =0; i<Participants.size();i++){
                            Log.v("Updating DB","-----------------Expenses size is" + Expenses.size());
                            myRef.child("Trips").child(TripID).child("members").child(Participants.get(i)).child("amountIncurred").setValue(TotalExp.get(i)-Expenses.get(i)*thisAct.getExchangeRate());

                            if (thisAct.getPayer().equals(Participants.get(i))){
                                myRef.child("Trips").child(TripID).child("members").child(Participants.get(i)).child("amountPaid").setValue(PayerTotalPaid-thisAct.getHomeWorth());

                            }
                        }

                        myRef.child("Trips").child(TripID).child("activities").child(thisAct.getName()).removeValue();

                        /*if (from_home.getStringArrayListExtra("memberlist") != null)
                            memberlist = from_home.getStringArrayListExtra("memberlist");
                        TripID = from_home.getIntExtra("ID", 1);
                        HomeCurrency = from_home.getStringExtra("HC");*/
                        startActivity(gotoedit);
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    public void settle(){

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.alert)
                .setTitle("Settle Activity")
                .setMessage("Confirm to settle this activity?")
                .setPositiveButton("Yes",new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        myRef.child("Trips").child(TripID).child("activities").child(thisAct.getName()).child("status").setValue(false);

        for (int i =0; i<Participants.size();i++){
            Log.v("Updating DB","-----------------Expenses size is" + Expenses.size());

            if (thisAct.getPayer().equals(Participants.get(i))){
                Log.v("settle","-----------------amount updated after settling is (for payer)" + (PayerTotalPaid - (thisAct.getHomeWorth() - Expenses.get(i)*thisAct.getExchangeRate())));

                myRef.child("Trips").child(TripID).child("members").child(Participants.get(i)).child("amountPaid").setValue(PayerTotalPaid - (thisAct.getHomeWorth() - Expenses.get(i)*thisAct.getExchangeRate()));
                //myRef.child("Trips").child(TripID).child("members").child(Participants.get(i)).child("amountPaid").setValue(8888);
            }
            else{
                Log.v("settle","-----------------amount updated after settling is (for payee)" + (Totalpaid.get(i) + Expenses.get(i)*thisAct.getExchangeRate()));
               // myRef.child("Trips").child(TripID).child("members").child(Participants.get(i)).child("amountPaid").setValue(7777);
                myRef.child("Trips").child(TripID).child("members").child(Participants.get(i)).child("amountPaid").setValue(Totalpaid.get(i) + Expenses.get(i)*thisAct.getExchangeRate());
            }


        }

        Intent gohome = new Intent (getApplicationContext(), Homepage.class);
        gohome.putExtra("TripID", Integer.parseInt(TripID));
        startActivity(gohome);

                    }

                })
                .setNegativeButton("No", null)

                .show();

    }

}
