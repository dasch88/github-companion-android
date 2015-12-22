package com.aptera.githubcompanion.app.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aptera.githubcompanion.lib.model.IDescribable;

import java.util.List;

/**
 * Created by daschliman on 12/22/2015.
 */
public class DescribableArrayAdapter<T extends IDescribable> extends ArrayAdapter<T> {

    private int mPrimaryTextViewResourceId;
    private int mSecondaryTextViewResourceId;

    public DescribableArrayAdapter(Context context, int resource, int primaryTextViewResourceId, int secondaryTextViewResourceId, List<T> objects) {
        super(context, resource, primaryTextViewResourceId, objects);
        mPrimaryTextViewResourceId = primaryTextViewResourceId;
        mSecondaryTextViewResourceId = secondaryTextViewResourceId;
    }

    public DescribableArrayAdapter(Context context, int resource, int primaryTextViewResourceId, int secondaryTextViewResourceId, T[] objects) {
        super(context, resource, primaryTextViewResourceId, objects);
        mPrimaryTextViewResourceId = primaryTextViewResourceId;
        mSecondaryTextViewResourceId = secondaryTextViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView text1 = (TextView) view.findViewById(mPrimaryTextViewResourceId);
        TextView text2 = (TextView) view.findViewById(mSecondaryTextViewResourceId);
        text1.setText(this.getItem(position).toString());
        text2.setText(this.getItem(position).getDescription());
        if(text2.getText() == null || text2.getText() == "") {
            text2.setVisibility(View.INVISIBLE);
        }
        return view;
    }

}
