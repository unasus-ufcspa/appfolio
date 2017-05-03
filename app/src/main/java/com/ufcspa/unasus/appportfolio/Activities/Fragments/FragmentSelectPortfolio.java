package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.GridView;

import com.ufcspa.unasus.appportfolio.Activities.MainActivity;
import com.ufcspa.unasus.appportfolio.Adapter.SelectPortfolioClassAdapter;
import com.ufcspa.unasus.appportfolio.Model.PortfolioClass;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.util.Collections;
import java.util.List;

/**
 * Created by Desenvolvimento on 12/01/2016.
 */
public class FragmentSelectPortfolio extends Frag {
    private GridView grid_classes;
    private DataBaseAdapter source;
    private Singleton singleton;
    private List<PortfolioClass> portclasses;
    private Button btSync;

    void FragmentSelectPortfolio() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_classes, null);
        btSync = (Button) view.findViewById(R.id.bt_sync);
        final RotateAnimation rotate = new RotateAnimation(0, -360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotate.setDuration(500);
        btSync.setOnClickListener(new View.OnClickListener() {// TODO: 02/05/2017 finish sync button
            @Override
            public void onClick(View v) {
                btSync.startAnimation(rotate);

//                MainActivity mainActivity = new MainActivity();
//                mainActivity.getBasicData();
//                mainActivity.getFullData();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        singleton = Singleton.getInstance();
        source = DataBaseAdapter.getInstance(getActivity());

        try {
            portclasses = source.selectListClassAndUserType(singleton.user.getIdUser());
            Log.d("lista", "tam portlis:" + portclasses.size());
        } catch (Exception e) {
            Log.wtf("ERRO", e.getMessage());
        }

        Collections.sort(portclasses);

        SelectPortfolioClassAdapter gridAdapter = new SelectPortfolioClassAdapter(getActivity(), portclasses);
        grid_classes = (GridView) getView().findViewById(R.id.grid_classes);
        grid_classes.setAdapter(gridAdapter);
    }
}
