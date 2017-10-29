package com.example.android.currentcconverter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ngtrnhan1205 on 10/25/17.
 */

public class CurrentCAdapter extends ArrayAdapter<CurrentC> implements Filterable{

    //Set up a ViewHolder to enhance scrolling performance
    private static class ViewHolder {
        TextView nameTextView;
        TextView abbrTextView;
        ImageView flagImageView;
    }

    private ArrayList<CurrentC> originalList;
    private ArrayList<CurrentC> filteredList;

    //Set up CurrentCAdapter
    public CurrentCAdapter (Activity context, ArrayList<CurrentC> currentCArrayList) {
        super(context, 0, currentCArrayList);
        originalList = currentCArrayList;
        filteredList = currentCArrayList;
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Nullable
    @Override
    public CurrentC getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Check if the existing view is used, if not produces one
        View listItemView = convertView;
        ViewHolder viewHolder; //Look up cache stored in tag
        if (listItemView == null) {
            //Inflate using list_item.xml as layout
            listItemView = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();

            //Initialize View to set texts and images
            viewHolder.nameTextView = listItemView.findViewById(R.id.nameTextView);
            viewHolder.abbrTextView = listItemView.findViewById(R.id.abbrTextView);
            viewHolder.flagImageView = listItemView.findViewById(R.id.flagImageView);
            listItemView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) listItemView.getTag();
        }

        // Get the object located at this position in the list
        CurrentC currentC = filteredList.get(position);

        //Set nameTextView in list_item
        viewHolder.nameTextView.setText(currentC.getCurrentCName());

        //Set abbrTextView in list_item
        viewHolder.abbrTextView.setText(currentC.getCurrentCAbbreviations());

        //Set image to flagImageView in list_item
        viewHolder.flagImageView.setImageResource(currentC.getFlagResourcesId());

        return listItemView;
    }

    private boolean match(CharSequence constraint, String data) {
        int limit = 10000;
        int[] distribution = new int[limit];
        for (int i = 0; i < constraint.length(); ++i)
            distribution[(int)constraint.charAt(i)]++;
        for (int i = 0; i < data.length(); ++i)
            distribution[(int)data.charAt(i)]--;
        for (int i =0; i < limit; ++i)
            if (distribution[i] > 0) return false;
        return true;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                if (constraint == null || constraint.length() == 0){
                    filterResults.values = originalList;
                    filterResults.count = originalList.size();
                }
                else {
                    ArrayList<CurrentC> filteredData = new ArrayList<>();

                    for (int i = 0; i < originalList.size(); ++i) {
                        CurrentC object = originalList.get(i);
                        if (match(constraint, object.getCurrentCName()) ||
                                match(constraint, object.getCurrentCAbbreviations())){
                            filteredData.add(object);
                        }
                    }
                    filterResults.values = filteredData;
                    filterResults.count = filteredData.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<CurrentC>) results.values;
                notifyDataSetChanged();
            }
        };

    }
}
