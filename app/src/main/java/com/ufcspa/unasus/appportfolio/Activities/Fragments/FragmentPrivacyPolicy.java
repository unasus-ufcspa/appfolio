package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

/**
 * Created by icaromsc on 16/05/2016.
 */
public class FragmentPrivacyPolicy extends Frag {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.termo_uso, null);

//        singleton.idActivityStudent = source.getActivityStudentID(singleton.activity.getIdAtivity(), singleton.portfolioClass.getIdPortfolioStudent());
        Log.d("Comments", "On createView entrou");

        return view;
    }
}
