package com.example.android.currentcconverter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ngtrnhan1205 on 10/25/17.
 */

public class CurrentCAdapter extends RecyclerView.Adapter<CurrentCAdapter.CurrentCViewHolder> implements Filterable{

    private ArrayList<CurrentC> originalList;
    private ArrayList<CurrentC> filteredList;
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int postion);
    }

    // Set up a ViewHolder to enhance scrolling performance
    class CurrentCViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameTextView;
        TextView abbrTextView;
        ImageView flagImageView;
        public CurrentCViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            abbrTextView = itemView.findViewById(R.id.abbrTextView);
            flagImageView = itemView.findViewById(R.id.flagImageView);
            itemView.setOnClickListener(this);
        }

        void bindData(CurrentC currentC) {
            nameTextView.setText(currentC.getCurrentCName());
            abbrTextView.setText(currentC.getCurrentCAbbreviations());
            flagImageView.setImageResource(currentC.getFlagResourcesId());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnClickListener.onListItemClick(position);
        }
    }

    // CurrentCAdapter constructor
    public CurrentCAdapter (ArrayList<CurrentC> currentCArrayList, ListItemClickListener listener) {
        originalList = currentCArrayList;
        filteredList = currentCArrayList;
        mOnClickListener = listener;
        //Reset filteredPosArr
        for (int i = 0; i < 200; ++i) MainActivity.filteredPosArr[i] = i;
    }

    @Override
    public CurrentCViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item, parent, false);
        CurrentCViewHolder viewHolder = new CurrentCViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CurrentCViewHolder holder, int position) {
        holder.bindData(filteredList.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }


    private boolean match(CharSequence constraint, String data) {
        // Checking if data has characters of constraint (both upper and lower case
        int limit = 60;
        int[] distribution = new int[limit];
        for (int i = 0; i < constraint.length(); ++i) {
            int charNum = (int) constraint.charAt(i);
            if (charNum > 64 && charNum < 91) {
                distribution[charNum - 65]++;
                distribution[charNum - 39]++;
            }
            if (charNum > 96 && charNum < 123) {
                distribution[charNum - 71]++;
                distribution[charNum - 97]++;
            }
        }
        for (int i = 0; i < data.length(); ++i){
            int charNum = (int) data.charAt(i);
            if (charNum > 64 && charNum < 91) {
                distribution[charNum - 65]--;
                distribution[charNum - 39]--;
            }
            if (charNum > 96 && charNum < 123) {
                distribution[charNum - 71]--;
                distribution[charNum - 97]--;
            }
        }
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
                    int counter = 0;

                    for (int i = 0; i < originalList.size(); ++i) {
                        CurrentC object = originalList.get(i);
                        if (match(constraint, object.getCurrentCName()) ||
                                match(constraint, object.getCurrentCAbbreviations())){
                            filteredData.add(object);
                            MainActivity.filteredPosArr[counter++] = i;
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
