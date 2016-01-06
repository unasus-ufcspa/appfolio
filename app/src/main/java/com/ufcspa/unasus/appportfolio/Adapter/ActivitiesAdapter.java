package com.ufcspa.unasus.appportfolio.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ufcspa.unasus.appportfolio.Activities.SelectActivitiesActivity;
import com.ufcspa.unasus.appportfolio.Model.Activity;
import com.ufcspa.unasus.appportfolio.R;

import java.util.List;

/**
 * Created by arthurzettler on 1/5/16.
 */
public class ActivitiesAdapter extends BaseAdapter {

    private SelectActivitiesActivity context;
    private List<Activity> activities;

    private static LayoutInflater inflater = null;

    public ActivitiesAdapter(SelectActivitiesActivity context, List<Activity> activities)
    {
        this.context = context;
        this.activities = activities;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Activity getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        rowView = inflater.inflate(R.layout.adapter_item, null);

        return rowView;
    }
}
