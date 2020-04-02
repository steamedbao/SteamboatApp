package com.SE.steamedboat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.SE.steamedboat.Entity.Consumer;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

//import android.support.annotation.NonNull;

public class Customer_List_Adaptor extends ArrayAdapter<Consumer> {

    private static final String TAG = "PersonListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView name;
        TextInputEditText amount;
    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public Customer_List_Adaptor(Context context, int resource, ArrayList<Consumer> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the persons information
        String name = getItem(position).getName();
        Float amount = getItem(position).getAmount();

        //Create the person object with the information
        Consumer person = new Consumer(name,amount);

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;


        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.consumername);
            holder.amount = (TextInputEditText) convertView.findViewById(R.id.amount_consumed);

            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        /*
        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position; */

        holder.name.setText(person.getName());
        //holder.amount.setText(person.getAmount());


        return convertView;
    }
}