package com.aptera.githubcompanion.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aptera.githubcompanion.R;
import com.aptera.githubcompanion.app.viewmodels.FragmentNavigationMapping;

import java.io.Serializable;
import java.util.List;

/**
 * Created by daschliman on 12/29/2015.
 */
public class FragmentNavigationAdapter extends ArrayAdapter<FragmentNavigationMapping> {


    public FragmentNavigationAdapter(Context context, int resource) {
        super(context, resource);
    }

    public FragmentNavigationAdapter(Context context, int resource, List<FragmentNavigationMapping> objects) {
        super(context, resource, objects);
    }

    public FragmentNavigationAdapter(Context context, int resource, FragmentNavigationMapping[] objects) {
        super(context, resource, objects);
    }

    public FragmentNavigationAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public FragmentNavigationAdapter(Context context, int resource, int textViewResourceId, List<FragmentNavigationMapping> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public FragmentNavigationAdapter(Context context, int resource, int textViewResourceId, FragmentNavigationMapping[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.listitem_selectable_navigation, null);
        }

        TextView txtDisplayName = (TextView) convertView.findViewById(R.id.txtDisplayName);

        FragmentNavigationMapping mapping = getItem(position);
        txtDisplayName.setText(getContext().getString(mapping.displayTextId));
        convertView.setBackgroundColor(mapping.isSelected ? ContextCompat.getColor(getContext(), R.color.colorPrimary) : Color.TRANSPARENT);
        txtDisplayName.setTextColor(mapping.isSelected ? ContextCompat.getColor(getContext(), R.color.colorWhite) : ContextCompat.getColor(getContext(), android.R.color.primary_text_light));

        return convertView;
    }


}
