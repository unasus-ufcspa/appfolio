package com.ufcspa.unasus.appportfolio.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Adapter.StudentActivitiesAdapter;
import com.ufcspa.unasus.appportfolio.Model.Activity;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.Model.StudFrPortClass;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by UNASUS on 10/11/2015.
 */
public class SelectActivitiesActivity extends AppActivity //implements AdapterView.OnItemClickListener
{
    private ListView list_activities;
    private ArrayList<StudFrPortClass> list;
    private DataBaseAdapter source;
    private Singleton singleton;

    private TextView className;
    private TextView portfolioName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_activity);
        getSupportActionBar().hide();

        init();
        createDrawer(savedInstanceState);
    }

    private void init()
    {
        singleton = Singleton.getInstance();

        source = new DataBaseAdapter(getApplicationContext());
        try {

            list = source.selectListActivitiesAndStudents(singleton.portfolioClass.getIdPortClass(),singleton.portfolioClass.getPerfil(),singleton.user.getIdUser());
            //source.close();
        } catch (Exception e) {
            Log.e("BANCO", "falha em pegar atividades (SelectActivitiesAactivity):" + e.getMessage());
        }

        className = (TextView)findViewById(R.id.class_name);
        portfolioName = (TextView)findViewById(R.id.portfolio_name);

        className.setText(singleton.portfolioClass.getClassCode());
        portfolioName.setText(singleton.portfolioClass.getPortfolioTitle());

        StudentActivitiesAdapter gridAdapter = new StudentActivitiesAdapter(this, list);
        list_activities = (ListView) findViewById(R.id.list_activities);
        list_activities.setAdapter(gridAdapter);
        list_activities.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                findViewById(R.id.activities_list).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
    }


//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//    {
//        singleton.activity = this.activities.get(position);
//        System.out.println("Teste " + position);
//        startActivity(new Intent(this, EditActivity.class));
//        //finish();
//    }
}
