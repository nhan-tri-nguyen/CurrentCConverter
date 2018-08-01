package com.promise.android.currentcconverter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.promise.android.currentcconverter.data.FavCurrentCContract;

/**
 * Created by ngtrnhan1205 on 11/20/17.
 */

public class FavCurrentCAdapter extends RecyclerView.Adapter<FavCurrentCAdapter.FavCurrentCViewHolder> {

    final private ListenerInterface mOnClickListener;
    private Context mContext;
    private Cursor mCursor;

    // Constructor
    public FavCurrentCAdapter(Context context, Cursor cursor, ListenerInterface listener) {

        this.mContext = context;
        this.mCursor = cursor;
        mOnClickListener = listener;
    }

    public FavCurrentCViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item, parent, false);

        return new FavCurrentCViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavCurrentCAdapter.FavCurrentCViewHolder holder, int position) {

        // Move the mCursor to the position of the item to be displayed
        if (!mCursor.moveToPosition(position)) {
            return; // bail if returned null
        }

        // Update the view holder with the information needed to display
        String abbr = mCursor.getString(mCursor.getColumnIndex(FavCurrentCContract.FavCurrentCEntry.COLUMN_ABBR));
        String amount = mCursor.getString(mCursor.getColumnIndex(FavCurrentCContract.FavCurrentCEntry.COLUMN_AMOUNT));
        int imgId = mCursor.getInt(mCursor.getColumnIndex(FavCurrentCContract.FavCurrentCEntry.COLUMN_IMG_RES_ID));
        long id = mCursor.getLong(mCursor.getColumnIndex(FavCurrentCContract.FavCurrentCEntry._ID));

        // Display the abbreviation
        holder.abbrTextView.setText(abbr);

        // Display the amount
        holder.amountTextView.setText(amount);

        // Display the flag
        Glide.with(mContext).load(imgId).into(holder.flagImageView);

        // Save the id to associate with the database
        holder.itemView.setTag(id);
    }

    public void swapCursor(Cursor newCursor) {

        // Always close the previous mCursor first
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public interface ListenerInterface extends View.OnClickListener {
        void onListItemLongClick(View view);
    }

    public class FavCurrentCViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        TextView abbrTextView;
        TextView amountTextView;
        ImageView flagImageView;

        // Constructor
        public FavCurrentCViewHolder(View itemView) {

            super(itemView);

            abbrTextView = itemView.findViewById(R.id.abbrTextView);
            amountTextView = itemView.findViewById(R.id.nameTextView);
            flagImageView = itemView.findViewById(R.id.flagImageView);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {

            mOnClickListener.onListItemLongClick(view);
            return true;
        }
    }
}
