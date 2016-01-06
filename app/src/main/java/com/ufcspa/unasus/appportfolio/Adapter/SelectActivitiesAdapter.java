package com.ufcspa.unasus.appportfolio.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Activities.SelectActivitiesActivity;
import com.ufcspa.unasus.appportfolio.Model.Activity;
import com.ufcspa.unasus.appportfolio.Model.DividerItemDecoration;
import com.ufcspa.unasus.appportfolio.R;

import java.util.List;

/**
 * Created by UNASUS on 10/11/2015.
 */
public class SelectActivitiesAdapter extends BaseAdapter
{
    private static LayoutInflater inflater = null;
    private SelectActivitiesActivity context;
    private List<Activity> activities;

    public SelectActivitiesAdapter(SelectActivitiesActivity context, List<Activity> activities)
    {
        this.context = context;
        this.activities = activities;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return activities.size();
    }

    @Override
    public Activity getItem(int position) {
        return activities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.adapter_portfolio_activity, null);

        RecyclerView recyclerView = (RecyclerView) rowView.findViewById(R.id.activities_list);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of  child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL_LIST, 100));

        recyclerView.setAdapter(new ActivitiesAdapter());

//        LinearLayout linearLayout = (LinearLayout) rowView.findViewById(R.id.activities_layout);
//        View v = context.getLayoutInflater().inflate(R.layout., null);
//        linearLayout.addView(v);

//        holder.txt_activity_title = (TextView) rowView.findViewById(R.id.txt_activity_title);
//        holder.txt_activity_description = (TextView) rowView.findViewById(R.id.txt_activity_description);
//
//        Activity activity_aux = activities.get(position);
//
//        holder.txt_activity_title.setText(activity_aux.getTitle());
//        holder.txt_activity_description.setText(activity_aux.getDescription());

        return rowView;
    }

    public class Holder
    {
        TextView txt_activity_title;
        TextView txt_activity_description;
    }
}
