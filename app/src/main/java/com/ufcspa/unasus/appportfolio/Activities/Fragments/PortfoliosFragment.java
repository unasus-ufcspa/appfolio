package com.ufcspa.unasus.appportfolio.Activities.Fragments;

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

import com.ufcspa.unasus.appportfolio.Adapter.SelectPortfolioClassAdapter;
import com.ufcspa.unasus.appportfolio.Database.DataBase;
import com.ufcspa.unasus.appportfolio.Model.PortfolioClass;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Desenvolvimento on 12/01/2016.
 */
public class PortfoliosFragment extends HelperFragment implements SelectPortfolioClassAdapter.OnInfoButtonClick{
    private GridView grid_classes;
    private DataBase source;
    private Singleton singleton;
    private List<PortfolioClass> portclasses;
    private Button btSync;
    private RelativeLayout mDrawer;
    private DrawerLayout mDrawerLayout;
    private TextView mDrawerTitle;
    private WebView mDrawerDescription;

    void FragmentSelectPortfolio() {
    }

    public void openInfo(View v, PortfolioClass portfolioClass){
        mDrawerTitle = (TextView) getView().findViewById(R.id.info_container_title);
        mDrawerDescription = (WebView) getView().findViewById(R.id.info_container_description);

        mDrawer.setVisibility(View.VISIBLE);

        mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));

        mDrawerTitle.setText(portfolioClass.getPortfolioTitle());
        mDrawerDescription.loadDataWithBaseURL("",source.getPortfolioDescription(portfolioClass.getIdPortClass()),"text/html","UTF-8","about:blank");
        mDrawerDescription.setBackgroundColor(getResources().getColor(R.color.gray_4));

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
        btSync = (Button) view.findViewById(R.id.bt_sync);
        final RotateAnimation rotate = new RotateAnimation(0, -360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotate.setDuration(500);

        singleton = Singleton.getInstance();

        /*if (singleton.guestUser) {
            btSync.setVisibility(View.VISIBLE);
            view.findViewById(R.id.tx_sync).setVisibility(View.VISIBLE);
            btSync.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btSync.startAnimation(rotate);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        source = DataBase.getInstance(getActivity());

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
