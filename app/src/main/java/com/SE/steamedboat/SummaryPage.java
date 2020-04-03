package com.SE.steamedboat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;

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

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class SummaryPage extends AppCompatActivity {
    private String TripID="0000";
    private FirebaseDatabase FD;
    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener AuthListen;
    private DatabaseReference myRef;
    private DatabaseReference TripRef;
    private String userID;
    private Trip currentTrip;
    private ArrayList<Trip> ALtrip = new ArrayList<>();
    private ArrayList<Member> ALmember = new ArrayList<Member>();
    protected ArrayList<String> ALtoshow = new ArrayList<>();
    protected ArrayList<String> ALmemnames = new ArrayList<>();

    private ArrayList<String> AL_activity_names = new ArrayList<>();
    private ArrayList<String> AL_activity_UID = new ArrayList<>();
    private ArrayAdapter<String> actAdapter=null;
    private ArrayAdapter<String> memAdapter=null;
    private ArrayList <Float> ALpaid;
    private ArrayList <Float> ALinc;
    private ArrayList <Float> ALowe = new ArrayList<>();
    private ArrayList <Float> AL_Amount_to_pay = new ArrayList<>();
    protected ArrayList<String> Solution = new ArrayList<>();

    private boolean checkDB;
    private int memCount;
    private boolean even=false;

    private ImageButton b1;
    private TextView b2;
    private ImageButton b3;
    private TextView b4;
    private ListView listView;
    private String homeCurrency = "SGD";
    private Button back;
    float sum=0;
    int size = 0;
    DecimalFormat numberFormat = new DecimalFormat("#.00");

    public void GetMembers(){

        TripRef.child("members").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                Member mm = new Member();
                mm = dataSnapshot.getValue(Member.class);
                ALmember.add(mm);
                memAdapter.notifyDataSetChanged();
                Log.v("E_lVALUE", "-------------------ADDED MEMBER: "+ mm.getMemberName() +"  ---------------------------");
                Log.v("Action", "-------------------Check ALMember size: "+ ALmember.size()+" ---------------------------");
                Log.v("Members", "---------------Name: "+ mm.getMemberName()+ " Overpaid/Owe: "+
                        (mm.getAmountPaid()-mm.getAmountIncurred())+"---------------------------");
                ALtoshow.add(mm.getMemberName());



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
    }

    public void GetMembers2(){

        TripRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentTrip = dataSnapshot.getValue(Trip.class);
                if (currentTrip!=null){
                    homeCurrency = currentTrip.getHomeCurrency();}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void test_print(){
        if (ALmember.size()!=0){
            for (int ind =0; ind<ALmember.size(); ind++){
                Log.v("Members", "---------------Name: "+ ALmember.get(ind).getMemberName()+ " Overpaid/Owe: "+
                        (ALmember.get(ind).getAmountPaid()-ALmember.get(ind).getAmountIncurred())+"---------------------------");
            }
        }
        else {        Log.v("Action", "-------------------NOOOOO ! ALMember size: "+ ALmember.size()+" ---------------------------");
        }
    }

    public void Generate_payment_solution(){

        ArrayList<String> newMemAL = new ArrayList<>();
        ArrayList<Float> ALClone =  (ArrayList<Float>) ALowe.clone();
        Collections.sort(ALowe);
        Collections.reverse(ALowe);

        GetMembers2();

        int index=0;
        Float temp;
        String S_temp;

        for (index=0;index<memCount;index++){
            temp = ALowe.get(index);
            int oldindex = ALClone.indexOf(temp);
            newMemAL.add(ALmemnames.get(oldindex));
        }
        float checkSum = 0;

        for (index=0;index<memCount;index++){
            Log.v("", "-------------- NewALmem: " + newMemAL.get(index)+"-------------");
            Log.v("", "-------------- ALowe: " + ALowe.get(index)+"-------------");
            checkSum+=ALowe.get(index);
        }

        String mem_positive;
        String mem_negative;
        int cur_pos=0;
        int cur_neg=memCount-1;
        float cur_trans;
        Solution.clear();
        if(Math.abs(checkSum)>0.1){
            Solution.add("Please make sure your sums of all expenses and payments add up");
            Solution.add("Now for all members, total expense != total amount paid, please check !");
        }

        else {
            while (even == false) {

                if (ALowe.get(cur_pos) <= 0.01) {
                    cur_pos++;
                    if (cur_pos == cur_neg) {
                        even = true;
                    }
                    continue;
                }

                if (ALowe.get(cur_neg) == 0) {
                    cur_neg--;
                }

                cur_trans = Math.min(ALowe.get(cur_pos), Math.abs(ALowe.get(cur_neg)));
                ALowe.set(cur_pos, ALowe.get(cur_pos) - cur_trans);
                ALowe.set(cur_neg, ALowe.get(cur_neg) + cur_trans);
                Solution.add(newMemAL.get(cur_neg) + "  pays  " + newMemAL.get(cur_pos) + "  " + cur_trans + "  " + homeCurrency);
            }
        }

        for (index=0;index<Solution.size();index++){
            Log.v("Solutions", "--------------  " + Solution.get(index)+"-------------");
        }

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_summary);

        Intent from_home = getIntent();
        TripID = from_home.getStringExtra("TripID");
        ALmemnames = from_home.getStringArrayListExtra("Membernames");
        ALmember = (ArrayList<Member>) from_home.getSerializableExtra("ALmember");

        Auth = FirebaseAuth.getInstance();
        FD = FirebaseDatabase.getInstance();
        myRef = FD.getReference();
        FirebaseUser user = Auth.getCurrentUser();
        userID = user.getUid();
        TripRef = myRef.child("Trips").child(TripID);

        memAdapter = new ArrayAdapter<String>(this, R.layout.cust_list_view, ALtoshow);

        Log.v("Action", "-------------------Check ALMember size: "+ ALmember.size()+" ---------------------------");

        memCount = ALmember.size();

        if (memCount<=1)
        {
            b3.setEnabled(false);
            b4.setEnabled(false);
        }

        ALtoshow.add("Name     Incurred    Paid    Expects +/-");

        for (int i=0;i<memCount;i++ ){

            String oneline = ALmember.get(i).getMemberName();

            int spacecount = Math.max(1,(11-(oneline.length())));
            for (int space = 0; space<spacecount;space++){
                oneline = oneline+" ";
            }

            oneline = oneline+numberFormat.format(ALmember.get(i).getAmountIncurred());
            int spacecount2 = Math.max(1,(10-numberFormat.format(ALmember.get(i).getAmountIncurred()).length()));
            for (int space = 0; space<spacecount2;space++){
                oneline = oneline+" ";
            }

            oneline = oneline+numberFormat.format(ALmember.get(i).getAmountPaid());
            int spacecount3 = Math.max(1,(10-numberFormat.format(ALmember.get(i).getAmountPaid()).length()));
            for (int space = 0; space<spacecount3;space++){
                oneline = oneline+" ";
            }

            if(ALmember.get(i).getAmountPaid()-ALmember.get(i).getAmountIncurred()>=0){oneline=oneline+" ";}

            ALowe.add(ALmember.get(i).getAmountPaid()-ALmember.get(i).getAmountIncurred());

            ALtoshow.add(oneline+numberFormat.format((ALmember.get(i).getAmountPaid()-ALmember.get(i).getAmountIncurred())));


        }

        for (int i=0;i<=memCount;i++ ) {
            Log.v("", "-----------"+ ALtoshow.get(i)+"-----------");

        }

        Generate_payment_solution();

        b1 = findViewById(R.id.button1);
        b2 = findViewById(R.id.button2);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sum=0;
                for (int i=0;i<ALmember.size();i++)
                {
                    sum += ALmember.get(i).getAmountPaid();
                }
                show_stat(currentTrip.getCreaterName(),sum,ALmember.size());
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sum=0;
                for (int i=0;i<ALmember.size();i++)
                {
                    sum += ALmember.get(i).getAmountPaid();
                }
                show_stat(currentTrip.getCreaterName(),sum,ALmember.size());
            }
        });


        b3 = findViewById(R.id.button3);
        b4 = findViewById(R.id.button4);

        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_solution(Solution);
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_solution(Solution);
            }
        });


        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.left_align_view, ALtoshow);
        listView = findViewById(R.id.listview);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }


    public void show_solution(ArrayList<String> als){
        Intent go_to_sum = new Intent(getApplicationContext(), Payment_solution.class);
        go_to_sum.putStringArrayListExtra("sol",als);
        startActivity(go_to_sum);
    }

    public void show_stat(String name, float sum,int size){
        Intent go_to_stat = new Intent(getApplicationContext(), OverallStat.class);
        go_to_stat.putExtra("name",name);
        String str1 = Float.toString(sum);
        String str2 = Integer.toString(size);
        go_to_stat.putExtra("sum",str1);
        go_to_stat.putExtra("size",str2);
        startActivity(go_to_stat);
    }


}
