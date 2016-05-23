package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.onegravity.rteditor.RTEditText;
import com.onegravity.rteditor.RTManager;
import com.onegravity.rteditor.RTToolbar;
import com.onegravity.rteditor.api.RTApi;
import com.onegravity.rteditor.api.RTMediaFactoryImpl;
import com.onegravity.rteditor.api.RTProxyImpl;
import com.onegravity.rteditor.api.format.RTFormat;
import com.onegravity.rteditor.api.format.RTHtml;
import com.onegravity.rteditor.api.media.RTAudio;
import com.onegravity.rteditor.api.media.RTImage;
import com.onegravity.rteditor.api.media.RTVideo;
import com.onegravity.rteditor.converter.ConverterSpannedToHtml;
import com.onegravity.rteditor.effects.Effects;
import com.onegravity.rteditor.spans.BackgroundColorSpan;
import com.ufcspa.unasus.appportfolio.Activities.MainActivity;
import com.ufcspa.unasus.appportfolio.Adapter.VersionsAdapter;
import com.ufcspa.unasus.appportfolio.Model.ActivityStudent;
import com.ufcspa.unasus.appportfolio.Model.Comentario;
import com.ufcspa.unasus.appportfolio.Model.Note;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.Model.Sync;
import com.ufcspa.unasus.appportfolio.Model.User;
import com.ufcspa.unasus.appportfolio.Model.VersionActivity;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class FragmentRTEditor extends Frag {
    // Constante
    static final int REQUEST_FOLIO_ATTACHMENT = 5;
    // Editor
    private RTManager mRTManager;
    private RTEditText mRTMessageField;
    private RTToolbar rtToolbar;
    // Observação
    private int currentSpecificComment;
    private HashMap<Integer, Note> specificCommentsNotes;
    private String selectedActualText = "null";
    private boolean specificCommentsOpen;
    // Layout
    private ViewGroup scrollview;
    private ImageButton fullScreen;
    private Button sendVersion;
    private Switch switchNote;
    private RelativeLayout slider;
    private ViewGroup rightBarSpecificComments;
    private View importPanel;
    private ImageButton personalCommentButton;
    private ImageButton versionsButton;
    private ImageView usrPhoto;
    // Model
    private ActivityStudent acStudent;
    private DataBaseAdapter source;
    private Singleton singleton;
    User userPerfil; // dados do tutor
    // Cores
    private int trasnparent;
    private int greenLight;
    private int greenDark;
    // Versoes
    private VersionsAdapter versionAdapter;
    private ArrayList<VersionActivity> versions;
    // Personal Comment
    private boolean personalCommentChanged;
    private String txtPersonal;
    private EditText edtTextPersonal;

    // Receptor de eventos
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DialogFragment newFragment = new FragmentAttachmentDialog();
            newFragment.setTargetFragment(getParentFragment(), REQUEST_FOLIO_ATTACHMENT);
            newFragment.show(getChildFragmentManager(), "Attachment");
        }
    };

    public FragmentRTEditor() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_FOLIO_ATTACHMENT) {
                String url = null;
                String type = null;
                String smallImagePath = null;

                int idAttachment = data.getIntExtra("idAttachment", -1);

                if (data.hasExtra("url"))
                    url = data.getStringExtra("url");
                if (data.hasExtra("type"))
                    type = data.getStringExtra("type");
                if (data.hasExtra("smallImagePath"))
                    smallImagePath = data.getStringExtra("smallImagePath");

                switch (type) {
                    case "I":
                        if (url != null)
                            putAttachment(url);
                        break;
                    case "V":
                        if (smallImagePath != null)
                            putAttachment(smallImagePath);
                        break;
                    case "T":
                        break;
                    default:
                        break;
                }

                if (idAttachment != -1)
                    source.insertAttachActivity(idAttachment, singleton.idActivityStudent);
            }
        }
    }

    /**
     * MÉTODO PARA CARREGAR  A ULTIMA VERSÃO DO TEXTO
     */
    public void loadLastText() {
        acStudent = source.listLastVersionActivityStudent(singleton.idActivityStudent);
//        if (singleton.firsttime) {
//            mRTMessageField.setRichTextEditing(true, acStudent.getTxtActivity());
//            singleton.firsttime = false;
//        }
        if (singleton.firsttime) {
            String text = source.getTextFromCurrentVersion(singleton.idCurrentVersionActivity);
            mRTMessageField.setRichTextEditing(true, text);
            singleton.firsttime = false;
        }
    }

    /**
     * MÉTODO PARA SALVAR TEXTO NO FORMATO HTML
     */
    public void saveText() {
        Log.d("editor DB", "salvando texto..");

        if (mRTMessageField != null && mRTMessageField.isMediaFactoryRegister() != null) {
            acStudent.setTxtActivity(mRTMessageField.getText(RTFormat.HTML));
            source.updateActivityStudent(acStudent, getActualTime(), singleton.idCurrentVersionActivity);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rteditor, null);

        singleton = Singleton.getInstance();
        source = DataBaseAdapter.getInstance(getActivity());
        if(singleton.portfolioClass.getPerfil().equals("S")) {  // SE È ALUNO, MOSTRAR INFORMAÇÔES DO TUTOR
            userPerfil = source.getTutorPerfil(singleton.idActivityStudent);
            Log.d("tutorPerfil", "tutor data:" + userPerfil.toString());
        }

        specificCommentsOpen = false;

        trasnparent = getResources().getColor(android.R.color.transparent);
        greenLight = getResources().getColor(R.color.base_green_light);
        greenDark = getResources().getColor(R.color.base_green);

        getIdNotesFromDB();

        scrollview = (ViewGroup) view.findViewById(R.id.comments);
        rightBarSpecificComments = (ViewGroup) view.findViewById(R.id.obs_view);

        if (singleton.portfolioClass.getPerfil().equals("T") || (singleton.idCurrentVersionActivity != singleton.idVersionActivity))
            view.findViewById(R.id.rte_toolbar_container).setVisibility(View.INVISIBLE);

        createEditor(view, savedInstanceState);

        fullScreen = (ImageButton) view.findViewById(R.id.fullscreen);
        if (!singleton.isFullscreen)
            fullScreen.setBackgroundResource(R.drawable.fullscreen1);
        else
            fullScreen.setBackgroundResource(R.drawable.fullscreen2);
        fullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("rteditor", mRTMessageField.getText(RTFormat.HTML));
                saveText();

                if (singleton.isFullscreen) {
                    singleton.wasFullscreen = true;
                    singleton.isFullscreen = false;
                } else {
                    singleton.wasFullscreen = false;
                    singleton.isFullscreen = true;
                }

                ((MainActivity) getActivity()).dontCreateCrossfader();
            }
        });

        sendVersion = (Button) view.findViewById(R.id.send_version);
        sendVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (singleton.portfolioClass.getPerfil().equals("S") && (singleton.idCurrentVersionActivity == singleton.idVersionActivity)) {
                if ((singleton.idCurrentVersionActivity == singleton.idVersionActivity)) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Enviar Versão")
                            .setMessage("Você tem certeza que deseja enviar essa versão da atividade?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d("RTEditor", "Enviando versão!");
                                    Toast.makeText(getContext(), "Enviando versão!", Toast.LENGTH_SHORT).show();

                                    saveText();
                                    //POPULA SYNC PARA SINCRONIZAR
                                    Sync sync = new Sync();
                                    sync.setNm_table("tb_version_activity");
                                    sync.setCo_id_table(source.getLastIDVersionActivity(singleton.idActivityStudent));
                                    sync.setId_activity_student(Singleton.getInstance().idActivityStudent);
                                    sync.setId_device(singleton.device.get_id_device());
                                    source.insertIntoTBSync(sync);

                                    //SALVA NOVA VERSION ACTIVITY
                                    DataBaseAdapter data = DataBaseAdapter.getInstance(getContext());
                                    VersionActivity version = new VersionActivity();
                                    version.setTx_activity(mRTMessageField.getText(RTFormat.HTML));
                                    version.setId_activity_student(Singleton.getInstance().idActivityStudent);
                                    version.setDt_last_access(getActualTime());
                                    int id = data.insertVersionActivity(version);
                                    singleton.idVersionActivity = id;
                                    singleton.idCurrentVersionActivity = id;

                                    MainActivity main = ((MainActivity) getActivity());
                                    if (main != null)
                                        main.sendFullData();

                                    if (!singleton.isFullscreen) {
                                        singleton.wasFullscreen = true;
                                        singleton.isFullscreen = false;
                                    } else {
                                        singleton.wasFullscreen = false;
                                        singleton.isFullscreen = true;
                                    }

                                    ((MainActivity) getActivity()).dontCreateCrossfader();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        switchNote = (Switch) view.findViewById(R.id.switch_note);
        switchNote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hideNotes(isChecked);
            }
        });

