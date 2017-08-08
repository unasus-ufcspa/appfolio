package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Adapter.SelectReportStudentAdapter;
import com.ufcspa.unasus.appportfolio.Model.Activity;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.Model.StudFrPortClass;
import com.ufcspa.unasus.appportfolio.Model.User;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReportStudentsFragment extends HelperFragment {
    private GridView grid_reports;
    private TextView portfolio_name;
    private TextView class_name;
    private DataBaseAdapter source;
    private Singleton singleton;
    private List<User> students;
    private ArrayList<StudFrPortClass> list;
    private ArrayList<User> finalList;

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
        singleton = Singleton.getInstance();
        source = DataBaseAdapter.getInstance(getActivity());
        portfolio_name = (TextView)getView().findViewById(R.id.portfolio_name);
        portfolio_name.setText(singleton.portfolioClass.getPortfolioTitle());
        class_name = (TextView)getView().findViewById(R.id.class_name);
        class_name.setText(singleton.portfolioClass.getClassCode());
        list = new ArrayList<>();
        students = new ArrayList<>();
        finalList = new ArrayList<>();

        int i=0;

        try {
            students = source.getUsersByIdPortfolioClass(singleton.portfolioClass.getIdPortClass());
            for (User student:students) {
                list = source.selectListActivitiesAndStudentsByStudent(singleton.portfolioClass.getIdPortClass(), singleton.portfolioClass.getPerfil(), student.getIdUser());
                for (StudFrPortClass studFrPortClass:list){
                    List<Activity> temp = studFrPortClass.getListActivities();
                    for (Activity activity:temp){
                        if (!source.getActivityStudentById(activity.getIdActivityStudent()).getDt_conclusion().equals("null") && source.getActivityStudentById(activity.getIdActivityStudent()).getDt_conclusion()!=null)
                            i++;
                    }
                    if (i == temp.size()) {
                        finalList.add(student);
                        i=0;
                    }
                }
            }
            Log.d("lista", "tam portlis:" + students.size());
        } catch (Exception e) {
            Log.wtf("ERRO", e.getMessage());
        }

//        Collections.sort(students);

        SelectReportStudentAdapter gridAdapter = new SelectReportStudentAdapter(getActivity(), finalList);
        grid_reports = (GridView) getView().findViewById(R.id.grid_students);
        grid_reports.setAdapter(gridAdapter);
    }
}
