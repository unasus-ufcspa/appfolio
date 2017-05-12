package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Activities.MainActivity;
import com.ufcspa.unasus.appportfolio.Adapter.SelectPortfolioClassAdapter;
import com.ufcspa.unasus.appportfolio.Model.PortfolioClass;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.WebClient.BasicData;
import com.ufcspa.unasus.appportfolio.WebClient.BasicDataClient;
import com.ufcspa.unasus.appportfolio.WebClient.FullData;
import com.ufcspa.unasus.appportfolio.WebClient.FullDataClient;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.util.Collections;
import java.util.List;

/**
 * Created by Desenvolvimento on 12/01/2016.
 */
public class FragmentSelectPortfolio extends Frag implements SelectPortfolioClassAdapter.OnInfoButtonClick{
    private GridView grid_classes;
    private DataBaseAdapter source;
    private Singleton singleton;
    private List<PortfolioClass> portclasses;
    private Button btSync;
    private RelativeLayout slider;

    void FragmentSelectPortfolio() {
    }

    public void openInfo(View v, PortfolioClass portfolioClass){
        SlidingPaneLayout layout = (SlidingPaneLayout) getView().findViewById(R.id.activity_classes);
        TextView info_container_title = (TextView) getView().findViewById(R.id.info_container_title);
        TextView info_container_description = (TextView) getView().findViewById(R.id.info_container_description);

        slider.setVisibility(View.VISIBLE);

        info_container_title.setText(portfolioClass.getPortfolioTitle());
        info_container_description.setText(source.getPortfolioDescription(portfolioClass.getIdPortClass()));

        if (layout.isOpen()) {
            layout.closePane();
        } else {
            layout.openPane();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_classes, null);
        btSync = (Button) view.findViewById(R.id.bt_sync);
        final RotateAnimation rotate = new RotateAnimation(0, -360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotate.setDuration(500);

        singleton = Singleton.getInstance();

        if (singleton.guestUser) {
            btSync.setVisibility(View.VISIBLE);
            view.findViewById(R.id.tx_sync).setVisibility(View.VISIBLE);
            btSync.setOnClickListener(new View.OnClickListener() {// TODO: 02/05/2017 finish sync button
                @Override
                public void onClick(View v) {
                    btSync.startAnimation(rotate);

                    BasicDataClient basicDataClient = new BasicDataClient(getContext());
                    basicDataClient.postJson(BasicData.toJSON(Singleton.getInstance().user.getIdUser(), Singleton.getInstance().device.get_id_device()));
                    FullDataClient fullDataClient = new FullDataClient(getContext());
                    fullDataClient.postJson(FullData.toJSON(singleton.device.get_id_device(), 0));
                }
            });
        }
        initCommentsTab(view);

        return view;
    }

    private void initCommentsTab(final View view) {

        slider = (RelativeLayout) view.findViewById(R.id.slider);
        slider.setVisibility(View.INVISIBLE);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? dm.widthPixels / 4 : dm.widthPixels / 3;

        SlidingPaneLayout.LayoutParams relativeParams = new SlidingPaneLayout.LayoutParams(new SlidingPaneLayout.LayoutParams(SlidingPaneLayout.LayoutParams.MATCH_PARENT, SlidingPaneLayout.LayoutParams.MATCH_PARENT));
        relativeParams.setMargins(width*2, 0, 0, 0);
        slider.setLayoutParams(relativeParams);

        slider.requestLayout();
        slider.bringToFront();

        final SlidingPaneLayout layout = (SlidingPaneLayout) view.findViewById(R.id.activity_classes);

        layout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }

            @Override
            public void onPanelOpened(View panel) {
                if (panel != null) {
                    Fragment frag = getActivity().getSupportFragmentManager().findFragmentById(R.id.info_container);
                    if (frag != null) {
                        getActivity().getSupportFragmentManager().beginTransaction().remove(frag).commit();
                    }
                    slider.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPanelClosed(View panel) {
                if (panel != null) {
                }
            }
        });

        layout.setSliderFadeColor(getResources().getColor(android.R.color.transparent));
        layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        layout.openPane();
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        source = DataBaseAdapter.getInstance(getActivity());

        try {
            portclasses = source.selectListClassAndUserType(singleton.user.getIdUser());
            Log.d("lista", "tam portlis:" + portclasses.size());
        } catch (Exception e) {
            Log.wtf("ERRO", e.getMessage());
        }

        Collections.sort(portclasses);

        SelectPortfolioClassAdapter gridAdapter = new SelectPortfolioClassAdapter(getActivity(), portclasses);
        gridAdapter.setOnInfoButtonClickListener(this);
        grid_classes = (GridView) getView().findViewById(R.id.grid_classes);
        grid_classes.setAdapter(gridAdapter);
    }
}
