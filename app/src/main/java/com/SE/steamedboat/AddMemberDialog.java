package com.SE.steamedboat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddMemberDialog extends AppCompatDialogFragment {
    private FirebaseDatabase FD;
    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener AuthListen;
    private DatabaseReference myRef;
    private String userID;
    private Button but_addmem;
    private EditText member_name;
    private int TripID;
    private AddMemberDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.activity_add_member, null);

        builder.setView(view)
                .setTitle("Add Member")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String username = member_name.getText().toString();
                        if(!TextUtils.isEmpty(member_name.getText().toString()))
                            listener.applyTexts(username);
                    }
                });
        member_name = view.findViewById(R.id.inputMemName);


        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    try {
        listener = (AddMemberDialogListener) context;
    }catch(ClassCastException e){
        throw new ClassCastException(context.toString()+ "Must implement  AddMemberDialogListener");
    }
    }

    public interface AddMemberDialogListener {
        void applyTexts(String username);
    }


}
