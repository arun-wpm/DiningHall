package edu.exeter.dininghall;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

//I just copied from the Android Developer guide lol
//TODO: make this work
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private String[] mDataset;
    private float[] mRating;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public RatingBar mRatingBar;
        public MyViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.menuName);
            mRatingBar = v.findViewById(R.id.ratingBar);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(String[] myDataset) {
        ArrayList<String> Temp = new ArrayList<String>();
        ArrayList<Float> TempRating = new ArrayList<Float>();
        for (int i = 0; i < myDataset.length; i++)
        {
            if (!myDataset[i].equals("")) {
                Temp.add(myDataset[i]);
                //TODO: retrieve rating
                if (myDataset[i].contains("Wetherell") || myDataset[i].contains("Elm Street"))
                    TempRating.add(-1.0f);
                else
                    TempRating.add(i%5 + 0.5f);
            }
        }
        mDataset = new String[Temp.size()];
        mRating = new float[Temp.size()];
        for (int i = 0; i < Temp.size(); i++) {
            mDataset[i] = Temp.get(i);
            mRating[i] = TempRating.get(i);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_element, parent, false);
//        ...
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Log.e("TAG",mDataset[position] + mRating[position]);
        holder.mTextView.setText(mDataset[position]);
        if (mRating[position] < 0.0f) {
            Log.e("TAG", (mRating[position] < 0.0f) + "");
            holder.mRatingBar.setVisibility(View.INVISIBLE);
            holder.mTextView.setTypeface(null, Typeface.BOLD);
        }
        else
            holder.mRatingBar.setRating(mRating[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}