package com.ufcspa.unasus.appportfolio.Activities.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Activities.MainActivity;
import com.ufcspa.unasus.appportfolio.Adapter.ReportPortfolioAdapter;
import com.ufcspa.unasus.appportfolio.Model.Activity;
import com.ufcspa.unasus.appportfolio.Model.ActivityStudent;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.Model.StudFrPortClass;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.Database.DataBase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportPortfolioFragment extends HelperFragment {
    private ListView list_report_activities;
    private ArrayList<StudFrPortClass> list;
    private List<Activity> listActivities;
    private ArrayList<ActivityStudent> listActivitiesStudent;
    private DataBase source;
    private Singleton singleton;
//    private TextView className;
    private TextView portfolioName;


    public ReportPortfolioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_portfolio, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    public void init() {
        singleton = Singleton.getInstance();
        source = DataBase.getInstance(getActivity());
        listActivitiesStudent = new ArrayList<ActivityStudent>();

//        try {
            list = source.selectListActivitiesAndStudentsByStudent(singleton.portfolioClass.getIdPortClass(), singleton.portfolioClass.getPerfil(), singleton.idStudent);
            for (int i=0;i<list.size();i++) {
                listActivities = list.get(i).getListActivities();
                for (int j=0;j<listActivities.size();j++) {
                    listActivitiesStudent.add(j,source.listLastVersionActivityStudent(listActivities.get(j).getIdActivityStudent()));
                }
            }

//        className = (TextView) getView().findViewById(R.id.class_name);
        portfolioName = (TextView) getView().findViewById(R.id.portfolio_name);
        list_report_activities = (ListView) getView().findViewById(R.id.list_report_activities);

//        className.setText(singleton.portfolioClass.getClassCode());
        portfolioName.setText(singleton.portfolioClass.getPortfolioTitle());

        ReportPortfolioAdapter listAdapter = new ReportPortfolioAdapter((MainActivity) getActivity(), listActivitiesStudent);

        list_report_activities.setAdapter(listAdapter);
    }

}
