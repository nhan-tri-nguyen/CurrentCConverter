package com.example.android.currentcconverter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ngtrnhan1205 on 10/25/17.
 */

public class CurrentCAdapter extends ArrayAdapter<CurrentC> {

    //Set up CurrentCAdapter
    public CurrentCAdapter (Activity context, ArrayList<CurrentC> currentCArrayList) {
        super(context, 0, currentCArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Check if the existing view is used, if not produces one
        View listItemView = convertView;

        if (listItemView == null) {
            //Inflate using list_item.xml as layout
            listItemView = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.list_item, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        CurrentC currentC = getItem(position);

        //Set nameTextView in list_item
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.nameTextView);
        nameTextView.setText(currentC.getCurrentCName());

        //Set abbrTextView in list_item
        TextView abbrTextView = (TextView) listItemView.findViewById(R.id.abbrTextView);
        abbrTextView.setText(currentC.getCurrentCAbbreviations());

        //Set image to flagImageView
        ImageView flagImageView = (ImageView) listItemView.findViewById(R.id.flagImageView);
        flagImageView.setImageResource(currentC.getFlagResourcesId());

        return listItemView;
    }
}
