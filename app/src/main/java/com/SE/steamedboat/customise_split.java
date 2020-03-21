package com.SE.steamedboat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class customise_split extends AppCompatActivity {

    private static final String TAG = "customise_split";
    private ArrayList<String> consumernames = new ArrayList<>();
    private ArrayList<Consumer> consumers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customise_split);
        Log.d(TAG, "onCreate: Started.");
        ListView mListView = (ListView) findViewById(R.id.LVspenderamount);

        Intent from_activity = getIntent();
        consumernames = from_activity.getStringArrayListExtra("memberlist");

        for(int n = 0; n<consumernames.size(); n++){
            Consumer c  = new Consumer(consumernames.get(n), 0);
            consumers.add(c);

        }

        Customer_List_Adaptor adapter = new Customer_List_Adaptor(this, R.layout.adaptor_spenderamount, consumers);
        mListView.setAdapter(adapter);
    }





}
