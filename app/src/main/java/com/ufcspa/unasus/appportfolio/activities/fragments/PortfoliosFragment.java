package com.ufcspa.unasus.appportfolio.activities.fragments;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.adapter.SelectPortfolioClassAdapter;
import com.ufcspa.unasus.appportfolio.database.DataBase;
import com.ufcspa.unasus.appportfolio.model.PortfolioClass;
import com.ufcspa.unasus.appportfolio.model.Singleton;
import com.ufcspa.unasus.appportfolio.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Desenvolvimento on 12/01/2016.
 */
public class PortfoliosFragment extends HelperFragment implements SelectPortfolioClassAdapter.OnInfoButtonClick {
    private GridView mGridClasses;
    private DataBase mDataBase;
    private static Singleton sSingleton;
    private List<PortfolioClass> mListPortClasses;
    private Button mBtSync;
    private RelativeLayout mDrawer;
    private DrawerLayout mDrawerLayout;
    private TextView mDrawerTitle;
    private WebView mDrawerDescription;
    private TextView mNotificationView;
    private RelativeLayout mRightBarGreen;
    private int mNotifications;

    public PortfoliosFragment() {
    }

    public void openInfo(View v, PortfolioClass portfolioClass) {
        mDrawerTitle = (TextView) getView().findViewById(R.id.info_container_title);
        mNotificationView = (TextView) getView().findViewById(R.id.notification_icon);
        mDrawerDescription = (WebView) getView().findViewById(R.id.info_container_description);
        mRightBarGreen = (RelativeLayout) getView().findViewById(R.id.rightbar_green);

        mNotifications = mDataBase.getPortfolioClassNotification(portfolioClass.getIdPortClass());

        mDrawer.setVisibility(View.VISIBLE);

        mDrawerLayout.setScrimColor(getResources().getColor(R.color.white_transparent));

        mDrawerTitle.setText(portfolioClass.getPortfolioTitle());
        mDrawerDescription.loadDataWithBaseURL("", mDataBase.getPortfolioDescription(portfolioClass.getIdPortClass()), "text/html", "UTF-8", "about:blank");
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
        View view = inflater.inflate(R.layout.fragment_portfolios, null);
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.activity_classes);
        mDrawer = (RelativeLayout) view.findViewById(R.id.slider);
        mBtSync = (Button) view.findViewById(R.id.bt_sync);
        final RotateAnimation rotate = new RotateAnimation(0, -360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotate.setDuration(500);

        sSingleton = Singleton.getInstance();

        /*if (sSingleton.guestUser) {
            mBtSync.setVisibility(View.VISIBLE);
            view.findViewById(R.id.tx_sync).setVisibility(View.VISIBLE);
            mBtSync.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBtSync.startAnimation(rotate);
                    SyncVisitanteClient svClient = new SyncVisitanteClient(getContext());
                    svClient.postJson(SyncVisitante.toJSON(Singleton.getInstance().user.getIdUser(), Singleton.getInstance().device.get_id_device()));
                }
            });
        }*/

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

    private void init() {
        mDataBase = DataBase.getInstance(getActivity());

        try {
            mListPortClasses = mDataBase.selectListClassAndUserType(sSingleton.user.getIdUser());
            Log.d("lista", "tam portlis:" + mListPortClasses.size());
        } catch (Exception e) {
            Log.wtf("ERRO", e.getMessage());
        }

        Collections.sort(mListPortClasses);

        SelectPortfolioClassAdapter gridAdapter = new SelectPortfolioClassAdapter(getActivity(), mListPortClasses);
        gridAdapter.setOnInfoButtonClickListener(this);
        mGridClasses = (GridView) getView().findViewById(R.id.grid_classes);
        mGridClasses.setAdapter(gridAdapter);
    }
}