//        if (singleton.portfolioClass.getPerfil().equals("T") || (singleton.idCurrentVersionActivity != singleton.idVersionActivity)) {
        if (singleton.portfolioClass.getPerfil().equals("T") || (singleton.idCurrentVersionActivity != singleton.idVersionActivity)) {
            sendVersion.setVisibility(View.GONE);
        }

        initCommentsTab(view);
        initTopBar(view);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter("call.attachmentdialog.action"));

        if (source.getLastIDVersionActivity(singleton.idActivityStudent) == 0 && singleton.portfolioClass.getPerfil().equals("S")) {
            VersionActivity version = new VersionActivity();
            version.setTx_activity("");
            version.setId_activity_student(Singleton.getInstance().idActivityStudent);
            version.setDt_last_access(getActualTime());
            int id = source.insertVersionActivity(version);
            singleton.idVersionActivity = id;
            singleton.idCurrentVersionActivity = id;
        }

        verifyNotifications(view);
        removeOthersNotifications();

        return view;
    }

    public void createEditor(View view, Bundle savedInstanceState) {
        // create RTManager
        RTApi rtApi = new RTApi(getActivity(), new RTProxyImpl(getActivity()), new RTMediaFactoryImpl(getActivity(), true));

        mRTManager = new RTManager(rtApi, savedInstanceState, getContext());

        ViewGroup toolbarContainer = (ViewGroup) view.findViewById(R.id.rte_toolbar_container);

        // register toolbar (if it exists)
        rtToolbar = (RTToolbar) view.findViewById(R.id.rte_toolbar);
        if (rtToolbar != null) {
            mRTManager.registerToolbar(toolbarContainer, rtToolbar);
        }

        // register message editor
        mRTMessageField = (RTEditText) view.findViewById(R.id.rtEditText);
        mRTManager.registerEditor(mRTMessageField, true);

        mRTMessageField.setCustomSelectionActionModeCallback(new ActionBarCallBack());
        loadLastText();

        mRTMessageField.addTextChangedListener(new TextWatcher() {
            private float posStart;
            private float posEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                posStart = getCaretYPosition(mRTMessageField.getSelectionStart());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                posEnd = getCaretYPosition(mRTMessageField.getSelectionEnd());
                changeNotePosition();
                setSpecificCommentNoteValue();
            }
        });

        mRTMessageField.post(new Runnable() {
            @Override
            public void run() {
                noteFollowText();
                setSpecificCommentNoteValue();
            }
        });

        mRTMessageField.setLineSpacing(15, 1);

        mRTMessageField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.getId() == R.id.rtEditText && hasFocus) {
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    View view = getView();
                    if (view != null) {
                        SlidingPaneLayout layout = (SlidingPaneLayout) view.findViewById(R.id.rteditor_fragment);
                        layout.openPane();

                        int id = singleton.note.getBtId();
                        if (specificCommentsOpen && id > 0) {
                            ArrayList<Comentario> lista = (ArrayList<Comentario>) source.listComments(singleton.activity.getIdActivityStudent(), "O", id);
                            if (lista == null || lista.size() == 0) {
                                //Remove note from hash map
                                specificCommentsNotes.remove(id);
                                currentSpecificComment--;

                                //Remove background color from text
                                Spannable textSpanned = mRTMessageField.getText();
                                BackgroundColorSpan[] spans = textSpanned.getSpans(0, textSpanned.length(), BackgroundColorSpan.class);

                                for (BackgroundColorSpan s : spans) {
                                    if (s.getId() == id) {
                                        textSpanned.removeSpan(s);
                                        break;
                                    }
                                }

                                //Remove button from right drawer
                                Button btn = (Button) scrollview.findViewById(id);
                                singleton.note = new Note(0, "null", 0);

                                int childs = rightBarSpecificComments.getChildCount();
                                for (int i = childs - 1; i >= 0; i--)
                                    rightBarSpecificComments.removeViewAt(i);

                                final float scale = getResources().getDisplayMetrics().density;
                                int btnBegin = (int) (40 * scale + 0.5f);
                                if (btn.getText().equals("..."))
                                    createMultiButtonsRightTabBar((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE), btn, btnBegin);

                                showCommentsTab(false);

                                //Remove button from rteditor
                                scrollview.removeView(btn);
                                setSpecificCommentNoteValue();
                            }
                        }
                    }
                }
            }
        });

        mRTMessageField.setCanPaste(true);
        mRTManager.setToolbarVisibility(RTManager.ToolbarVisibility.SHOW);
        if (singleton.portfolioClass.getPerfil().equals("T") || (singleton.idCurrentVersionActivity != singleton.idVersionActivity)) {
            mRTMessageField.setKeyListener(null);
            mRTMessageField.setTextIsSelectable(true);
            mRTManager.setToolbarVisibility(RTManager.ToolbarVisibility.HIDE);
        }
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.info_rteditor_container);
        layout.clearFocus();
    }

    public String getActualTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        return strDate;
    }

    @Override
    public void onResume() {
        //saveText();
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mRTManager != null) {
            mRTManager.onSaveInstanceState(outState);
            saveText();
        }
        outState.putInt("currentSpecificComment", currentSpecificComment);
        outState.putSerializable("specificCommentsNotes", specificCommentsNotes);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.getSerializable("specificCommentsNotes") != null) {
                specificCommentsNotes = (HashMap<Integer, Note>) savedInstanceState.getSerializable("specificCommentsNotes");
            }
            currentSpecificComment = savedInstanceState.getInt("currentSpecificComment", -1);
        }

        singleton.firsttime = true;
        loadLastText();
    }

    @Override
    public void onDestroyView() {
        saveText();
        if(personalCommentChanged) {
            int idPersonalComment = source.getPersonalComment(singleton.idActivityStudent);
            String idDevice = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
            Sync sync = new Sync(idDevice, "tb_comment", idPersonalComment, singleton.idActivityStudent);
            DataBaseAdapter.getInstance(getContext()).insertIntoTBSync(sync);
            personalCommentChanged=false;
        }
        if (mRTManager != null) {
            mRTManager.onDestroy(true);
        }
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        super.onStop();
        Fragment frag = getActivity().getSupportFragmentManager().findFragmentById(R.id.comments_container);
        if (frag != null) {
            try {
                getActivity().getSupportFragmentManager().beginTransaction().remove(frag).commitAllowingStateLoss();
            }catch (IllegalStateException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initCommentsTab(final View view) {
        view.findViewById(R.id.usr_photo).bringToFront();

        slider = (RelativeLayout) view.findViewById(R.id.slider);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? dm.widthPixels / 4 : dm.widthPixels / 3;

        SlidingPaneLayout.LayoutParams relativeParams = new SlidingPaneLayout.LayoutParams(new SlidingPaneLayout.LayoutParams(SlidingPaneLayout.LayoutParams.MATCH_PARENT, SlidingPaneLayout.LayoutParams.MATCH_PARENT));
        relativeParams.setMargins(width, 0, 0, 0);
        slider.setLayoutParams(relativeParams);

        slider.requestLayout();
        slider.bringToFront();

        final TextView geral = (TextView) view.findViewById(R.id.btn_geral);
        TextView specific = (TextView) view.findViewById(R.id.btn_specific);

        geral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentsTab(false);
            }
        });

        specific.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getView().findViewById(R.id.general_comment_notice).setVisibility(View.GONE);
                showCommentsTab(true);
            }
        });

