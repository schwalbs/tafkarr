package edu.rose_hulman.tafkarr;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ScheduleLookupResultsListAdapter extends
        RecyclerView.Adapter<ScheduleLookupResultsListAdapter.ViewHolder> {
    private ArrayList<String> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private View mView;

        public ViewHolder(View v) {
            super(v);
            mView = v;
        }

        public void setData(String data) {
            ((TextView) mView.findViewById(R.id.resultData)).setText(data);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ScheduleLookupResultsListAdapter(ArrayList<String> dataset) {
        mDataset = dataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ScheduleLookupResultsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.schedule_lookup_results_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.setData(mDataset.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setDataset(ArrayList<String> newDataset) {
        mDataset = newDataset;
        this.notifyDataSetChanged();
    }

    public ArrayList<String> getDataset() {
        return mDataset;
    }
}
