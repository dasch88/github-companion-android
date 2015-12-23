package com.aptera.githubcompanion.app.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aptera.githubcompanion.lib.model.IDescribable;

import java.util.List;

/**
 * Created by daschliman on 12/22/2015.
 */
public abstract class DescribableRecyclerAdapter<T extends IDescribable> extends RecyclerView.Adapter<DescribableRecyclerAdapter.ViewHolder> implements View.OnClickListener {

    private List<T> mDataset;
    private int mLayoutResourceId;
    private int mPrimaryTextViewResourceId;
    private int mSecondaryTextViewResourceId;

    public void setDataset(List<T> dataset) {
        mDataset = dataset;
        this.notifyDataSetChanged();
    }

    public DescribableRecyclerAdapter(int layoutResourceId, int primaryTextViewResourceId, int secondaryTextViewResourceId) {
        mLayoutResourceId = layoutResourceId;
        mPrimaryTextViewResourceId = primaryTextViewResourceId;
        mSecondaryTextViewResourceId = secondaryTextViewResourceId;
    }

    @Override
    public DescribableRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //create a new view and viewholder
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(mLayoutResourceId, null);
        itemLayoutView.setOnClickListener(this);
        DescribableRecyclerAdapter.ViewHolder viewHolder = new DescribableRecyclerAdapter.ViewHolder(itemLayoutView, mPrimaryTextViewResourceId, mSecondaryTextViewResourceId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DescribableRecyclerAdapter.ViewHolder holder, int position) {
        if(mDataset != null) {
            T item = mDataset.get(position);
            holder.primaryTextView.setText(item.toString());
            holder.secondaryTextView.setText(item.getDescription());
            holder.secondaryTextView.setVisibility(item.getDescription() == null || item.getDescription() == "" ? View.INVISIBLE : View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if(mDataset != null)
            return mDataset.size();
        else
            return 0;
    }

    public T getItem(int position) {
        if(mDataset != null)
            return mDataset.get(position);
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView primaryTextView;
        public TextView secondaryTextView;

        public ViewHolder(View itemLayoutView, int primaryTextViewResourceId, int secondaryTextViewResourceId) {
            super(itemLayoutView);
            primaryTextView = (TextView) itemLayoutView.findViewById(primaryTextViewResourceId);
            secondaryTextView = (TextView) itemLayoutView.findViewById(secondaryTextViewResourceId);
        }
    }

}