//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.comments_container, new FragmentComments()).addToBackStack("Frag").commit();

        final SlidingPaneLayout layout = (SlidingPaneLayout) view.findViewById(R.id.rteditor_fragment);

        layout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                importPanel.setVisibility(View.GONE);
                if (edtTextPersonal != null) {
                    if (source.getPersonalComment(singleton.idActivityStudent) <= 0)// se nao possui nenhum comentario pessoal, cria um novo
                        savePersonalComment(edtTextPersonal.getText().toString(), true);
                    else
                        savePersonalComment(edtTextPersonal.getText().toString(), false); // senao atualiza o antigo
                    edtTextPersonal = null;
                }

                getView().findViewById(R.id.personal_comment_container).setVisibility(View.GONE);

                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }

            @Override
            public void onPanelOpened(View panel) {
//                showCommentsTab(false);
                if (panel != null) {
                    Fragment frag = getActivity().getSupportFragmentManager().findFragmentById(R.id.comments_container);
                    if (frag != null) {
                        getActivity().getSupportFragmentManager().beginTransaction().remove(frag).commit();
                    }

                    int childs = rightBarSpecificComments.getChildCount();
                    for (int i = childs - 1; i >= 0; i--)
                        rightBarSpecificComments.getChildAt(i).setVisibility(View.GONE);
                    slider.findViewById(R.id.rightbar_green).setVisibility(View.INVISIBLE);

                    onClickTopBar(true);

                    verifyNotifications(getView());
                }
            }

            @Override
            public void onPanelClosed(View panel) {
                if (panel != null) {
                    Fragment frag = getActivity().getSupportFragmentManager().findFragmentById(R.id.comments_container);
                    if (frag != null) {
                        String tag = frag.getTag();
                        if (tag.equals("G"))
                            showCommentsTab(false);
                        else {
                            getView().findViewById(R.id.general_comment_notice).setVisibility(View.GONE);
                            showCommentsTab(true);
                        }
                    } else
                        showCommentsTab(false);

                    onClickTopBar(false);
                }
            }
        });

        layout.setSliderFadeColor(getResources().getColor(android.R.color.transparent));
        layout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        layout.openPane();
    }

    private void initTopBar(View view) {
        TextView studentName = (TextView) view.findViewById(R.id.p_student_name);
        TextView activityName = (TextView) view.findViewById(R.id.activity_name);
        usrPhoto = (ImageView) view.findViewById(R.id.usr_photo);
        personalCommentButton = (ImageButton) view.findViewById(R.id.personal_comment);
        versionsButton = (ImageButton) view.findViewById(R.id.btn_versions);

        onClickTopBar(true);

        importPanel = view.findViewById(R.id.versions_container);
        versions = source.getAllVersionsFromActivityStudent(singleton.idActivityStudent);

        GridView versionList = (GridView) importPanel.findViewById(R.id.version_list);
        versionAdapter = new VersionsAdapter(getActivity(), versions);
        versionList.setAdapter(versionAdapter);
        versionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position <= versions.size() - 1) {
                    saveText();

                    singleton.idCurrentVersionActivity = versions.get(position).getId_version_activity();
                    singleton.firsttime = true;

                    removeSpecificCommentsNotifications();

                    if (!singleton.isFullscreen) {
                        singleton.wasFullscreen = true;
                        singleton.isFullscreen = false;
                    } else {
                        singleton.wasFullscreen = false;
                        singleton.isFullscreen = true;
                    }

                    ((MainActivity) getActivity()).dontCreateCrossfader();
                }
            }
        });
        if(singleton.portfolioClass.getPerfil().equals("S")){
            studentName.setText(userPerfil.getName());
            Bitmap photo = userPerfil.getPhotoBitmap();
            if (photo != null)
                usrPhoto.setImageBitmap(photo);
            activityName.setText("Ativ. " + singleton.activity.getNuOrder() + ": " + singleton.activity.getTitle());
        }else{
            studentName.setText(singleton.portfolioClass.getStudentName());
            Bitmap photo = singleton.portfolioClass.getPhoto();
            if (photo != null)
                usrPhoto.setImageBitmap(photo);
            activityName.setText("Ativ. " + singleton.activity.getNuOrder() + ": " + singleton.activity.getTitle());
        }


    }

    private void onClickTopBar(boolean shouldClick) {
        if (shouldClick) {
            if (!singleton.portfolioClass.getPerfil().equals("Z")) {
                personalCommentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        importPanel.setVisibility(View.GONE);
                        displayPersonalComment(getView().findViewById(R.id.personal_comment_container));
                    }
                });
                usrPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        importPanel.setVisibility(View.GONE);
                        displayPersonalComment(getView().findViewById(R.id.personal_comment_container));
                    }
                });
            } else {
                //personalCommentButton.setVisibility(View.INVISIBLE);

            }

            versionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    saveText();
                    versionAdapter.refresh(source.getAllVersionsFromActivityStudent(singleton.idActivityStudent));

                    if (edtTextPersonal != null) {
                        savePersonalComment(edtTextPersonal.getText().toString(), !edtTextPersonal.getText().toString().isEmpty());
                        edtTextPersonal = null;
                    }

                    getView().findViewById(R.id.personal_comment_container).setVisibility(View.GONE);
                    displayVersionsDialog(importPanel);
                }
            });
        } else {
            personalCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            usrPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            versionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }

    private float getCaretYPosition(int position) {
        Layout layout = mRTMessageField.getLayout();
        if (layout != null) {
            int line = layout.getLineForOffset(position);
            int baseline = layout.getLineBaseline(line);
            int ascent = layout.getLineAscent(line);
            return baseline + ascent;
        }
        return 0;
    }

    /**
     * MÉTODO PARA RECUPERAR A ID DE CADA NOTA SALVA NO BANCO
     */
    public void getIdNotesFromDB() {
        specificCommentsNotes = new HashMap<>();
        ArrayList<Integer> ids = (ArrayList<Integer>) source.listSpecificComments(singleton.idActivityStudent); //source.listNotesSpecificComments(singleton.idCurrentVersionActivity);
        for (int id : ids) {
            specificCommentsNotes.put(id, new Note(id, "", 0));
        }
        currentSpecificComment = specificCommentsNotes.size();
        Log.d("editor notes", "currentSpecificComment:" + currentSpecificComment);
    }

    private Button createButton(final int id, final String value, final float yPosition) {
        Context context = getContext();
        if (context != null) {
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Button note = (Button) inflater.inflate(R.layout.btn_specific_comment, scrollview, false);

            note.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeColor(id);

                    Button btn = (Button) v;
                    btn.setBackgroundResource(R.drawable.rounded_corner);
                    btn.setTextColor(Color.WHITE);

                    ArrayList<Note> arrayAux = new ArrayList<>(specificCommentsNotes.values());

                    for (int i = 0; i < arrayAux.size(); i++) {
                        Button aux = (Button) getView().findViewById(arrayAux.get(i).getBtId());
                        if (aux.getId() != btn.getId()) {
                            aux.setBackgroundResource(R.drawable.btn_border);
                            aux.setTextColor(greenDark);
                        }
                    }

                    singleton.note.setBtY(yPosition);
                    singleton.note.setSelectedText(selectedActualText);
                    singleton.note.setBtId(id);

                    getView().findViewById(R.id.general_comment_notice).setVisibility(View.GONE);
                    showCommentsTab(true);

                    final float scale = getResources().getDisplayMetrics().density;
                    int btnBegin = (int) (40 * scale + 0.5f);

                    int childs = rightBarSpecificComments.getChildCount();
                    for (int i = childs - 1; i >= 0; i--)
                        rightBarSpecificComments.removeViewAt(i);

                    if (btn.getText().equals("..."))
                        createMultiButtonsRightTabBar(inflater, btn, btnBegin);
                    else {
                        Button btn_view = (Button) inflater.inflate(R.layout.btn_specific_comment, rightBarSpecificComments, false);
                        btn_view.setText(btn.getText());
                        btn_view.setY(btnBegin);
                        btn_view.setBackgroundResource(R.drawable.rounded_corner);
                        btn_view.setTextColor(Color.WHITE);
                        btn_view.setId(id);
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btn_view.getLayoutParams();
                        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        btn_view.setLayoutParams(params);
                        rightBarSpecificComments.addView(btn_view);
                    }
                }
            });

            note.setY(yPosition);
            note.setX(5);
            note.setId(id);
            note.setText(value);

            return note;
        }
        return null;
    }

    private void createMultiButtonsRightTabBar(LayoutInflater inflater, Button current, int beginHeight) {
        int i = beginHeight;
        for (final Note n : specificCommentsNotes.values()) {
            if (n.getBtY() == current.getY()) {
                Button btn_view = (Button) inflater.inflate(R.layout.btn_specific_comment, rightBarSpecificComments, false);

                btn_view.setText(String.valueOf(n.getBtId()));
                if (n.getBtId() == current.getId()) {
                    btn_view.setBackgroundResource(R.drawable.rounded_corner);
                    btn_view.setTextColor(Color.WHITE);
                } else {
                    btn_view.setBackgroundResource(R.drawable.btn_border);
                    btn_view.setTextColor(greenDark);
                }
                btn_view.setY(i);
                btn_view.setTag(n.getBtId());
                btn_view.setId(n.getBtId());

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btn_view.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                btn_view.setLayoutParams(params);

                btn_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeColor((int) v.getTag());

                        Button btn = (Button) v;
                        btn.setBackgroundResource(R.drawable.rounded_corner);
                        btn.setTextColor(Color.WHITE);

                        ArrayList<Note> arrayAux = new ArrayList<>(specificCommentsNotes.values());

                        for (int i = 0; i < arrayAux.size(); i++) {
                            Button aux = (Button) getView().findViewWithTag(arrayAux.get(i).getBtId());
                            if (aux != null && aux.getTag() != btn.getTag()) {
                                aux.setBackgroundResource(R.drawable.btn_border);
                                aux.setTextColor(greenDark);
                            }
                        }
                        singleton.note.setBtY(n.getBtY());
                        singleton.note.setSelectedText(selectedActualText);
                        singleton.note.setBtId((int) btn.getTag());

                        getView().findViewById(R.id.general_comment_notice).setVisibility(View.GONE);
                        showCommentsTab(true);
                    }
                });

                rightBarSpecificComments.addView(btn_view);

                final float scale = getResources().getDisplayMetrics().density;
                int btnHeight = (int) (27 * scale + 0.5f);

                i += btnHeight;
            }
        }
    }

    private void setSpecificCommentNoteValue() {
        View v = getView();

        if (v != null) {
            ArrayList<Note> aux = new ArrayList<>();
            aux.addAll(specificCommentsNotes.values());

            for (int i = 0; i < aux.size(); i++) {
                Note first = aux.get(i);
                boolean enter = false;
                for (int j = aux.size() - 1; j > i; j--) {
                    Note second = aux.get(j);
                    if (first.compareTo(second) == 0) {
                        Button btnFirst = (Button) v.findViewById(first.getBtId());
                        Button btnSecond = (Button) v.findViewById(second.getBtId());

                        if (btnFirst != null)
                            btnFirst.setText("...");
                        if (btnSecond != null)
                            btnSecond.setText("...");

                        aux.remove(second);
                        enter = true;
                    }
                }
                if (!enter) {
                    Button b = (Button) v.findViewById(first.getBtId());
                    if (b != null)
                        b.setText(String.valueOf(first.getBtId()));
                }
            }
        }
    }

    /**
     * MÉTODO PARA RECUPERAR O TEXTO SELECIONADO NO FORMATO HTML
     */
    private String getSelectedText() {
        int selStart = mRTMessageField.getSelectionStart();
        int selEnd = mRTMessageField.getSelectionEnd();
        Spannable text = (Spannable) mRTMessageField.getText().subSequence(selStart, selEnd);
        RTHtml<RTImage, RTAudio, RTVideo> rtHtml = new ConverterSpannedToHtml().convert(text, RTFormat.HTML);
        String thatsMySelectionInHTML = rtHtml.getText();
        return thatsMySelectionInHTML;
    }

    /**
     * MÉTODO PARA ALTERAR A COR DO TEXTO PELA ID DE UMA NOTA
     */
    private void changeColor(int id) {
        Spannable textSpanned = mRTMessageField.getText();
        BackgroundColorSpan[] spans = textSpanned.getSpans(0, textSpanned.length(), BackgroundColorSpan.class);

        BackgroundColorSpan aux = null;
        int auxStart = 0;
        int auxEnd = 0;

        for (BackgroundColorSpan spm : spans) {
            if (spm.getId() == id) {
                aux = spm;
                auxStart = textSpanned.getSpanStart(spm);
                auxEnd = textSpanned.getSpanEnd(spm);
            } else {
                if (spm.getId() != -1 && spm.getBackgroundColor() != greenLight)
                    spm.setColor(greenLight);
            }
        }

        if (aux != null) {
            textSpanned.removeSpan(aux);
            aux.setColor(greenDark);
            textSpanned.setSpan(aux, auxStart, auxEnd, 1);
        }

        mRTMessageField.invalidate();
    }

    public void showCommentsTab(Boolean isSpecificComment) {
        specificCommentsOpen = isSpecificComment;
        if (isSpecificComment) {
            if (singleton.note.getBtId() != 0)
                slider.findViewById(R.id.rightbar_green).setVisibility(View.VISIBLE);
            int childs = rightBarSpecificComments.getChildCount();
            for (int i = childs - 1; i >= 0; i--)
                rightBarSpecificComments.getChildAt(i).setVisibility(View.VISIBLE);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.comments_container, new FragmentSpecificComments(), "S").commit();
            SlidingPaneLayout layout = (SlidingPaneLayout) getView().findViewById(R.id.rteditor_fragment);
            layout.closePane();
        } else {
            removeGeneralCommentsNotifications();
            getView().findViewById(R.id.general_comment_notice).setVisibility(View.GONE);

            int childs = rightBarSpecificComments.getChildCount();
            for (int i = childs - 1; i >= 0; i--)
                rightBarSpecificComments.getChildAt(i).setVisibility(View.GONE);
            slider.findViewById(R.id.rightbar_green).setVisibility(View.INVISIBLE);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.comments_container, new FragmentComments(), "G").commit();
        }
    }

    private void changeNotePosition() {
        try {
            if (specificCommentsNotes != null && specificCommentsNotes.size() != 0 && !switchNote.isChecked()) {
                Spannable textSpanned = mRTMessageField.getText();
                BackgroundColorSpan[] spans = textSpanned.getSpans(0, textSpanned.length(), BackgroundColorSpan.class);

                for (BackgroundColorSpan spm : spans) {
                    if (spm.getId() != -1) {
                        Note aux = specificCommentsNotes.get(spm.getId());
                        float bcsPosition = getCaretYPosition(textSpanned.getSpanStart(spm));
                        if (aux != null && bcsPosition != aux.getBtY()) {
                            aux.setBtY(bcsPosition);
                            Button btn = (Button) scrollview.findViewById(aux.getBtId());
                            btn.setY(bcsPosition);
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
        }
    }

    private void noteFollowText() {
        Spannable textSpanned = mRTMessageField.getText();
        BackgroundColorSpan[] spans = textSpanned.getSpans(0, textSpanned.length(), BackgroundColorSpan.class);

        for (BackgroundColorSpan spm : spans) {
            if (spm.getId() != -1) {
                Note aux = specificCommentsNotes.get(spm.getId());
                if (aux != null)
                    aux.setBtY(getCaretYPosition(textSpanned.getSpanStart(spm)));
                else
                    textSpanned.removeSpan(spm);
            }
        }

        if (mRTManager.getActiveEditor() != null) {
            RTHtml<RTImage, RTAudio, RTVideo> rtHtml = new ConverterSpannedToHtml().convert(textSpanned, RTFormat.HTML);
            mRTMessageField.setRichTextEditing(true, rtHtml.getText());
        }

        ArrayList<Note> arrayAux = new ArrayList<>(specificCommentsNotes.values());

        for (int i = 0; i < arrayAux.size(); i++) {
            Note aux = arrayAux.get(i);
            Button btn = createButton(aux.getBtId(), String.valueOf(aux.getBtId()), aux.getBtY());
            if (btn != null) {
                scrollview.addView(btn);
            }
        }
    }

    private void putAttachment(String url) {
        int selStart = mRTMessageField.getSelectionStart();
        Spannable textBefore = (Spannable) mRTMessageField.getText().subSequence(0, selStart);
        Spannable textAfter = (Spannable) mRTMessageField.getText().subSequence(selStart, mRTMessageField.length());
        RTHtml<RTImage, RTAudio, RTVideo> rtHtmlBefore = new ConverterSpannedToHtml().convert(textBefore, RTFormat.HTML);
        RTHtml<RTImage, RTAudio, RTVideo> rtHtmlAfter = new ConverterSpannedToHtml().convert(textAfter, RTFormat.HTML);

        mRTMessageField.setRichTextEditing(true, rtHtmlBefore.getText() + "<img src=\"" + url + "\">" + rtHtmlAfter.getText());
    }

    private boolean canCreateButton(int start, int end) {
        boolean canCreate = true;
        Spannable textSpanned = mRTMessageField.getText();
        BackgroundColorSpan[] spans = textSpanned.getSpans(start, end, BackgroundColorSpan.class);

        for (BackgroundColorSpan bcs : spans) {
            if (textSpanned.getSpanStart(bcs) == start && textSpanned.getSpanEnd(bcs) == end) {
                canCreate = false;
                break;
            }
        }

        return canCreate;
    }

    private void displayPersonalComment(View anchorView) {
        if (anchorView.getVisibility() == View.VISIBLE)
            anchorView.setVisibility(View.GONE);
        else {
            anchorView.setVisibility(View.VISIBLE);
            TextView student_name = (TextView) anchorView.findViewById(R.id.p_student_name);
            TextView student_cell = (TextView) anchorView.findViewById(R.id.student_phone);
            edtTextPersonal = (EditText) anchorView.findViewById(R.id.edittext_personal_comment);
            ArrayList<Comentario> cs=(ArrayList)source.listComments(singleton.idActivityStudent, "P", 0);
            if(cs!=null && cs.size()!=0){
                if(cs.get(0).getTxtComment()!=null){
                    edtTextPersonal.setText(cs.get(0).getTxtComment());
                }
            }

            String cellphone;
            if(singleton.portfolioClass.getPerfil().equals("S")){
                student_name.setText(userPerfil.getName());
                cellphone = userPerfil.getCellphone();
                edtTextPersonal.setVisibility(View.GONE);
                TextView lblPrivacy = (TextView) anchorView.findViewById(R.id.lbl_privacy);
                TextView lblTextPersonal = (TextView) anchorView.findViewById(R.id.lbl_personal_comment);
                lblPrivacy.setVisibility(View.GONE);
                lblTextPersonal.setVisibility(View.GONE);

            }else{
                student_name.setText(singleton.portfolioClass.getStudentName());
                cellphone = singleton.portfolioClass.getCellphone();
            }

            if (cellphone != null && !cellphone.equals("null"))
                student_cell.setText(singleton.portfolioClass.getCellphone());
            else
                student_cell.setText("");
        }


    }

    private void displayVersionsDialog(View anchorView) {
        if (anchorView.getVisibility() == View.VISIBLE)
            anchorView.setVisibility(View.GONE);
        else {
            anchorView.setVisibility(View.VISIBLE);
        }
    }

    private void hideNotes(boolean f) {
        try {
            View v = getView();
            if (v != null) {
                ArrayList<Note> aux = new ArrayList<>();
                aux.addAll(specificCommentsNotes.values());

                for (int i = 0; i < aux.size(); i++) {
                    Note first = aux.get(i);
                    Button btnFirst = (Button) v.findViewById(first.getBtId());
                    if (f == true)
                        btnFirst.setVisibility(View.GONE);
                    else
                        btnFirst.setVisibility(View.VISIBLE);
                    resetColorNote(f);
                }
            }
        } catch (NullPointerException e) {
        }
    }

    private void resetColorNote(boolean flag) {
        Spannable textSpanned = mRTMessageField.getText();
        BackgroundColorSpan[] spans = textSpanned.getSpans(0, textSpanned.length(), BackgroundColorSpan.class);

        for (BackgroundColorSpan spm : spans) {
            if (spm.getId() != -1) {
                if (flag)
                    spm.setColor(trasnparent);
                else
                    spm.setColor(greenLight);
            }
        }
        mRTMessageField.invalidate();
        //noteFollowText();
    }

    private void savePersonalComment(String txtPersonal, boolean v) {
        Comentario c = new Comentario();
        c.setTxtComment(txtPersonal);
        c.setIdAuthor(singleton.user.getIdUser());
        c.setDateComment(getActualTime());
        c.setIdActivityStudent(singleton.idActivityStudent);
        c.setTypeComment("P");


        if (v)
            source.insertPersonalComment(c);
        else {
            source.updatePersonalComment(c);
            personalCommentChanged = true;
        }
    }

    /**
     * NOTIFICATIONS
     */
    private void removeOthersNotifications() {
        ArrayList<Integer> idsNotification = source.getNonCommentsNotifications(singleton.idActivityStudent);
        for (Integer id : idsNotification) {
            Sync sync = new Sync(singleton.device.get_id_device(), "tb_notice", id, singleton.idActivityStudent);
            DataBaseAdapter.getInstance(getContext()).insertIntoTBSync(sync);
        }

        source.deleteAllNotifications(idsNotification);
    }

    private void removeGeneralCommentsNotifications() {
        ArrayList<Integer> idsNotification = source.getGeneralCommentsNotifications(singleton.idActivityStudent);
        for (Integer id : idsNotification) {
            Sync sync = new Sync(singleton.device.get_id_device(), "tb_notice", id, singleton.idActivityStudent);
            DataBaseAdapter.getInstance(getContext()).insertIntoTBSync(sync);
        }
        source.deleteAllNotifications(idsNotification);
    }

    private void removeSpecificCommentsNotifications() {
        ArrayList<Integer> idsNotification = source.getSpecificCommentsNotificationsID(singleton.idActivityStudent, singleton.idCurrentVersionActivity);
        for (Integer id : idsNotification) {
            Sync sync = new Sync(singleton.device.get_id_device(), "tb_notice", id, singleton.idActivityStudent);
            DataBaseAdapter.getInstance(getContext()).insertIntoTBSync(sync);
        }
        source.deleteAllNotifications(idsNotification);
    }

    private void verifyNotifications(View view) {
        if (view != null) {
            int generalCommentsNotifications = source.getAllGeneralCommentsNotifications(singleton.idActivityStudent);
            TextView generalNotice = (TextView) view.findViewById(R.id.general_comment_notice);
            if (generalCommentsNotifications != 0) {
                generalNotice.setText(String.valueOf(generalCommentsNotifications));
                generalNotice.setVisibility(View.VISIBLE);
            } else {
                generalNotice.setVisibility(View.GONE);
            }

            ImageView specificCommentNotice = (ImageView) view.findViewById(R.id.specific_comment_notice);
            if (!source.hasSpecificComment(singleton.idActivityStudent))
                specificCommentNotice.setVisibility(View.GONE);
        }
    }

    private class ActionBarCallBack implements ActionMode.Callback {

        int startSelection;
        int endSelection;

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getView().getWindowToken(), 0);
            if (singleton.portfolioClass.getPerfil().equals("S")) {
//                if (singleton.idVersionActivity == singleton.idCurrentVersionActivity) {
//                    createAddSpecificCommentButton(getCaretYPosition(mRTMessageField.getSelectionStart()));
//                    mode.getMenu();
//                } else {
//                    menu.removeItem(android.R.id.paste);
//                    menu.removeItem(android.R.id.cut);
//                }

                if (singleton.idVersionActivity != singleton.idCurrentVersionActivity) {
                    menu.removeItem(android.R.id.paste);
                    menu.removeItem(android.R.id.cut);
                }
            }

            if (singleton.portfolioClass.getPerfil().equals("T")) {
                createAddSpecificCommentButton(getCaretYPosition(mRTMessageField.getSelectionStart()));
                menu.removeItem(android.R.id.paste);
                menu.removeItem(android.R.id.cut);
            }

            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            View v = getView().findViewWithTag("plus");
            if (v != null)
                scrollview.removeView(v);
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        private void createSpecificCommentNote(float yPosition, String selectedText) {
            currentSpecificComment++;
            Log.d("editor ", "current note is now:" + currentSpecificComment);
            float yButton = 0;
            int idButton = -1;

            if (yPosition != 0)
                yButton = yPosition - 2;

            idButton = currentSpecificComment;

            selectedActualText = selectedText;

            specificCommentsNotes.put(idButton, new Note(idButton, selectedText, yButton));
            Button btn = createButton(idButton, String.valueOf(currentSpecificComment), yButton);
            if (btn != null) {
                scrollview.addView(btn);
            }

            mRTManager.onEffectSelected(Effects.BGCOLOR, greenLight, idButton);
            mRTMessageField.setSelected(false);
            mRTMessageField.clearFocus();

            setSpecificCommentNoteValue();

            saveText();

            btn.callOnClick();
        }

        private void createAddSpecificCommentButton(float y) {
            Context context = getContext();
            if (context != null) {
                final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                Button add_button = (Button) inflater.inflate(R.layout.btn_specific_comment, scrollview, false);
                add_button.setY(y);
                add_button.setX(5);
                add_button.setTag("plus");
                add_button.setText("+");

                add_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mRTMessageField.getText().toString().isEmpty()) {
                            startSelection = mRTMessageField.getSelectionStart();
                            endSelection = mRTMessageField.getSelectionEnd();
                            String selectedText = getSelectedText();

                            if (!selectedText.isEmpty()) {
                                if (selectedText.length() > 0) {
                                    if (canCreateButton(startSelection, endSelection)) {
                                        singleton.selectedText = mRTMessageField.getText().toString().substring(startSelection, endSelection);
                                        createSpecificCommentNote(getCaretYPosition(startSelection), selectedText);
                                    }
                                }
                            }
                        }
                    }
                });

                scrollview.addView(add_button);
            }
        }
    }
}