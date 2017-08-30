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
    private GridView grid_reports;
    private TextView grid_empty;
    private DataBase source;
    private Singleton singleton;
    private List<PortfolioClass> portclasses;
    private ArrayList<User> students;
    private ArrayList<PortfolioClass> finalList;
    private ArrayList<StudFrPortClass> list;
    private ArrayList<StudFrPortClass> listActivities;

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
        singleton = Singleton.getInstance();
        source = DataBase.getInstance(getActivity());
        finalList = new ArrayList<>();
        students = new ArrayList<>();
        grid_empty = (TextView) getView().findViewById(R.id.grid_empty);
        int i=0;

        try {
            portclasses = source.selectListClassAndUserType(singleton.user.getIdUser());
            for (PortfolioClass portclass: portclasses){
                students = (ArrayList) source.getUsersByIdPortfolioClass(portclass.getIdPortClass());
                for (User student:students) {
                    list = source.selectListActivitiesAndStudentsByStudent(portclass.getIdPortClass(), portclass.getPerfil(), student.getIdUser());
                    for (StudFrPortClass studFrPortClass:list){
                     List<Activity> temp = studFrPortClass.getListActivities();
                        for (Activity activity:temp){
                            if (!source.getActivityStudentById(activity.getIdActivityStudent()).getDt_conclusion().equals("null") && source.getActivityStudentById(activity.getIdActivityStudent()).getDt_conclusion()!=null)
                                i++;
                        }
                        if (i == temp.size())
                            finalList.add(portclass);
                        i=0;
                    }
                    if (portclass.getPerfil().equals("T") && finalList.size()==1)
                        break;
                }
            }
            Log.d("lista", "tam portlis:" + portclasses.size());
        } catch (Exception e) {
            Log.wtf("ERRO", e.getMessage());
        }

        SelectReportClassAdapter gridAdapter = null;
        if (!finalList.isEmpty()) {
            grid_empty.setVisibility(View.INVISIBLE);
            Collections.sort(finalList);
            gridAdapter = new SelectReportClassAdapter(getActivity(), finalList);
        }
        grid_reports = (GridView) getView().findViewById(R.id.grid_reports);
        grid_reports.setAdapter(gridAdapter);
    }
}
