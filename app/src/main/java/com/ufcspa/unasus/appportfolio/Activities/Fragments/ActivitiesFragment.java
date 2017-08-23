package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Activities.MainActivity;
import com.ufcspa.unasus.appportfolio.Adapter.ActivitiesAdapter;
import com.ufcspa.unasus.appportfolio.Adapter.StudentActivitiesAdapter;
import com.ufcspa.unasus.appportfolio.Database.DataBase;
import com.ufcspa.unasus.appportfolio.Model.Activity;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.Model.StudFrPortClass;
import com.ufcspa.unasus.appportfolio.R;

import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 12/01/2016.
 */
public class ActivitiesFragment extends HelperFragment implements ActivitiesAdapter.OnInfoButtonClick{
    private ListView list_activities;
    private ArrayList<StudFrPortClass> list;
    private DataBase source;
    private Singleton singleton;
    private TextView className;
    private TextView portfolioName;
    private SearchView edtSearch;
    private StudentActivitiesAdapter gridAdapter;
    private RelativeLayout slider;
    private RelativeLayout content;

    public ActivitiesFragment() {
    }

    public void openInfo(View v, Activity activity){
        SlidingPaneLayout layout = (SlidingPaneLayout) getView().findViewById(R.id.activity_portfolio_activity);
        TextView info_container_title = (TextView) getView().findViewById(R.id.info_container_title);
        WebView info_container_description = (WebView) getView().findViewById(R.id.info_container_description);

        slider.setVisibility(View.VISIBLE);

        info_container_title.setText(activity.getTitle());
        info_container_description.loadDataWithBaseURL("",activity.getDescription(),"text/html","UTF-8","about:blank");
        info_container_description.setBackgroundColor(Color.parseColor("#F3F4F2"));

        if (layout.isOpen()) {
            layout.closePane();
        } else {
            layout.openPane();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activities, null);

        initCommentsTab(view);

        content = (RelativeLayout) container.findViewById(R.id.content_layout);
        SlidingPaneLayout.LayoutParams layoutParams = new SlidingPaneLayout.LayoutParams(SlidingPaneLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

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

        final SlidingPaneLayout layout = (SlidingPaneLayout) view.findViewById(R.id.activity_portfolio_activity);

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

    public void init() {
        singleton = Singleton.getInstance();
        source = DataBase.getInstance(getActivity());

        try {
            list = source.selectListActivitiesAndStudents(singleton.portfolioClass.getIdPortClass(), singleton.portfolioClass.getPerfil(), singleton.user.getIdUser());
            Log.e("BANCO", "atividades (SelectActivitiesAactivity):"+ list.toString());
        } catch (Exception e) {
            Log.e("BANCO", "falha em pegar atividades (SelectActivitiesAactivity):" + e.getMessage());
        }

        edtSearch = (SearchView) getView().findViewById(R.id.edt_search);

        className = (TextView) getView().findViewById(R.id.class_name);
        portfolioName = (TextView) getView().findViewById(R.id.portfolio_name);

        className.setText(singleton.portfolioClass.getClassCode());
        portfolioName.setText(singleton.portfolioClass.getPortfolioTitle());

        gridAdapter = new StudentActivitiesAdapter((MainActivity) getActivity(), list, this);

        list_activities = (ListView) getView().findViewById(R.id.list_activities);
        list_activities.setAdapter(gridAdapter);
        list_activities.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Log.d("tela atividades","clicou na caixa");
                getView().findViewById(R.id.activities_list).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        if (singleton.portfolioClass.getPerfil().equals("S")){
            edtSearch.setVisibility(View.INVISIBLE);
        }

        edtSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ActivitiesFragment.this.gridAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}
