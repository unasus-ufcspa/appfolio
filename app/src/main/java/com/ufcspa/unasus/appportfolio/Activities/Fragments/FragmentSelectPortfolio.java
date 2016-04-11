package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

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

    void FragmentSelectPortfolio()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_classes, null);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    public void init(){
        singleton = Singleton.getInstance();
        source = DataBaseAdapter.getInstance(getActivity());

        try
        {
            portclasses = source.selectListClassAndUserType(singleton.user.getIdUser());
            Log.d("lista", "tam portlis:" + portclasses.size());
//            Log.d("lista", "tam tb_class:" + source.getCountTbClass());
//            Log.d("lista", "tam tb_portfolio_student:" + source.getCountTbPortfolioStudent());
//            Log.d("lista", "tam tb_activity_student:" + source.getCountTbActivityStudent());

        }
        catch (Exception e)
        {
            Log.wtf("ERRO",e.getMessage());
        }

        SelectPortfolioClassAdapter gridAdapter = new SelectPortfolioClassAdapter(getActivity(),portclasses);
        grid_classes = (GridView) getView().findViewById(R.id.grid_classes);
        grid_classes.setAdapter(gridAdapter);
    }
}
