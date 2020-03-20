package com.SE.steamedboat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MemberDialog extends AppCompatActivity {
    TextView memberName;
    Button deleteMem;
    Button backToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_memberdialog);

        Intent intent = getIntent();
        String name = intent.getStringExtra("namedetail");

        memberName = (TextView) findViewById(R.id.member_name);
        memberName.setText(name);
        TextView memberExpense;
        TextView memberPaid;

        deleteMem = (Button) findViewById(R.id.member_delete);
        backToHome = (Button) findViewById(R.id.back);

        deleteMem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.alert)
                .setTitle("Deleting Member")
                .setMessage("Are you sure you want to delete this member permanently?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
