package com.ufcspa.unasus.appportfolio.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ufcspa.unasus.appportfolio.Model.Activity;
import com.ufcspa.unasus.appportfolio.Model.SelectActivitiesGridViewAdapter;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by UNASUS on 10/11/2015.
 */
public class SelectActivitiesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    private GridView grid_activities;
    private ArrayList<Activity> activities;
    private DataBaseAdapter source;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);

        init();
    }

    private void init()
    {
        source = new DataBaseAdapter(getApplicationContext());
        activities = source.getActivities(17, 5);
        Collections.sort(activities);

        SelectActivitiesGridViewAdapter gridAdapter = new SelectActivitiesGridViewAdapter(this, activities);

        grid_activities = (GridView) findViewById(R.id.grid_activities);
        grid_activities.setAdapter(gridAdapter);

        grid_activities.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        System.out.println("Teste " + position);
    }
}
