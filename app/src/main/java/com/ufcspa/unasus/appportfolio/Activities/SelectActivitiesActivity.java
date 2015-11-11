package com.ufcspa.unasus.appportfolio.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ufcspa.unasus.appportfolio.Model.Activity;
import com.ufcspa.unasus.appportfolio.Model.SelectActivitiesGridViewAdapter;
import com.ufcspa.unasus.appportfolio.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by UNASUS on 10/11/2015.
 */
public class SelectActivitiesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    private GridView grid_activities;
    private List<Activity> activities;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);

        fakeData();
        init();
    }

    private void fakeData()
    {

        Activity a = new Activity("Introdução", "Escreva uma introdução bacana.");
        Activity a2 = new Activity("Desenvolvimento", "Escreva um desenvolvimento bacana.");
        Activity a3 = new Activity("Conclusão", "Escreva uma conclusão bacana.");

        activities = new ArrayList();
        activities.add(a);
        activities.add(a2);
        activities.add(a3);
    }


    private void init()
    {
        SelectActivitiesGridViewAdapter gridAdapter = new SelectActivitiesGridViewAdapter(this, activities);

        grid_activities = (GridView) findViewById(R.id.grid_activities);
        grid_activities.setAdapter(gridAdapter);

        grid_activities.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        System.out.println("Teste" + position);
    }
}
