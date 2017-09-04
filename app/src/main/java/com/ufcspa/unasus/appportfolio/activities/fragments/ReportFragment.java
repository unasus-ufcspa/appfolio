package com.ufcspa.unasus.appportfolio.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.adapter.SelectReportClassAdapter;
import com.ufcspa.unasus.appportfolio.model.Activity;
import com.ufcspa.unasus.appportfolio.model.PortfolioClass;
import com.ufcspa.unasus.appportfolio.model.Singleton;
import com.ufcspa.unasus.appportfolio.model.StudFrPortClass;
import com.ufcspa.unasus.appportfolio.model.User;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReportFragment extends HelperFragment {
    private GridView mGridView;
    private TextView mTvGridEmpty;
    private DataBase mDataBase;
    private static Singleton sSingleton;
    private List<PortfolioClass> mPortClassList;
    private ArrayList<User> mUserList;
    private ArrayList<PortfolioClass> mFinalList;
    private ArrayList<StudFrPortClass> mList;
    private ArrayList<StudFrPortClass> mListActivities;

    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        sSingleton = Singleton.getInstance();
        mDataBase = DataBase.getInstance(getActivity());
        mFinalList = new ArrayList<>();
        mUserList = new ArrayList<>();
        mTvGridEmpty = (TextView) getView().findViewById(R.id.grid_empty);
        int i = 0;

        try {
            mPortClassList = mDataBase.selectListClassAndUserType(sSingleton.user.getIdUser());
            for (PortfolioClass portclass : mPortClassList) {
                mUserList = (ArrayList) mDataBase.getUsersByIdPortfolioClass(portclass.getIdPortClass());
                for (User student: mUserList) {
                    mList = mDataBase.selectListActivitiesAndStudentsByStudent(portclass.getIdPortClass(), portclass.getPerfil(), student.getIdUser());
                    for (StudFrPortClass studFrPortClass : mList) {
                        List<Activity> temp = studFrPortClass.getListActivities();
                        for (Activity activity : temp) {
                            if (!mDataBase.getActivityStudentById(activity.getIdActivityStudent()).getDt_conclusion().equals("null") && mDataBase.getActivityStudentById(activity.getIdActivityStudent()).getDt_conclusion() != null)
                                i++;
                        }
                        if (i == temp.size())
                            mFinalList.add(portclass);
                        i = 0;
                    }
                    if (portclass.getPerfil().equals("T") && mFinalList.size() == 1)
                        break;
                }
            }
            Log.d("lista", "tam portlis:" + mPortClassList.size());
        } catch (Exception e) {
            Log.wtf("ERRO", e.getMessage());
        }

        SelectReportClassAdapter gridAdapter = null;
        if (!mFinalList.isEmpty()) {
            mTvGridEmpty.setVisibility(View.INVISIBLE);
            Collections.sort(mFinalList);
            gridAdapter = new SelectReportClassAdapter(getActivity(), mFinalList);
        }
        mGridView = (GridView) getView().findViewById(R.id.grid_reports);
        mGridView.setAdapter(gridAdapter);
    }
}
