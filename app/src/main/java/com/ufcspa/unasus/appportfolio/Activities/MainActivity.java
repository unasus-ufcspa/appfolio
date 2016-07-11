package com.ufcspa.unasus.appportfolio.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
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
import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentAttachment;
import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentConfig;
import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentConfigPassword;
import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentConfigUser;
import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentPrivacyPolicy;
import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentRTEditor;
import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentReference;
import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentSelectPortfolio;
import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentStudentActivities;
import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.Model.Note;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.WebClient.BasicData;
import com.ufcspa.unasus.appportfolio.WebClient.BasicDataClient;
import com.ufcspa.unasus.appportfolio.WebClient.FullData;
import com.ufcspa.unasus.appportfolio.WebClient.FullDataClient;
import com.ufcspa.unasus.appportfolio.WebClient.SendData;
import com.ufcspa.unasus.appportfolio.WebClient.SendFullDataClient;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import org.json.JSONObject;

//import io.github.skyhacker2.sqliteonweb.SQLiteOnWeb;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static boolean isFullDataSucessful;
    public static boolean isFullSyncNotSucessful;

    public static boolean shouldSend;
    public static boolean sendResponseNotReceived;
    final GestureDetector gestureDetector = new GestureDetector(new GestureListener());
    private ProgressDialog dialog;
    private String lastFragName;
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
            if (intent.hasExtra("Image")) {
                insertFileIntoDataBase(intent.getStringExtra("Image"), "I");
            } else if (intent.hasExtra("Video")) {
                insertFileIntoDataBase(intent.getStringExtra("Video"), "V");
            } else {
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
                singleton.lastIdAttach = dataBaseAdapter.insertAttachment(new Attachment(0, type, name, path, 0));
                //dataBaseAdapter.insertAttachActivity(singleton.lastIdAttach, singleton.idActivityStudent);
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

        fragmentContainer = findViewById(R.id.fragment_container);
        if (shouldCreateDrawer) {
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            bigDrawer = inflater.inflate(R.layout.big_drawer, null);
            initBigDrawer();
            miniDrawer = inflater.inflate(R.layout.mini_drawer, null);
            initMiniDrawer();

            final float scale = getResources().getDisplayMetrics().density;
            int pixelsMini = (int) (80 * scale + 0.5f);
            int pixelsBig = (int) (250 * scale + 0.5f);

            crossFader = new Crossfader()
                    .withContent(fragmentContainer)
                    .withFirst(bigDrawer, pixelsBig)
                    .withSecond(miniDrawer, pixelsMini)
                    .withGmailStyleSwiping()
                    .withSavedInstance(savedInstanceState)
                    .build();
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("call.fragments.action"));

        if (!shouldCreateDrawer || singleton.wasFullscreen) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentRTEditor()).addToBackStack("FragmentRTEditor").commit();
            lastFragName = "FragmentRTEditor";
        } else if (savedInstanceState == null)
            changeFragment(0);
        /*
        File exst = Environment.getExternalStorageDirectory();
        String exstPath = exst.getPath();
        File newFile = new File(exstPath+"/folio");
        if(!newFile.exists())//check if file already exists
        {
            newFile.mkdirs();//if not, create it
        }
        */
    }

    private void initMiniDrawer() {
        ImageButton portfolios = (ImageButton) miniDrawer.findViewById(R.id.btn_members);
        ImageButton activities = (ImageButton) miniDrawer.findViewById(R.id.btn_activities);
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
                return !gestureDetector.onTouchEvent(event);
            }
        });
        bigDrawer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                clicked = view;
                return !gestureDetector.onTouchEvent(event);
            }
        });
        portfolios.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                clicked = view;
                return !gestureDetector.onTouchEvent(event);
            }
        });
        activities.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                clicked = view;
                return !gestureDetector.onTouchEvent(event);
            }
        });
        reports.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                clicked = view;
                return !gestureDetector.onTouchEvent(event);
            }
        });
        config.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                clicked = view;
                return !gestureDetector.onTouchEvent(event);
            }
        });
        attachments.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                clicked = view;
                return !gestureDetector.onTouchEvent(event);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_members || v.getId() == R.id.portfolios) {
            changeFragment(0);
        } else if (v.getId() == R.id.btn_activities || v.getId() == R.id.activities) {
            changeFragment(1);
        } else if (v.getId() == R.id.btn_reports || v.getId() == R.id.reports) {
            changeFragment(2);
        } else if (v.getId() == R.id.btn_config || v.getId() == R.id.settings) {
            changeFragment(3);
        } else if (v.getId() == R.id.btn_attachments || v.getId() == R.id.attachments) {
            changeFragment(4);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (shouldCreateDrawer)
            outState = crossFader.saveInstanceState(outState);
        outState.putBoolean("shouldCreateDrawer", shouldCreateDrawer);
        singleton.note = new Note(0, "null", 0);

        outState.putString("frag", lastFragName);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            shouldCreateDrawer = savedInstanceState.getBoolean("shouldCreateDrawer");
            lastFragName = savedInstanceState.getString("frag");

            if (lastFragName != null)
                switch (lastFragName) {
                    case "FragmentRTEditor":
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentRTEditor()).addToBackStack(lastFragName).commit();
                        break;
                    case "FragmentReference":
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentReference()).addToBackStack(lastFragName).commit();
                        break;
                    case "FragmentConfig":
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentConfig()).addToBackStack(lastFragName).commit();
                        break;
                    case "FragmentAttachment":
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentAttachment()).addToBackStack(lastFragName).commit();
                        break;
                    case "FragmentPrivacyPolicy":
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentPrivacyPolicy()).addToBackStack(lastFragName).commit();
                        break;
                    case "FragmentSelectPortfolio":
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentSelectPortfolio()).addToBackStack(lastFragName).commit();
                        break;
                    case "FragmentStudentActivities":
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentStudentActivities()).addToBackStack(lastFragName).commit();
                        break;
                    case "FragmentConfigPassword":
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentConfigPassword()).addToBackStack(lastFragName).commit();
                        break;
                    case "FragmentConfigUser":
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentConfigUser()).addToBackStack(lastFragName).commit();
                        break;
                }
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    private void changeFragment(int id) {
        singleton.firsttime = true;
        singleton.note = new Note(0, "null", 0);
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(findViewById(R.id.fragment_container).getWindowToken(), 0);
        switch (id) {
            case 0:
                downloadFullData(0, id);
                //getBasicData();//estava executando mais de uma vez.
                sendFullData();
                shouldSend = false;
                break;
            case 1:
                if (singleton.portfolioClass != null)
                    downloadFullData(0, id);
                break;
            case 2:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentReference()).addToBackStack("FragmentReference").commit();
                lastFragName = "FragmentReference";
                break;
            case 3:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentConfig()).addToBackStack("FragmentConfig").commit();
                lastFragName = "FragmentConfig";
                break;
            case 4:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentAttachment()).addToBackStack("FragmentAttachment").commit();
                lastFragName = "FragmentAttachment";
                break;
            case 5:
                downloadFullData(singleton.idActivityStudent, id);
                break;
            case 6:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentPrivacyPolicy()).addToBackStack("FragmentPrivacyPolicy").commit();
                lastFragName = "FragmentPrivacyPolicy";
            default:
                break;
        }
    }

    public void getBasicData() {
        if (isOnline()) {
            BasicDataClient client = new BasicDataClient(getBaseContext());
            JSONObject jsonObject = new JSONObject();
            client.postJson(BasicData.toJSON(Singleton.getInstance().user.getIdUser(), Singleton.getInstance().device.get_id_device()));// mandando id
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                }
            });
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

    public void getFullData(int id_activity_student) {
        FullDataClient client = new FullDataClient(this);
        client.postJson(FullData.toJSON(singleton.device.get_id_device(), id_activity_student));
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void downloadFullData(final int id_activity_student, final int change_fragment) {
        isFullDataSucessful = false;
        isFullSyncNotSucessful = false;

        if (isOnline()) {
            dialog = ProgressDialog.show(this, "Baixando novos dados", "Por favor aguarde...", true);
            final Thread myThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    getFullData(id_activity_student);

                    while (!isFullDataSucessful)
                        if (isFullSyncNotSucessful)
                            break;

                    if (!isFullSyncNotSucessful) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // Atualizar as notificações e o layout
                                removeProgressBar(change_fragment);
//                                Toast.makeText(getApplicationContext(), "Novos dados baixados com sucesso", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                removeProgressBar(change_fragment);
//                                Toast.makeText(getApplicationContext(), "Erro interno. Por favor tente novamente", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
            myThread.start();
        } else {
            // Atualizar a interface
            removeProgressBar(change_fragment);
//            Toast.makeText(getApplicationContext(), "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadFullData() {
        shouldSend = true;
        sendResponseNotReceived = true;

        if (isOnline()) {
            final Thread sendThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (shouldSend) {
                        SendData data = new SendData(getApplicationContext());
                        String idDevice = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                        if (data.getSyncs() > 0) {
                            JSONObject send = data.GenerateJSON(idDevice);
                            SendFullDataClient client = new SendFullDataClient(MainActivity.this, data);
                            client.postJson(send);
                        }
                        sendResponseNotReceived = true;
                        while (sendResponseNotReceived) ;
                    }
                }
            });
            sendThread.start();
        }
    }

    public void sendFullData() {
        if (isOnline()) {
            final Thread sendThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    SendData data = new SendData(getApplicationContext());
                    String idDevice = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    if (data.getSyncs() > 0) {
                        JSONObject send = data.GenerateJSON(idDevice);
                        SendFullDataClient client = new SendFullDataClient(MainActivity.this, data);
                        client.postJson(send);
                    }
                }
            });
            sendThread.start();
        }
    }

    public void downloadFullDataComments(final int id_activity_student) {
        isFullDataSucessful = false;
        isFullSyncNotSucessful = false;

        if (isOnline()) {
            final Thread myThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    getFullData(id_activity_student);

                    while (!isFullDataSucessful)
                        if (isFullSyncNotSucessful)
                            break;
                }
            });
            myThread.start();
        }
    }

    public void removeProgressBar(int change_fragment) {
        try {
            if (dialog != null) {
                dialog.dismiss();
            }

            switch (change_fragment) {
                case 0:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentSelectPortfolio()).addToBackStack("FragmentSelectPortfolio").commitAllowingStateLoss();//FragmentSelectPortfolio
                    lastFragName = "FragmentSelectPortfolio";
                    break;
                case 1:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentStudentActivities()).addToBackStack("FragmentStudentActivities").commitAllowingStateLoss();
                    lastFragName = "FragmentStudentActivities";
                    break;
                case 5:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentRTEditor()).addToBackStack("FragmentRTEditor").commit();
                    lastFragName = "FragmentRTEditor";
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
        }
    }

    public void initChangePasswordFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentConfigPassword()).addToBackStack("FragmentConfigPassword").commit();
        lastFragName = "FragmentConfigPassword";
    }

    public void initChangeProfileFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentConfigUser()).addToBackStack("FragmentConfigUser").commit();
        lastFragName = "FragmentConfigUser";
    }

    public void logout() {
        DataBaseAdapter.getInstance(this).cleanDataBase();
        startActivity(new Intent(this, LoginActivity2.class));
        finish();
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

    @Override
    protected void onResume() {
        super.onResume();
        Singleton.getInstance().device=DataBaseAdapter.getInstance(this).getDevice();
        Log.d("main", "GET FROM DEVICE: " + Singleton.getInstance().device);
    }
}
