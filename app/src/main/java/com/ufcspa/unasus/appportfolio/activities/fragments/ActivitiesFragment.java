package com.ufcspa.unasus.appportfolio.activities.fragments;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.activities.MainActivity;
import com.ufcspa.unasus.appportfolio.adapter.ActivitiesAdapter;
import com.ufcspa.unasus.appportfolio.adapter.StudentActivitiesAdapter;
import com.ufcspa.unasus.appportfolio.database.DataBase;
import com.ufcspa.unasus.appportfolio.model.Activity;
import com.ufcspa.unasus.appportfolio.model.Singleton;
import com.ufcspa.unasus.appportfolio.model.StudFrPortClass;
import com.ufcspa.unasus.appportfolio.R;

import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 12/01/2016.
 */
public class ActivitiesFragment extends HelperFragment implements ActivitiesAdapter.OnInfoButtonClick {
    private ListView mListActivities;
    private ArrayList<StudFrPortClass> mList;
    private DataBase mDataBase;
    private static Singleton sSingleton;
    private TextView mClassName;
    private TextView mPortfolioName;
    private SearchView mSearchView;
    private StudentActivitiesAdapter mGridAdapter;
    private RelativeLayout mDrawer;
    private DrawerLayout mDrawerLayout;
    private TextView mDrawerTitle;
    private TextView mNotificationView;
    private WebView mDrawerDescription;
    private RelativeLayout mRightBarGreen;
    private int mNotifications;

    public ActivitiesFragment() {
    }

    public void openInfo(View v, Activity activity) {
        mDrawerTitle = (TextView) getView().findViewById(R.id.info_container_title);
        mNotificationView = (TextView) getView().findViewById(R.id.notification_icon);
        mDrawerDescription = (WebView) getView().findViewById(R.id.info_container_description);
        mRightBarGreen = (RelativeLayout) getView().findViewById(R.id.rightbar_green);

        mNotifications = mDataBase.getActivityNotification(activity.getIdActivityStudent());

        mDrawer.setVisibility(View.VISIBLE);

        mDrawerLayout.setScrimColor(getResources().getColor(R.color.white_transparent));

        mDrawerTitle.setText(activity.getTitle());
        mDrawerDescription.loadDataWithBaseURL("", activity.getDescription(), "text/html", "UTF-8", "about:blank");
        mDrawerDescription.setBackgroundColor(getResources().getColor(R.color.gray_4));
        if (mNotifications != 0) {
            mRightBarGreen.setVisibility(View.VISIBLE);
            mNotificationView.setVisibility(View.VISIBLE);
            mNotificationView.setText(Integer.toString(mNotifications));
        }

        if (mDrawerLayout.isDrawerOpen(mDrawer)) {
            mDrawerLayout.closeDrawer(mDrawer);
        } else {
            mDrawerLayout.openDrawer(mDrawer);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activities, null);
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.activity_portfolio_activity);
        mDrawer = (RelativeLayout) view.findViewById(R.id.slider);

        initCommentsTab(view);

        return view;
    }

    private void initCommentsTab(final View view) {
        mDrawer.setVisibility(View.INVISIBLE);
        mDrawer.requestLayout();
        mDrawer.bringToFront();
        mDrawerLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    public void init() {
        sSingleton = Singleton.getInstance();
        mDataBase = DataBase.getInstance(getActivity());

        try {
            mList = mDataBase.selectListActivitiesAndStudents(sSingleton.portfolioClass.getIdPortClass(), sSingleton.portfolioClass.getPerfil(), sSingleton.user.getIdUser());
            Log.e("BANCO", "atividades (SelectActivitiesAactivity):" + mList.toString());
        } catch (Exception e) {
            Log.e("BANCO", "falha em pegar atividades (SelectActivitiesAactivity):" + e.getMessage());
        }

        mSearchView = (SearchView) getView().findViewById(R.id.edt_search);

        mClassName = (TextView) getView().findViewById(R.id.class_name);
        mPortfolioName = (TextView) getView().findViewById(R.id.portfolio_name);

        mClassName.setText(sSingleton.portfolioClass.getClassCode());
        mPortfolioName.setText(sSingleton.portfolioClass.getPortfolioTitle());

        mGridAdapter = new StudentActivitiesAdapter((MainActivity) getActivity(), mList, this);

        mListActivities = (ListView) getView().findViewById(R.id.list_activities);
        mListActivities.setAdapter(mGridAdapter);
        mListActivities.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Log.d("tela atividades","clicou na caixa");
                getView().findViewById(R.id.activities_list).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        if (sSingleton.portfolioClass.getPerfil().equals("S")) {
            mSearchView.setVisibility(View.INVISIBLE);
        }

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ActivitiesFragment.this.mGridAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}
