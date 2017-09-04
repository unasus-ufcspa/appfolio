package com.ufcspa.unasus.appportfolio.activities.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.activities.MainActivity;
import com.ufcspa.unasus.appportfolio.adapter.ReportPortfolioAdapter;
import com.ufcspa.unasus.appportfolio.model.Activity;
import com.ufcspa.unasus.appportfolio.model.ActivityStudent;
import com.ufcspa.unasus.appportfolio.model.Singleton;
import com.ufcspa.unasus.appportfolio.model.StudFrPortClass;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportPortfolioFragment extends HelperFragment {
    private ListView mListReportActivities;
    private ArrayList<StudFrPortClass> mStudFrPortClassList;
    private List<Activity> mListActivities;
    private ArrayList<ActivityStudent> mListActivitiesStudent;
    private DataBase mDataBase;
    private static Singleton sSingleton;
//    private TextView className;
    private TextView mPortfolioName;


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
        sSingleton = Singleton.getInstance();
        mDataBase = DataBase.getInstance(getActivity());
        mListActivitiesStudent = new ArrayList<ActivityStudent>();
        mStudFrPortClassList = mDataBase.selectListActivitiesAndStudentsByStudent(sSingleton.portfolioClass.getIdPortClass(), sSingleton.portfolioClass.getPerfil(), sSingleton.idStudent);
        for (int i = 0; i < mStudFrPortClassList.size(); i++) {
            mListActivities = mStudFrPortClassList.get(i).getListActivities();
            for (int j = 0; j < mListActivities.size(); j++) {
                mListActivitiesStudent.add(j, mDataBase.listLastVersionActivityStudent(mListActivities.get(j).getIdActivityStudent()));
            }
        }

//        className = (TextView) getView().findViewById(R.id.class_name);
        mPortfolioName = (TextView) getView().findViewById(R.id.portfolio_name);
        mListReportActivities = (ListView) getView().findViewById(R.id.list_report_activities);

//        className.setText(sSingleton.portfolioClass.getClassCode());
        mPortfolioName.setText(sSingleton.portfolioClass.getPortfolioTitle());

        ReportPortfolioAdapter listAdapter = new ReportPortfolioAdapter((MainActivity) getActivity(), mListActivitiesStudent);

        mListReportActivities.setAdapter(listAdapter);
    }

}
