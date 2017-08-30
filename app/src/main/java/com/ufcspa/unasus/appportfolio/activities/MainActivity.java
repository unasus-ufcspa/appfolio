package com.ufcspa.unasus.appportfolio.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
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
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.targets.Target;
import com.mikepenz.crossfader.Crossfader;
import com.ufcspa.unasus.appportfolio.activities.fragments.ActivitiesFragment;
import com.ufcspa.unasus.appportfolio.activities.fragments.AttachmentFragment;
import com.ufcspa.unasus.appportfolio.activities.fragments.ConfigFragment;
import com.ufcspa.unasus.appportfolio.activities.fragments.RTEditorFragment;
import com.ufcspa.unasus.appportfolio.activities.fragments.ReferenceFragment;
import com.ufcspa.unasus.appportfolio.activities.fragments.ReportFragment;
import com.ufcspa.unasus.appportfolio.activities.fragments.ReportPortfolioFragment;
import com.ufcspa.unasus.appportfolio.activities.fragments.ReportStudentsFragment;
import com.ufcspa.unasus.appportfolio.activities.fragments.PortfoliosFragment;
import com.ufcspa.unasus.appportfolio.database.DataBase;
import com.ufcspa.unasus.appportfolio.model.Attachment;
//import com.ufcspa.unasus.appportfolio.model.CustomShowcaseView;
import com.ufcspa.unasus.appportfolio.model.Device;
import com.ufcspa.unasus.appportfolio.model.Note;
import com.ufcspa.unasus.appportfolio.model.PolicyUser;
import com.ufcspa.unasus.appportfolio.model.Singleton;
import com.ufcspa.unasus.appportfolio.model.User;
import com.ufcspa.unasus.appportfolio.notifications.NotificationEventReceiver;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.webClient.BasicData;
import com.ufcspa.unasus.appportfolio.webClient.BasicDataClient;
import com.ufcspa.unasus.appportfolio.webClient.FullData;
import com.ufcspa.unasus.appportfolio.webClient.FullDataClient;
import com.ufcspa.unasus.appportfolio.webClient.PolicyUserClient;
import com.ufcspa.unasus.appportfolio.webClient.SendData;
import com.ufcspa.unasus.appportfolio.webClient.SendFullDataClient;
import com.ufcspa.unasus.appportfolio.webClient.SyncVisitante;
import com.ufcspa.unasus.appportfolio.webClient.SyncVisitanteClient;
import com.ufcspa.unasus.appportfolio.database.DataBaseHelper;

import org.json.JSONObject;

//import io.github.skyhacker2.sqliteonweb.SQLiteOnWeb;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static boolean isFullDataSucessful;
    public static boolean isFullSyncNotSucessful;

    public static boolean shouldSend;
    public static boolean sendResponseNotReceived;
    final GestureDetector gestureDetector = new GestureDetector(new GestureListener());
    private ProgressDialog mDialog;
    private String mLastFragName;
    private Crossfader mCrossFader;
    private View mFragmentContainer;
    private View mBigDrawer;
    private View mMiniDrawer;
    private static Singleton sSingleton;
    private boolean mShouldCreateDrawer;
    private View mClicked;
    private PolicyUser mPolicyUser;
    private PolicyUserClient mPolicyUserClient;
    private int mIdUser;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
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
                DataBase dataBase = DataBase.getInstance(getApplicationContext());
                String name = input.getText().toString();
                if (name.isEmpty()) {
                    name = "Anexo";
                }
                sSingleton.lastIdAttach = dataBase.insertAttachment(new Attachment(0, type, name, path, 0, 0));
                //dataBase.insertAttachActivity(sSingleton.lastIdAttach, sSingleton.idActivityStudent);
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
                mShouldCreateDrawer = true;
            } else {
                mShouldCreateDrawer = extras.getBoolean("mShouldCreateDrawer", true);
            }
        } else {
            mShouldCreateDrawer = savedInstanceState.getBoolean("mShouldCreateDrawer", true);
        }

        sSingleton = Singleton.getInstance();
        sSingleton.note = new Note(0, "null", 0);

