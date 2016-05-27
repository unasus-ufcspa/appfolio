package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Activities.MainActivity;
import com.ufcspa.unasus.appportfolio.Adapter.StudentActivitiesAdapter;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.Model.StudFrPortClass;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 12/01/2016.
 */
public class FragmentStudentActivities extends Frag {
    private ListView list_activities;
    private ArrayList<StudFrPortClass> list;
    private DataBaseAdapter source;
    private Singleton singleton;
    private TextView className;
    private TextView portfolioName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_portfolio_activity, null);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    public void init() {
        singleton = Singleton.getInstance();
        source = DataBaseAdapter.getInstance(getActivity());

        try {
            list = source.selectListActivitiesAndStudents(singleton.portfolioClass.getIdPortClass(), singleton.portfolioClass.getPerfil(), singleton.user.getIdUser());
            Log.e("BANCO", "atividades (SelectActivitiesAactivity):"+ list.toString());
        } catch (Exception e) {
            Log.e("BANCO", "falha em pegar atividades (SelectActivitiesAactivity):" + e.getMessage());
        }

        className = (TextView) getView().findViewById(R.id.class_name);
        portfolioName = (TextView) getView().findViewById(R.id.portfolio_name);

        className.setText(singleton.portfolioClass.getClassCode());
        portfolioName.setText(singleton.portfolioClass.getPortfolioTitle());

        StudentActivitiesAdapter gridAdapter = new StudentActivitiesAdapter((MainActivity) getActivity(), list);

        list_activities = (ListView) getView().findViewById(R.id.list_activities);
        list_activities.setAdapter(gridAdapter);
        list_activities.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Log.d("tela atividades","clicou na caixa");
                getView().findViewById(R.id.activities_list).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
    }
}
