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
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mikepenz.crossfader.Crossfader;
import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragRef;
import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentAttachment;
import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentRTEditor;
import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentSelectPortfolio;
import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentStudentActivities;
import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.Model.Note;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final GestureDetector gestureDetector = new GestureDetector(new GestureListener());
    private Crossfader crossFader;
    private View fragmentContainer;
    private View bigDrawer;
    private View miniDrawer;
    private Singleton singleton;
    private boolean shouldCreateDrawer;
    private View clicked;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("Image"))
                insertFileIntoDataBase(intent.getStringExtra("Image"), "I");
            else if (intent.hasExtra("Video"))
                insertFileIntoDataBase(intent.getStringExtra("Video"), "V");
            else {
                int id = intent.getIntExtra("ID", 0);
                changeFragment(id);
            }
        }
    };

    public void insertFileIntoDataBase(final String path, final String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Escolha um nome:");
        builder.setCancelable(false);

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
                dataBaseAdapter.insertAttachActivity(singleton.lastIdAttach, singleton.idActivityStudent);
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

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                shouldCreateDrawer = true;
            } else {
                shouldCreateDrawer = extras.getBoolean("shouldCreateDrawer", true);
            }
        } else {
            shouldCreateDrawer = savedInstanceState.getBoolean("shouldCreateDrawer", true);
        }

        singleton = Singleton.getInstance();
        singleton.note = new Note(0, "null", 0);
        //////////ID activity do MARIO///////////
//        singleton.idActivityStudent=1;
        /////////--------------------///////////

        fragmentContainer = findViewById(R.id.fragment_container);
        if (shouldCreateDrawer) {
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            bigDrawer = inflater.inflate(R.layout.big_drawer, null);
            initBigDrawer();
            miniDrawer = inflater.inflate(R.layout.mini_drawer, null);
            initMiniDrawer();

            final float scale = getResources().getDisplayMetrics().density;
            int pixelsMini = (int) (65 * scale + 0.5f);
            int pixelsBig = (int) (200 * scale + 0.5f);


            crossFader = new Crossfader()
                    .withContent(fragmentContainer)
                    .withFirst(bigDrawer, pixelsBig)
                    .withSecond(miniDrawer, pixelsMini)
                    .withGmailStyleSwiping()
                    .withSavedInstance(savedInstanceState)
                    .build();
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("call.fragments.action"));

        if (!shouldCreateDrawer || singleton.wasFullscreen)
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentRTEditor()).commit();
        else if (savedInstanceState == null)
           /*
             QUAL FRAGMENT IRA INICIAR APOS LOGIN?
             0 para select portfolio
             5 para rtEditor
             */
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
        ImageView logo = (ImageView) bigDrawer.findViewById(R.id.img_logo);
        RelativeLayout portfolios = (RelativeLayout) bigDrawer.findViewById(R.id.portfolios);
        RelativeLayout activities = (RelativeLayout) bigDrawer.findViewById(R.id.activities);
        RelativeLayout reports = (RelativeLayout) bigDrawer.findViewById(R.id.reports);
        RelativeLayout config = (RelativeLayout) bigDrawer.findViewById(R.id.settings);
        RelativeLayout attachments = (RelativeLayout) bigDrawer.findViewById(R.id.attachments);

        portfolios.setOnClickListener(this);
        activities.setOnClickListener(this);
        reports.setOnClickListener(this);
        config.setOnClickListener(this);
        attachments.setOnClickListener(this);

        logo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                clicked = view;
                if (gestureDetector.onTouchEvent(event)) {
                    return false;
                }
                return true;
            }
        });
        bigDrawer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                clicked = view;
                if (gestureDetector.onTouchEvent(event)) {
                    return false;
                }
                return true;
            }
        });
        portfolios.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                clicked = view;
                if (gestureDetector.onTouchEvent(event)) {
                    return false;
                }
                return true;
            }
        });
        activities.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                clicked = view;
                if (gestureDetector.onTouchEvent(event)) {
                    return false;
                }
                return true;
            }
        });
        reports.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                clicked = view;
                if (gestureDetector.onTouchEvent(event)) {
                    return false;
                }
                return true;
            }
        });
        config.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                clicked = view;
                if (gestureDetector.onTouchEvent(event)) {
                    return false;
                }
                return true;
            }
        });
        attachments.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                clicked = view;
                if (gestureDetector.onTouchEvent(event)) {
                    return false;
                }
                return true;
            }
        });
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
        if (shouldCreateDrawer)
            outState = crossFader.saveInstanceState(outState);
        outState.putBoolean("shouldCreateDrawer", shouldCreateDrawer);
        singleton.note = new Note(0, "null", 0);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null)
            shouldCreateDrawer = savedInstanceState.getBoolean("shouldCreateDrawer");
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    private void changeFragment(int id)
    {
        singleton.firsttime = true;
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(findViewById(R.id.fragment_container).getWindowToken(), 0);
        switch (id)
        {
            case 0:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentSelectPortfolio()).addToBackStack("Frag").commit();//FragmentSelectPortfolio
                break;
            case 1:
                if(singleton.portfolioClass != null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentStudentActivities()).addToBackStack("Frag").commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragRef()).addToBackStack("Frag").commit();
                break;
            case 3:
                break;
            case 4:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentAttachment()).addToBackStack("Frag").commit();
                break;
            case 5:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentRTEditor()).addToBackStack("Frag").commit();
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

    public void dontCreateCrossfader() {
        singleton.firsttime = true;
        if (singleton.isFullscreen) {
            Intent main = new Intent(this, MainActivity.class);
            main.putExtra("shouldCreateDrawer", false);
            startActivity(main);
        } else
            startActivity(new Intent(this, MainActivity.class));

        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1)
            super.onBackPressed();

        if (singleton.isFullscreen)
            if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                super.onBackPressed();
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            // Trigger the touch event on the calendar
            clicked.callOnClick();
            return super.onSingleTapUp(event);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            ViewConfiguration viewConfiguration = ViewConfiguration.get(MainActivity.this);
            int minSwipeDistance = viewConfiguration.getScaledPagingTouchSlop();
            int minSwipeVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
            int maxSwipeOffPath = viewConfiguration.getScaledTouchSlop();

            if (Math.abs(e1.getY() - e2.getY()) > maxSwipeOffPath) {
                return false;
            }

            if (Math.abs(velocityX) > minSwipeVelocity) {
                // Right to left swipe
                if (e1.getX() - e2.getX() > minSwipeDistance) {
                    crossFader.crossFade();
                }
            }

            return false;
        }
    }
}
