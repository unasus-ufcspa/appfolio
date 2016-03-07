package com.ufcspa.unasus.appportfolio.Activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.mikepenz.crossfader.Crossfader;
import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentAttachment;
import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentRTEditor;
import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentStudentActivities;
import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Crossfader crossFader;
    private View fragmentContainer;
    private View bigDrawer;
    private View miniDrawer;
    private Singleton singleton;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("Image"))
                insertFileIntoDataBase(intent.getStringExtra("Image"), "I");
            else if (intent.hasExtra("Video"))
                insertFileIntoDataBase(intent.getStringExtra("Video"), "V");
            else {
                int id = intent.getIntExtra("ID", 0);
                if (id == 6)
                    callAttachments(intent.getIntExtra("Position", -1));
                else
                    changeFragment(id);
            }
        }
    };

    public void insertFileIntoDataBase(final String path, final String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Escolha um nome:");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataBaseAdapter dataBaseAdapter = DataBaseAdapter.getInstance(getApplicationContext());
                String name = input.getText().toString();
                if (name.isEmpty()) {
                    name = "Anexo";
                }
                singleton.lastIdAttach = dataBaseAdapter.insertAttachment(new Attachment(0, path, "", type, name, 0));
            }
        });

        builder.show();
    }

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
            // QUAL FRAGMENT IRA INICIAR APOS LOGIN?
            // 0 para select portfolio
            // 5 para rtEditor
            changeFragment(5);
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
        singleton.firsttime = true;
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
        setClickable(miniDrawer, false);
        setClickable(bigDrawer, false);
        miniDrawer.setAlpha((float) 0.5);
    }

    public void setClickable(View view, boolean isClickable) {
        if (view != null) {
            view.setClickable(isClickable);
            if (view instanceof ViewGroup) {
                ViewGroup vg = ((ViewGroup) view);
                for (int i = 0; i < vg.getChildCount(); i++) {
                    setClickable(vg.getChildAt(i), isClickable);
                }
            }
        }
    }

    public void showDrawer() {
        setClickable(miniDrawer, true);
        setClickable(bigDrawer, true);
        miniDrawer.setAlpha(1);
    }

    public void callRTEditorToAttachSomething(String url, int position, String type)
    {
        singleton.firsttime = true;
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
