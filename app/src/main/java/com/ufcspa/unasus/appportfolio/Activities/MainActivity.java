package com.ufcspa.unasus.appportfolio.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.mikepenz.crossfader.Crossfader;
import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentAttachment;
import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentRTEditor;
import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentSelectPortfolio;
import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentStudentActivities;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Crossfader crossFader;
    private View fragmentContainer;
    private View bigDrawer;
    private View miniDrawer;
    private Singleton singleton;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int id = intent.getIntExtra("ID", 0);
            if(id == 6)
                callAttachments(intent.getIntExtra("Position", -1));
            else
                changeFragment(id);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.RTE_ThemeLight);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        singleton = Singleton.getInstance();

        //////////ID activity do MARIO///////////
        singleton.idActivityStudent=1;
        /////////--------------------///////////


        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        bigDrawer = inflater.inflate(R.layout.big_drawer, null);
        initBigDrawer();
        miniDrawer = inflater.inflate(R.layout.mini_drawer, null);
        initMiniDrawer();

        fragmentContainer = findViewById(R.id.fragment_container);

        final float scale = getResources().getDisplayMetrics().density;
        int pixelsMini = (int) (60 * scale + 0.5f);
        int pixelsBig = (int) (230 * scale + 0.5f);

        crossFader = new Crossfader()
                .withContent(fragmentContainer)
                .withFirst(bigDrawer, pixelsBig)
                .withSecond(miniDrawer, pixelsMini)
                .withGmailStyleSwiping()
                .withSavedInstance(savedInstanceState)
                .build();

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("call.fragments.action"));

        if(savedInstanceState == null)
            changeFragment(0);
    }

    private void initMiniDrawer() {
        ImageButton portfolios = (ImageButton) miniDrawer.findViewById(R.id.btn_members);
        ImageButton activities  = (ImageButton) miniDrawer.findViewById(R.id.btn_activities);
        ImageButton reports = (ImageButton) miniDrawer.findViewById(R.id.btn_reports);
        ImageButton config = (ImageButton) miniDrawer.findViewById(R.id.btn_config);
        ImageButton attachments = (ImageButton) miniDrawer.findViewById(R.id.btn_attachments);

        portfolios.setOnClickListener(this);
        activities.setOnClickListener(this);
        reports.setOnClickListener(this);
        config.setOnClickListener(this);
        attachments.setOnClickListener(this);
    }

    private void initBigDrawer() {
        LinearLayout portfolios = (LinearLayout) bigDrawer.findViewById(R.id.portfolios);
        LinearLayout activities  = (LinearLayout) bigDrawer.findViewById(R.id.activities);
        LinearLayout reports = (LinearLayout) bigDrawer.findViewById(R.id.reports);
        LinearLayout config = (LinearLayout) bigDrawer.findViewById(R.id.settings);
        LinearLayout attachments = (LinearLayout) bigDrawer.findViewById(R.id.attachments);

        portfolios.setOnClickListener(this);
        activities.setOnClickListener(this);
        reports.setOnClickListener(this);
        config.setOnClickListener(this);
        attachments.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_members || v.getId() == R.id.portfolios)
        {
            changeFragment(0);
        }
        else if(v.getId() == R.id.btn_activities || v.getId() == R.id.activities)
        {
            changeFragment(1);
        }
        else if(v.getId() == R.id.btn_reports || v.getId() == R.id.reports)
        {
            changeFragment(2);
        }
        else if(v.getId() == R.id.btn_config || v.getId() == R.id.settings)
        {
            changeFragment(3);
        }
        else if(v.getId() == R.id.btn_attachments || v.getId() == R.id.attachments)
        {
            changeFragment(4);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = crossFader.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    private void changeFragment(int id)
    {
        switch (id)
        {
            case 0:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentRTEditor()).commit();//FragmentSelectPortfolio
                break;
            case 1:
                if(singleton.portfolioClass != null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentStudentActivities()).commit();
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                singleton.isRTEditor = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentAttachment()).commit();
                break;
            case 5:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentRTEditor()).commit();
                break;
            default:
                break;
        }
    }

    public void hideDrawer() {
    }

    public void callRTEditorToAttachSomething(String url, int position, String type)
    {
        FragmentRTEditor fragment = new FragmentRTEditor();
        Bundle args = new Bundle();
        args.putString("URL",url);
        args.putString("Type",type);
        args.putInt("Position",position);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    public void callAttachments(int position)
    {
        singleton.isRTEditor = true;

        FragmentAttachment fragment = new FragmentAttachment();
        Bundle args = new Bundle();
        args.putInt("Position",position);
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

}