//        sSingleton.guestUser = DataBase.getInstance(this).userIsGuest();

        mFragmentContainer = findViewById(R.id.fragment_container);
        if (mShouldCreateDrawer) {
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            mBigDrawer = inflater.inflate(R.layout.menu_expanded, null);
            initBigDrawer();
            mMiniDrawer = inflater.inflate(R.layout.menu_retracted, null);
            initMiniDrawer();

            apagarBotoes(1);

            final float scale = getResources().getDisplayMetrics().density;
            int pixelsMini = (int) (80 * scale + 0.5f);
            int pixelsBig = (int) (250 * scale + 0.5f);

            mCrossFader = new Crossfader()
                    .withContent(mFragmentContainer)
                    .withFirst(mBigDrawer, pixelsBig)
                    .withSecond(mMiniDrawer, pixelsMini)
                    .withGmailStyleSwiping()
                    .withSavedInstance(savedInstanceState)
                    .build();
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter("call.fragments.action"));

        if (!mShouldCreateDrawer || sSingleton.wasFullscreen) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RTEditorFragment()).addToBackStack("RTEditorFragment").commit();
            mLastFragName = "RTEditorFragment";
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

        if (policyExist()) {
            if (!policyAceita()) {
                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_privacy_policy);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);

                WebView policyTX = (WebView) dialog.findViewById(R.id.term_text);
                Button acceptBT = (Button) dialog.findViewById(R.id.btn_agree);
                Button notAcceptBT = (Button) dialog.findViewById(R.id.btn_not_agree);

                dialog.show();

                mIdUser = Singleton.getInstance().user.getIdUser();

                String txPolicy = DataBase.getInstance(getBaseContext()).getPolicyByUserID(mIdUser).getTxPolicy();

                policyTX.loadDataWithBaseURL("file:" + Environment.getExternalStorageDirectory() + "/Android/data/com.ufcspa.unasus.appportfolio/files/images", txPolicy, "text/html", "UTF-8", "about:blank");

                acceptBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPolicyUser = DataBase.getInstance(getBaseContext()).getPolicyUserByUserId(mIdUser);
                        DataBase.getInstance(getBaseContext()).updateFlAccept(mPolicyUser.getIdPolicyUser());
                        mPolicyUser = DataBase.getInstance(getBaseContext()).getPolicyUserByUserId(mIdUser);
                        Log.d("mPolicyUser", mPolicyUser.toString());
                        mPolicyUserClient = new PolicyUserClient(getBaseContext());
                        mPolicyUserClient.postJson(PolicyUser.toJSON(mPolicyUser.getIdPolicyUser(), mPolicyUser.getIdUser(), mPolicyUser.getFlAccept()));
                        dialog.dismiss();
                    }
                });
                notAcceptBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "É necessário aceitar os termos para continuar", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    public boolean policyAceita() {
        DataBase bd = DataBase.getInstance(this);
        if (bd.getPolicyUserByUserId(Singleton.getInstance().user.getIdUser()).getFlAccept() != null) {
            return true;
        }
        else
            return false;
    }

    public boolean policyExist() {
        DataBase bd = DataBase.getInstance(this);
        if (bd.getPolicyByUserID(Singleton.getInstance().user.getIdUser()).getIdPolicy() != 0) {
            return true;
        }
        else
            return false;
    }

    //Método para criar show case views dos tutoriais
    public void showCase(Target target, String titulo, String texto) {
       /* int largura = (int) (250 * getResources().getDisplayMetrics().density + 0.5f);
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        if (sSingleton.tutorial) {
            new ShowcaseView.Builder(this)
                    .setTarget(target)
                    .setStyle(R.style.ShowCaseCustom)
                    .setContentTitle(titulo)
                    .setContentText(texto)
                    .hideOnTouchOutside()
                    .setShowcaseDrawer(new CustomShowcaseView(getResources(),largura,point.y))
                    .build();
        }*/
    }

    private void initMiniDrawer() {
        ImageButton portfolios = (ImageButton) mMiniDrawer.findViewById(R.id.btn_members);
        ImageButton activities = (ImageButton) mMiniDrawer.findViewById(R.id.btn_activities);
        ImageButton references = (ImageButton) mMiniDrawer.findViewById(R.id.btn_references);
        ImageButton config = (ImageButton) mMiniDrawer.findViewById(R.id.btn_config);
        ImageButton attachments = (ImageButton) mMiniDrawer.findViewById(R.id.btn_attachments);
        ImageButton reports = (ImageButton) mMiniDrawer.findViewById(R.id.btn_reports);

        portfolios.setOnClickListener(this);
        activities.setOnClickListener(this);
        references.setOnClickListener(this);
        config.setOnClickListener(this);
        attachments.setOnClickListener(this);
        reports.setOnClickListener(this);
    }

    private void initBigDrawer() {
        ImageView logo = (ImageView) mBigDrawer.findViewById(R.id.img_logo);
        RelativeLayout portfolios = (RelativeLayout) mBigDrawer.findViewById(R.id.portfolios);
        RelativeLayout activities = (RelativeLayout) mBigDrawer.findViewById(R.id.activities);
        RelativeLayout references = (RelativeLayout) mBigDrawer.findViewById(R.id.references);
        RelativeLayout config = (RelativeLayout) mBigDrawer.findViewById(R.id.settings);
        RelativeLayout attachments = (RelativeLayout) mBigDrawer.findViewById(R.id.attachments);
        RelativeLayout reports = (RelativeLayout) mBigDrawer.findViewById(R.id.reports);

        portfolios.setOnClickListener(this);
        activities.setOnClickListener(this);
        references.setOnClickListener(this);
        config.setOnClickListener(this);
        attachments.setOnClickListener(this);
        reports.setOnClickListener(this);

        logo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                mClicked = view;
                return !gestureDetector.onTouchEvent(event);
            }
        });
        mBigDrawer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                mClicked = view;
                return !gestureDetector.onTouchEvent(event);
            }
        });
        portfolios.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                mClicked = view;
                return !gestureDetector.onTouchEvent(event);
            }
        });
        activities.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                mClicked = view;
                return !gestureDetector.onTouchEvent(event);
            }
        });
        references.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                mClicked = view;
                return !gestureDetector.onTouchEvent(event);
            }
        });
        config.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                mClicked = view;
                return !gestureDetector.onTouchEvent(event);
            }
        });
        attachments.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                mClicked = view;
                return !gestureDetector.onTouchEvent(event);
            }
        });
        reports.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                mClicked = view;
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
        } else if (v.getId() == R.id.btn_references || v.getId() == R.id.references) {
            changeFragment(2);
        } else if (v.getId() == R.id.btn_config || v.getId() == R.id.settings) {
            changeFragment(3);
        } else if (v.getId() == R.id.btn_attachments || v.getId() == R.id.attachments) {
            changeFragment(4);
        } else if (v.getId() == R.id.btn_reports || v.getId() == R.id.reports) {
            changeFragment(5);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mShouldCreateDrawer)
            outState = mCrossFader.saveInstanceState(outState);
        outState.putBoolean("mShouldCreateDrawer", mShouldCreateDrawer);
        sSingleton.note = new Note(0, "null", 0);

        outState.putString("frag", mLastFragName);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            mShouldCreateDrawer = savedInstanceState.getBoolean("mShouldCreateDrawer");
            mLastFragName = savedInstanceState.getString("frag");

            if (mLastFragName != null)
                switch (mLastFragName) {
                    case "RTEditorFragment":
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RTEditorFragment()).addToBackStack(mLastFragName).commit();
                        apagarBotoes(1);
                        break;
                    case "ReferenceFragment":
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReferenceFragment()).addToBackStack(mLastFragName).commit();
                        break;
                    case "ConfigFragment":
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConfigFragment()).addToBackStack(mLastFragName).commit();
                        break;
                    case "AttachmentFragment":
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AttachmentFragment()).addToBackStack(mLastFragName).commit();
                        break;
                    case "PortfoliosFragment":
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PortfoliosFragment()).addToBackStack(mLastFragName).commit();
                        break;
                    case "ActivitiesFragment":
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ActivitiesFragment()).addToBackStack(mLastFragName).commit();
                        break;
                    case "ReportFragment":
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReportFragment()).addToBackStack(mLastFragName).commit();
                        break;
                    case "ReportPortfolioFragment":
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReportPortfolioFragment()).addToBackStack(mLastFragName).commit();
                        break;
                }
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    public void apagarBotoes (int id) {
        mBigDrawer.findViewById(R.id.portfolios).setAlpha((float) 0.3);
        mBigDrawer.findViewById(R.id.activities).setAlpha((float) 0.3);
        mBigDrawer.findViewById(R.id.attachments).setAlpha((float) 0.3);
        mBigDrawer.findViewById(R.id.references).setAlpha((float) 0.3);
        mBigDrawer.findViewById(R.id.reports).setAlpha((float) 0.3);
        mBigDrawer.findViewById(R.id.settings).setAlpha((float) 0.3);

        mMiniDrawer.findViewById(R.id.btn_members).setAlpha((float) 0.3);
        mMiniDrawer.findViewById(R.id.btn_activities).setAlpha((float) 0.3);
        mMiniDrawer.findViewById(R.id.btn_attachments).setAlpha((float) 0.3);
        mMiniDrawer.findViewById(R.id.btn_references).setAlpha((float) 0.3);
        mMiniDrawer.findViewById(R.id.btn_reports).setAlpha((float) 0.3);
        mMiniDrawer.findViewById(R.id.btn_config).setAlpha((float) 0.3);

        switch (id) {
            case 0:
                mBigDrawer.findViewById(R.id.portfolios).setAlpha((float) 1);
                mMiniDrawer.findViewById(R.id.btn_members).setAlpha((float) 1);
                break;
            case 1:
                if (sSingleton.portfolioClass != null) {
                    mBigDrawer.findViewById(R.id.activities).setAlpha((float)  1);
                    mMiniDrawer.findViewById(R.id.btn_activities).setAlpha((float)  1);
                }
                break;
            case 2:
                mBigDrawer.findViewById(R.id.references).setAlpha((float) 1);
                mMiniDrawer.findViewById(R.id.btn_references).setAlpha((float) 1);
                break;
            case 3:
                mBigDrawer.findViewById(R.id.settings).setAlpha((float) 1);
                mMiniDrawer.findViewById(R.id.btn_config).setAlpha((float) 1);
                break;
            case 4:
                mBigDrawer.findViewById(R.id.attachments).setAlpha((float) 1);
                mMiniDrawer.findViewById(R.id.btn_attachments).setAlpha((float) 1);
                break;
            case 5:
                mBigDrawer.findViewById(R.id.reports).setAlpha((float) 1);
                mMiniDrawer.findViewById(R.id.btn_reports).setAlpha((float) 1);
                break;
            default:
                break;
        }
    }

    private void changeFragment(int id) {
        sSingleton.firsttime = true;
        sSingleton.note = new Note(0, "null", 0);
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(findViewById(R.id.fragment_container).getWindowToken(), 0);
        switch (id) {
            case 0:
                downloadFullData(0, id);
                //getBasicData();//estava executando mais de uma vez.
                sendFullData();
                shouldSend = false;
                apagarBotoes(id);
                break;
            case 1:
                if (sSingleton.portfolioClass != null) {
                    downloadFullData(0, id);
                    apagarBotoes(id);
                } else {
                    Toast.makeText(this, "É necessário selecionar um portfólio", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReferenceFragment()).addToBackStack("ReferenceFragment").commit();
                mLastFragName = "ReferenceFragment";
                apagarBotoes(id);
                break;
            case 3:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConfigFragment()).addToBackStack("ConfigFragment").commit();
                mLastFragName = "ConfigFragment";
                apagarBotoes(id);
                break;
            case 4:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AttachmentFragment()).addToBackStack("AttachmentFragment").commit();
                mLastFragName = "AttachmentFragment";
                apagarBotoes(id);
                break;
            case 5:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReportFragment()).addToBackStack("ReportFragment").commit();
                mLastFragName = "ReportFragment";
                apagarBotoes(id);
                break;
            case 7:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReportPortfolioFragment()).addToBackStack("ReportPortfolioFragment").commit();
                mLastFragName = "ReportPortfolioFragment";
                break;
            case 8:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReportStudentsFragment()).addToBackStack("ReportStudentsFragment").commit();
                mLastFragName = "ReportStudentsFragment";
                break;
            case 6:
                downloadFullData(sSingleton.idActivityStudent, id);
                break;
            default:
                break;
        }
    }

    public void getBasicData() {
        if (isOnline()) {
            BasicDataClient client = new BasicDataClient(getBaseContext());
            JSONObject jsonObject = new JSONObject();
            client.postJson(BasicData.toJSON(Singleton.getInstance().user.getIdUser(), Singleton.getInstance().device.get_id_device())); // mandando id
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                }
            });
        }
    }

    public void hideDrawer() {
        setClickable(mMiniDrawer, false);
        setClickable(mBigDrawer, false);
        mMiniDrawer.setAlpha((float)  0.5);
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
        setClickable(mMiniDrawer, true);
        setClickable(mBigDrawer, true);
        mMiniDrawer.setAlpha(1);
    }

    public void dontCreateCrossfader() {
        sSingleton.firsttime = true;
        if (sSingleton.isFullscreen) {
            Intent main = new Intent(this, MainActivity.class);
            main.putExtra("mShouldCreateDrawer", false);
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

        if (sSingleton.isFullscreen)
            if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                super.onBackPressed();
    }

    public void getFullData(int idActivityStudent) {
        FullDataClient client = new FullDataClient(this);
        client.postJson(FullData.toJSON(sSingleton.device.get_id_device(), idActivityStudent));

        if (sSingleton.portfolioClass == null) {
            if (DataBase.getInstance(this).userIsGuest()) {
                SyncVisitanteClient svClient = new SyncVisitanteClient(this);
                svClient.postJson(SyncVisitante.toJSON(Singleton.getInstance().user.getIdUser(), Singleton.getInstance().device.get_id_device()));
                sSingleton.guestUser = false;
            }
        } else {
            if (DataBase.getInstance(this).userIsGuest(sSingleton.portfolioClass.getIdPortfolioStudent())) {
                SyncVisitanteClient svClient = new SyncVisitanteClient(this);
                svClient.postJson(SyncVisitante.toJSON(Singleton.getInstance().user.getIdUser(), Singleton.getInstance().device.get_id_device()));
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void downloadFullData(final int idActivityStudent, final int changeFragment) {
        isFullDataSucessful = false;
        isFullSyncNotSucessful = false;

        if (isOnline()) {
            mDialog = ProgressDialog.show(this, "Baixando novos dados", "Por favor aguarde...", true);
            final Thread myThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    getFullData(idActivityStudent);

                    while (!isFullDataSucessful)
                        if (isFullSyncNotSucessful)
                            break;

                    if (!isFullSyncNotSucessful) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // Atualizar as notificações e o layout
                                removeProgressBar(changeFragment);
//                                Toast.makeText(getApplicationContext(), "Novos dados baixados com sucesso", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                removeProgressBar(changeFragment);
//                                Toast.makeText(getApplicationContext(), "Erro interno. Por favor tente novamente", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
            myThread.start();
        } else {
            // Atualizar a interface
            removeProgressBar(changeFragment);
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
        if (isOnline() /*&& sSingleton.portfolioClass!=null*/) { // TODO: 19/01/2017 Verificar se ocorre maiores problemas 
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

    public void downloadFullDataComments(final int idActivityStudent) {
        isFullDataSucessful = false;
        isFullSyncNotSucessful = false;

        if (isOnline()) {
            final Thread myThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    getFullData(idActivityStudent);

                    while (!isFullDataSucessful)
                        if (isFullSyncNotSucessful)
                            break;
                }
            });
            myThread.start();
        }
    }

    public void removeProgressBar(int changeFragment) {
        try {
            if (mDialog != null) {
                mDialog.dismiss();
            }

            switch (changeFragment) {
                case 0:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PortfoliosFragment()).addToBackStack("PortfoliosFragment").commitAllowingStateLoss(); //PortfoliosFragment
                    mLastFragName = "PortfoliosFragment";
                    break;
                case 1:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ActivitiesFragment()).addToBackStack("ActivitiesFragment").commitAllowingStateLoss();
                    mLastFragName = "ActivitiesFragment";
                    break;
                case 6:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RTEditorFragment()).addToBackStack("RTEditorFragment").commit();
                    mLastFragName = "RTEditorFragment";
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
        }
    }

    public void logout() {
        DataBase.getInstance(this).cleanDataBase();
        sSingleton.user = new User(0, null, null);
        sSingleton.device = new Device();
        DataBaseHelper.getInstance(this).getDatabase();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        System.exit(0);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            // Trigger the touch event on the calendar
            mClicked.callOnClick();
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
                    mCrossFader.crossFade();
                }
            }

            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Singleton.getInstance().device = DataBase.getInstance(this).getDevice();
        Log.d("main", "GET FROM DEVICE: " + Singleton.getInstance().device);
        NotificationEventReceiver.setupAlarm(this, sSingleton.interval);
    }
}
