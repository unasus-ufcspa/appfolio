package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Adapter.SelectReportStudentAdapter;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.Model.User;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.util.List;

public class FragmentReportStudents extends Frag {
    private GridView grid_reports;
    private TextView portfolio_name;
    private TextView class_name;
    private DataBaseAdapter source;
    private Singleton singleton;
    private List<User> students;

    public FragmentReportStudents() {
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

        try {
            students = source.getUsersByIdPortfolioClass(singleton.portfolioClass.getIdPortClass());
            Log.d("lista", "tam portlis:" + students.size());
        } catch (Exception e) {
            Log.wtf("ERRO", e.getMessage());
        }

//        Collections.sort(students);

        SelectReportStudentAdapter gridAdapter = new SelectReportStudentAdapter(getActivity(), students);
        grid_reports = (GridView) getView().findViewById(R.id.grid_students);
        grid_reports.setAdapter(gridAdapter);
    }
}
