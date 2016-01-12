package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.ufcspa.unasus.appportfolio.Activities.SelectActivitiesActivity;

import com.ufcspa.unasus.appportfolio.Adapter.SelectPortfolioClassAdapter;
import com.ufcspa.unasus.appportfolio.Model.PortfolioClass;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.util.List;

/**
 * Created by Desenvolvimento on 12/01/2016.
 */
public class FragmentSelectPortfolio extends Frag {
    private GridView grid_classes;
    private DataBaseAdapter source;
    private Singleton singleton;
    private List<PortfolioClass> portclasses;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        //getSupportActionBar().hide();
        View view = inflater.inflate(R.layout.activity_classes, null);
        getActivity().getActionBar().hide();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }



    public void init(){
        singleton = Singleton.getInstance();

        source = new DataBaseAdapter(getActivity().getApplicationContext());
        try {
            //classes = source.getClasses(singleton.user.getIdUser(), singleton.user.getUserType());
            portclasses=source.selectListClassAndUserType(singleton.user.getIdUser());
            Log.d("lista", "tam portlis:" + portclasses.size());

            //source.close();
        }catch (Exception e){

            Log.wtf("ERRO",e.getMessage());
        }
        SelectPortfolioClassAdapter gridAdapter= new SelectPortfolioClassAdapter(getActivity(),portclasses);
        grid_classes = (GridView) getView().findViewById(R.id.grid_classes);
        grid_classes.setAdapter(gridAdapter);
        grid_classes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                singleton.portfolioClass = portclasses.get(position);
                Log.d("BANCO", "ID do portfolioClass " + singleton.portfolioClass.getIdPortClass());
                Toast.makeText(getActivity().getApplicationContext(), "clicou em:" + portclasses.get(position).getClassCode(), Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(this,SelectActivitiesActivity.class));
                //finish();
//                FragmentStudentActivities mFragment = new FragmentStudentActivities();
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.content_frame, mFragment ).commit();
            }
        });
    }
}
