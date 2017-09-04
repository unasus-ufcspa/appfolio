package com.ufcspa.unasus.appportfolio.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.adapter.SelectReportStudentAdapter;
import com.ufcspa.unasus.appportfolio.database.DataBase;
import com.ufcspa.unasus.appportfolio.model.Activity;
import com.ufcspa.unasus.appportfolio.model.Singleton;
import com.ufcspa.unasus.appportfolio.model.StudFrPortClass;
import com.ufcspa.unasus.appportfolio.model.User;
import com.ufcspa.unasus.appportfolio.R;

import java.util.ArrayList;
import java.util.List;

public class ReportStudentsFragment extends HelperFragment {
    private GridView mGridReports;
    private TextView mPortfolioName;
    private TextView mClassName;
    private DataBase mDataBase;
    private static Singleton sSingleton;
    private List<User> mStudentList;
    private ArrayList<StudFrPortClass> mStudFrPortClassList;
    private ArrayList<User> mFinalList;

    public ReportStudentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_students, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        sSingleton = Singleton.getInstance();
        mDataBase = DataBase.getInstance(getActivity());
        mPortfolioName = (TextView) getView().findViewById(R.id.portfolio_name);
        mPortfolioName.setText(sSingleton.portfolioClass.getPortfolioTitle());
        mClassName = (TextView) getView().findViewById(R.id.class_name);
        mClassName.setText(sSingleton.portfolioClass.getClassCode());
        mStudFrPortClassList = new ArrayList<>();
        mStudentList = new ArrayList<>();
        mFinalList = new ArrayList<>();

        int i = 0;

        try {
            mStudentList = mDataBase.getUsersByIdPortfolioClass(sSingleton.portfolioClass.getIdPortClass());
            for (User student: mStudentList) {
                mStudFrPortClassList = mDataBase.selectListActivitiesAndStudentsByStudent(sSingleton.portfolioClass.getIdPortClass(), sSingleton.portfolioClass.getPerfil(), student.getIdUser());
                for (StudFrPortClass studFrPortClass : mStudFrPortClassList) {
                    List<Activity> temp = studFrPortClass.getListActivities();
                    for (Activity activity : temp) {
                        if (!mDataBase.getActivityStudentById(activity.getIdActivityStudent()).getDt_conclusion().equals("null") && mDataBase.getActivityStudentById(activity.getIdActivityStudent()).getDt_conclusion() != null)
                            i++;
                    }
                    if (i == temp.size()) {
                        mFinalList.add(student);
                        i = 0;
                    }
                }
            }
            Log.d("lista", "tam portlis:" + mStudentList.size());
        } catch (Exception e) {
            Log.wtf("ERRO", e.getMessage());
        }

//        Collections.sort(mStudentList);

        SelectReportStudentAdapter gridAdapter = new SelectReportStudentAdapter(getActivity(), mFinalList);
        mGridReports = (GridView) getView().findViewById(R.id.grid_students);
        mGridReports.setAdapter(gridAdapter);
    }
}
