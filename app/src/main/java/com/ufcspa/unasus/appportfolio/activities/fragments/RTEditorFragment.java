package com.ufcspa.unasus.appportfolio.activities.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.view.ViewManager;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.onegravity.rteditor.spans.MediaSpan;
import com.ufcspa.unasus.appportfolio.activities.MainActivity;
import com.ufcspa.unasus.appportfolio.adapter.UserListAdapter;
import com.ufcspa.unasus.appportfolio.adapter.VersionsAdapter;
import com.ufcspa.unasus.appportfolio.database.DataBase;
import com.ufcspa.unasus.appportfolio.model.ActivityStudent;
import com.ufcspa.unasus.appportfolio.model.Comentario;
import com.ufcspa.unasus.appportfolio.model.Note;
import com.ufcspa.unasus.appportfolio.model.Observation;
import com.ufcspa.unasus.appportfolio.model.Singleton;
import com.ufcspa.unasus.appportfolio.model.Sync;
import com.ufcspa.unasus.appportfolio.model.User;
import com.ufcspa.unasus.appportfolio.model.VersionActivity;
import com.ufcspa.unasus.appportfolio.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class RTEditorFragment extends HelperFragment {
    // Constante
    static final int REQUEST_FOLIO_ATTACHMENT = 5;
    User userPerfil; // dados do tutor
    // Editor
    private RTManager mRtManager;
    private RTEditText mRtMessageField;
    private RTToolbar mRtToolbar;
    // Observação
    private int mCurrentSpecificComment;
    private HashMap<Integer, Note> mSpecificCommentsNotes;
    private String mSelectedActualText = "null";
    private boolean mSpecificCommentsOpen;
    // Layout
    private ViewGroup mScrollView;
    private ImageButton mFullScreen;
    private Button mSendVersion;
    private Button mFinalizeActivity;
    private Switch mSwitchNote;
    private RelativeLayout mSlider;
    private ViewGroup mRightBarSpecificComments;
    private View mImportPanel;
    private ImageButton mPersonalCommentButton;
    private ImageButton mVersionsButton;
    private ImageView mUsrPhoto;
    private TextView mSendVersionText;
    private TextView mFinalizeActivityText;
    private TextView mFullscreenText;
    private ListView mEditorUserList;
    private UserListAdapter mUserListAdapter;
    private TextView mNoVersion;
    // model
    private ActivityStudent mAcStudent;
    private DataBase mDataBase;
    private static Singleton sSingleton;
    // Cores
    private int mTrasnparent;
    private int mGreenLight;
    private int mGreenDark;
    // Versoes
    private VersionsAdapter mVersionsAdapter;
    private ArrayList<VersionActivity> mVersionsList;
    private ArrayList<User> mUsersList;
    // Personal Comment
    private boolean mPersonalCommentChanged;
    private String mTxtPersonal;
    private EditText mEdtTextPersonal;

    // Receptor de eventos
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DialogFragment newFragment = new AttachmentDialogFragment();
            newFragment.setTargetFragment(getParentFragment(), REQUEST_FOLIO_ATTACHMENT);
            newFragment.show(getChildFragmentManager(), "Attachment");
        }
    };

    public RTEditorFragment() {
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

                //if (idAttachment != -1)
                //mDataBase.insertAttachActivity(idAttachment, sSingleton.idActivityStudent);
            }
        }
    }

    /**
     * MÉTODO PARA CARREGAR  A ULTIMA VERSÃO DO TEXTO
     */
    public void loadLastText() {
        mAcStudent = mDataBase.listLastVersionActivityStudent(sSingleton.idActivityStudent);
        if (mAcStudent.getTxtActivity().equals("")) {
            mNoVersion.setVisibility(View.VISIBLE);
        }
//        if (sSingleton.firsttime) {
//            mRtMessageField.setRichTextEditing(true, mAcStudent.getTxtActivity());
//            sSingleton.firsttime = false;
//        }
        if (sSingleton.firsttime) {
            String text = mDataBase.getTextFromCurrentVersion(sSingleton.idCurrentVersionActivity);

            HashMap<String, String> aux = mDataBase.getAllAttachmentsNames(sSingleton.idActivityStudent);

            mRtMessageField.setRichTextEditing(true, text, aux);
            sSingleton.firsttime = false;
        }
    }

    /**
     * MÉTODO PARA SALVAR TEXTO NO FORMATO HTML
     */
    public void saveText() {
        Log.d("editor DB", "salvando texto..");
        if (mRtMessageField != null && mRtMessageField.isMediaFactoryRegister() != null) {
            mAcStudent.setTxtActivity(mRtMessageField.getText(RTFormat.HTML));
            if (!sSingleton.portfolioClass.getPerfil().equals("T") && !sSingleton.guestUser) {
                mDataBase.updateActivityStudent(mAcStudent, getActualTime(), mDataBase.getIDVersionAtual(sSingleton.idActivityStudent));
            } else {
                mDataBase.updateActivityStudent(mAcStudent, getActualTime(), sSingleton.idCurrentVersionActivity);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rteditor, null);

        mNoVersion = (TextView) view.findViewById(R.id.no_version_tv);

        sSingleton = Singleton.getInstance();
        mDataBase = DataBase.getInstance(getActivity());
        if (sSingleton.portfolioClass.getPerfil().equals("S")) {  // SE È ALUNO, MOSTRAR INFORMAÇÔES DO TUTOR
            userPerfil = mDataBase.getTutorPerfil(sSingleton.idActivityStudent);
            Log.d("tutorPerfil", "tutor data:" + userPerfil.toString());
//            sSingleton.idCurrentVersionActivity = mDataBase.getIDVersionAtual(sSingleton.idActivityStudent);
        }

        mSpecificCommentsOpen = false;

        mTrasnparent = getResources().getColor(android.R.color.transparent);
        mGreenLight = getResources().getColor(R.color.base_green_light);
        mGreenDark = getResources().getColor(R.color.base_green);

        getIdNotesFromDB();

        mScrollView = (ViewGroup) view.findViewById(R.id.comments);
        mRightBarSpecificComments = (ViewGroup) view.findViewById(R.id.obs_view);

        if (sSingleton.portfolioClass.getPerfil().equals("T") || sSingleton.guestUser || (sSingleton.idCurrentVersionActivity != mDataBase.getIDVersionAtual(sSingleton.idActivityStudent)) || !mDataBase.getActivityStudentById(sSingleton.idActivityStudent).getDt_conclusion().equals("null")) {
            view.findViewById(R.id.rte_toolbar_container).setVisibility(View.INVISIBLE);
        } else if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST);
            view.findViewById(R.id.rte_toolbar_container).setVisibility(View.VISIBLE);
        }

        createEditor(view, savedInstanceState);

        mFullScreen = (ImageButton) view.findViewById(R.id.fullscreen);
        mFullscreenText = (TextView) view.findViewById(R.id.fullscreen_text);
        if (!sSingleton.isFullscreen) {
            mFullscreenText.setVisibility(View.VISIBLE);
            mFullScreen.setBackgroundResource(R.drawable.ic_maximize);
        } else {
            mFullscreenText.setVisibility(View.INVISIBLE);
            mFullScreen.setBackgroundResource(R.drawable.ic_minimize);
        }
        mFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("rteditor", mRtMessageField.getText(RTFormat.HTML));
                saveText();

                if (sSingleton.isFullscreen) {
                    sSingleton.wasFullscreen = true;
                    sSingleton.isFullscreen = false;
                } else {
                    sSingleton.wasFullscreen = false;
                    sSingleton.isFullscreen = true;
                }

                ((MainActivity) getActivity()).dontCreateCrossfader();
            }
        });

        mSendVersion = (Button) view.findViewById(R.id.send_version);
        mSendVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (sSingleton.portfolioClass.getPerfil().equals("S") && (sSingleton.idCurrentVersionActivity == sSingleton.idVersionActivity)) {
                if ((sSingleton.idCurrentVersionActivity == sSingleton.idVersionActivity) && mDataBase.getActivityStudentById(sSingleton.idActivityStudent).getDt_conclusion().equals("null")) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Enviar Versão")
                            .setMessage("Você tem certeza que deseja enviar essa versão da atividade?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (mRtMessageField.getText().toString().trim().length() != 0) {
                                        Log.d("RTEditor", "Enviando versão!");
                                        Toast.makeText(getContext(), "Enviando versão!", Toast.LENGTH_SHORT).show();
                                        saveText();

                                        //ANEXOS QUE DEVEM SER ENVIADOS
                                        handleSendAttachments();

                                        //SALVA NOVA VERSION ACTIVITY
                                        DataBase data = DataBase.getInstance(getContext());
                                        VersionActivity version = new VersionActivity();
                                        version.setTx_activity(mRtMessageField.getText(RTFormat.HTML));
                                        version.setId_activity_student(Singleton.getInstance().idActivityStudent);
                                        version.setDt_last_access(getActualTime());
                                        version.setDt_submission(null);
                                        //version.setId_version_activit_srv(version.getId_version_activity());
                                        int id = data.insertVersionActivity(version);

//                                        sSingleton.idVersionActivity = id;
                                        sSingleton.idCurrentVersionActivity = mDataBase.getIDVersionAtual(sSingleton.idActivityStudent);

                                        //POPULA SYNC PARA SINCRONIZAR
                                        Sync sync = new Sync();
                                        sync.setNm_table("tb_version_activity");
                                        sync.setCo_id_table(id);
                                        sync.setId_activity_student(Singleton.getInstance().idActivityStudent);
                                        sync.setId_device(sSingleton.device.get_id_device());
                                        mDataBase.insertIntoTBSync(sync);

                                        data.updateVersionsBySendFullData(0, version.getDt_submission(), id);

                                        MainActivity main = ((MainActivity) getActivity());
                                        if (main != null) {
                                            try {
                                                main.sendFullData();
                                            } finally {
                                                Toast.makeText(getContext(), "Versão Enviada", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        if (!sSingleton.isFullscreen) {
                                            sSingleton.wasFullscreen = true;
                                            sSingleton.isFullscreen = false;
                                        } else {
                                            sSingleton.wasFullscreen = false;
                                            sSingleton.isFullscreen = true;
                                        }

                                        ((MainActivity) getActivity()).dontCreateCrossfader();
                                    } else {
                                        mRtMessageField.setError("Você não pode enviar uma versão sem texto.");
                                    }
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

        mFinalizeActivity = (Button) view.findViewById(R.id.finalize_activity);
        mFinalizeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataBase.getActivityStudentById(sSingleton.idActivityStudent).getDt_conclusion().equals("null")) {
                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.dialog_logout);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    TextView title = (TextView) dialog.findViewById(R.id.tv_title);
                    Button accept = (Button) dialog.findViewById(R.id.btn_logout_ok);
                    Button cancel = (Button) dialog.findViewById(R.id.btn_logout_cancel);

                    title.setText("Tem certeza que deseja\nfinalizar a atividade?");
                    accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            com.ufcspa.unasus.appportfolio.model.basicData.ActivityStudent activityStudent;
                            activityStudent = mDataBase.getActivityStudentById(Singleton.getInstance().idActivityStudent);
                            activityStudent.setDt_conclusion(getActualTime());
                            mDataBase.updateTBActivityStudent(activityStudent);

                            Sync sync = new Sync();
                            sync.setNm_table("tb_activity_student");
                            sync.setCo_id_table(Singleton.getInstance().idActivityStudent);
                            sync.setId_activity_student(Singleton.getInstance().idActivityStudent);
                            sync.setId_device(sSingleton.device.get_id_device());
                            mDataBase.insertIntoTBSync(sync);
                            MainActivity main = ((MainActivity) getActivity());
                            try {
                                main.sendFullData();
                            } finally {
                                Toast.makeText(getContext(), "Atividade finalizada", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent("call.fragments.action").putExtra("ID", 1));
                            }
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });

        mSwitchNote = (Switch) view.findViewById(R.id.switch_note);
        mSwitchNote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hideNotes(isChecked);
                if (isChecked)
                    Toast.makeText(getContext(), "Observações ocultas", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "Observações visíveis", Toast.LENGTH_SHORT).show();
            }
        });

        mSendVersionText = (TextView) view.findViewById(R.id.send_version_text);
        mFinalizeActivityText = (TextView) view.findViewById(R.id.finalize_activity_text);
        if (sSingleton.portfolioClass.getPerfil().equals("T") || (sSingleton.idCurrentVersionActivity != sSingleton.idVersionActivity) || !mDataBase.getActivityStudentById(sSingleton.idActivityStudent).getDt_conclusion().equals("null")) {
            mSendVersion.setVisibility(View.GONE);
            mSendVersionText.setVisibility(View.GONE);
            mFinalizeActivity.setVisibility(View.GONE);
            mFinalizeActivityText.setVisibility(View.GONE);
        }

        initCommentsTab(view);
        initTopBar(view);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver, new IntentFilter("call.attachmentdialog.action"));

        if (mDataBase.getIDVersionAtual(sSingleton.idActivityStudent) == 0 && sSingleton.portfolioClass.getPerfil().equals("S")) {
            VersionActivity version = new VersionActivity();
            version.setTx_activity("");
            version.setId_activity_student(Singleton.getInstance().idActivityStudent);
            version.setDt_last_access(getActualTime());
            version.setDt_submission("0000-00-00 00:00:00");

            int id = mDataBase.insertVersionActivity(version);
            mDataBase.updateVersionsBySendFullData(0, version.getDt_submission(), id);

            sSingleton.idVersionActivity = id;
            sSingleton.idCurrentVersionActivity = id;

            //POPULA SYNC PARA SINCRONIZAR
            Sync sync = new Sync();
            sync.setNm_table("tb_version_activity");
            sync.setCo_id_table(mDataBase.getIDVersionAtual(sSingleton.idActivityStudent));
            sync.setId_activity_student(Singleton.getInstance().idActivityStudent);
            sync.setId_device(sSingleton.device.get_id_device());
            mDataBase.insertIntoTBSync(sync);

            if (isOnline()) {
                MainActivity main = ((MainActivity) getActivity());
                main.sendFullData();
            }
        }

        verifyNotifications(view);
        removeOthersNotifications();

        if (!sSingleton.guestUserComments) {
            ( (ViewManager) mSlider.getParent()).removeView(mSlider);
            mSwitchNote.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getView().findViewById(R.id.rte_toolbar_container).setVisibility(View.VISIBLE);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void handleSendAttachments() {
        Spannable textSpanned = mRtMessageField.getText();
        MediaSpan[] spans = textSpanned.getSpans(0, textSpanned.length(), MediaSpan.class);
        for (MediaSpan s : spans) {
            int idAttachment = mDataBase.getAttachmentByPath(s.getMedia().getFilePath(RTFormat.PLAIN_TEXT));
            if (idAttachment != -1) {
                int idAttachActivity = mDataBase.insertAttachActivity(idAttachment, sSingleton.idActivityStudent);

                //POPULA SYNC PARA SINCRONIZAR
                Sync sync = new Sync();
                sync.setNm_table("tb_attach_activity");
                sync.setCo_id_table(idAttachActivity);
                sync.setId_activity_student(sSingleton.idActivityStudent);
                sync.setId_device(sSingleton.device.get_id_device());
                mDataBase.insertIntoTBSync(sync);
            }
        }
    }

    public void createEditor(View view, Bundle savedInstanceState) {
        // create RTManager
        RTApi rtApi = new RTApi(getActivity(), new RTProxyImpl(getActivity()), new RTMediaFactoryImpl(getActivity(), true));

        mRtManager = new RTManager(rtApi, savedInstanceState, getContext());

        ViewGroup toolbarContainer = (ViewGroup) view.findViewById(R.id.rte_toolbar_container);

        // register toolbar (if it exists)
        mRtToolbar = (RTToolbar) view.findViewById(R.id.rte_toolbar);
        if (mRtToolbar != null) {
            mRtManager.registerToolbar(toolbarContainer, mRtToolbar);
        }

        // register message editor
        mRtMessageField = (RTEditText) view.findViewById(R.id.rtEditText);
        mRtManager.registerEditor(mRtMessageField, true);

        mRtMessageField.setCustomSelectionActionModeCallback(new ActionBarCallBack());
        loadLastText();

        mRtMessageField.addTextChangedListener(new TextWatcher() {
            float posStart;
            float posEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                posStart = getCaretYPosition(mRtMessageField.getSelectionStart());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                posEnd = getCaretYPosition(mRtMessageField.getSelectionEnd());
                changeNotePosition();
                setSpecificCommentNoteValue();
            }
        });

        mRtMessageField.post(new Runnable() {
            @Override
            public void run() {
                noteFollowText();
                setSpecificCommentNoteValue();
            }
        });

        mRtMessageField.setLineSpacing(15, 1);

        mRtMessageField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.getId() == R.id.rtEditText && hasFocus) {
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    View view = getView();
                    if (view != null) {
                        SlidingPaneLayout layout = (SlidingPaneLayout) view.findViewById(R.id.rteditor_fragment);
                        layout.openPane();

                        int id = sSingleton.note.getBtId();
                        if (mSpecificCommentsOpen && id > 0) {
                            ArrayList<Comentario> lista = (ArrayList<Comentario>) mDataBase.listObsComments(sSingleton.idActivityStudent, id);
                            if (lista == null || lista.size() == 0) {
                                //Remove note from hash map
                                mSpecificCommentsNotes.remove(id);
                                mCurrentSpecificComment--;

                                //Remove background color from text
                                Spannable textSpanned = mRtMessageField.getText();
                                BackgroundColorSpan[] spans = textSpanned.getSpans(0, textSpanned.length(), BackgroundColorSpan.class);

                                for (BackgroundColorSpan s : spans) {
                                    if (s.getId() == id) {
                                        textSpanned.removeSpan(s);
                                        break;
                                    }
                                }

                                //Remove button from right drawer
                                Button btn = (Button) mScrollView.findViewById(id);
                                sSingleton.note = new Note(0, "null", 0);

                                int childs = mRightBarSpecificComments.getChildCount();
                                for (int i = childs - 1; i >= 0; i--)
                                    mRightBarSpecificComments.removeViewAt(i);

                                final float scale = getResources().getDisplayMetrics().density;
                                int btnBegin = (int) (40 * scale + 0.5f);
                                if (btn.getText().equals("..."))
                                    createMultiButtonsRightTabBar((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE), btn, btnBegin);

                                showCommentsTab(false);

                                //Remove button from rteditor
                                mScrollView.removeView(btn);
                                setSpecificCommentNoteValue();
                            }
                        }
                    }
                }
            }
        });

        mRtMessageField.setCanPaste(true);
        mRtManager.setToolbarVisibility(RTManager.ToolbarVisibility.SHOW);
        if (sSingleton.portfolioClass.getPerfil().equals("T") || sSingleton.guestUser || (sSingleton.idCurrentVersionActivity != sSingleton.idVersionActivity) || !mDataBase.getActivityStudentById(sSingleton.idActivityStudent).getDt_conclusion().equals("null")) {
            mRtMessageField.setKeyListener(null);
            mRtMessageField.setTextIsSelectable(true);
            mRtManager.setToolbarVisibility(RTManager.ToolbarVisibility.HIDE);
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
        showCommentsTab(false);
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mRtManager != null) {
            mRtManager.onSaveInstanceState(outState);
            saveText();
        }
        outState.putInt("mCurrentSpecificComment", mCurrentSpecificComment);
        outState.putSerializable("mSpecificCommentsNotes", mSpecificCommentsNotes);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.getSerializable("mSpecificCommentsNotes") != null) {
                mSpecificCommentsNotes = (HashMap<Integer, Note>) savedInstanceState.getSerializable("mSpecificCommentsNotes");
            }
            mCurrentSpecificComment = savedInstanceState.getInt("mCurrentSpecificComment", -1);
        }

        sSingleton.firsttime = true;
        loadLastText();
    }

    @Override
    public void onDestroyView() {
        saveText();
        if (mPersonalCommentChanged) {
            int idPersonalComment = mDataBase.getPersonalComment(sSingleton.idActivityStudent);
            String idDevice = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
            Sync sync = new Sync(idDevice, "tb_comment", idPersonalComment, sSingleton.idActivityStudent);
            DataBase.getInstance(getContext()).insertIntoTBSync(sync);
            mPersonalCommentChanged = false;
        }
        if (mRtManager != null) {
            mRtManager.onDestroy(true);
        }
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        super.onStop();
        Fragment frag = getActivity().getSupportFragmentManager().findFragmentById(R.id.comments_container);
        if (frag != null) {
            try {
                getActivity().getSupportFragmentManager().beginTransaction().remove(frag).commitAllowingStateLoss();
            } catch (IllegalStateException e) {
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

        mSlider = (RelativeLayout) view.findViewById(R.id.slider);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? dm.widthPixels / 4 : dm.widthPixels / 3;

        SlidingPaneLayout.LayoutParams relativeParams = new SlidingPaneLayout.LayoutParams(new SlidingPaneLayout.LayoutParams(SlidingPaneLayout.LayoutParams.MATCH_PARENT, SlidingPaneLayout.LayoutParams.MATCH_PARENT));
        relativeParams.setMargins(width, 0, 0, 0);
        mSlider.setLayoutParams(relativeParams);

        mSlider.requestLayout();
        mSlider.bringToFront();

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

//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.comments_container, new CommentsFragment()).addToBackStack("HelperFragment").commit();

        final SlidingPaneLayout layout = (SlidingPaneLayout) view.findViewById(R.id.rteditor_fragment);

        layout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                mImportPanel.setVisibility(View.GONE);
                if (mEdtTextPersonal != null) {
                    if (mDataBase.getPersonalComment(sSingleton.idActivityStudent) <= 0)// se nao possui nenhum comentario pessoal, cria um novo
                        savePersonalComment(mEdtTextPersonal.getText().toString(), true);
                    else
                        savePersonalComment(mEdtTextPersonal.getText().toString(), false); // senao atualiza o antigo
                    mEdtTextPersonal = null;
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

                    int childs = mRightBarSpecificComments.getChildCount();
                    for (int i = childs - 1; i >= 0; i--)
                        mRightBarSpecificComments.getChildAt(i).setVisibility(View.GONE);
                    mSlider.findViewById(R.id.rightbar_green).setVisibility(View.INVISIBLE);

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
        mUsrPhoto = (ImageView) view.findViewById(R.id.usr_photo);
        mPersonalCommentButton = (ImageButton) view.findViewById(R.id.personal_comment);
        mVersionsButton = (ImageButton) view.findViewById(R.id.btn_versions);

        mEditorUserList = (ListView) view.findViewById(R.id.editor_user_list);

        mUsersList = mDataBase.getActivityUsers(sSingleton.idActivityStudent, sSingleton.user.getIdUser());
        mUserListAdapter = new UserListAdapter(getActivity(), mUsersList);
        mEditorUserList.setAdapter(mUserListAdapter);
        mUserListAdapter.notifyDataSetChanged();

        onClickTopBar(true);

        mImportPanel = view.findViewById(R.id.versions_container);
        mVersionsList = mDataBase.getAllVersionsFromActivityStudent(sSingleton.idActivityStudent);

        GridView versionList = (GridView) mImportPanel.findViewById(R.id.version_list);
        mVersionsAdapter = new VersionsAdapter(getActivity(), mVersionsList);
        versionList.setAdapter(mVersionsAdapter);
        versionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position <= mVersionsList.size() - 1) {
                    saveText();

                    sSingleton.idCurrentVersionActivity = mVersionsList.get(position).getId_version_activity();
                    sSingleton.firsttime = true;

                    removeSpecificCommentsNotifications();

                    if (!sSingleton.isFullscreen) {
                        sSingleton.wasFullscreen = true;
                        sSingleton.isFullscreen = false;
                    } else {
                        sSingleton.wasFullscreen = false;
                        sSingleton.isFullscreen = true;
                    }

                    ((MainActivity) getActivity()).dontCreateCrossfader();
                }
            }
        });
        if (sSingleton.portfolioClass.getPerfil().equals("S")) {
            studentName.setText(userPerfil.getName());
            Bitmap photo = userPerfil.getPhotoBitmap();
            if (photo != null)
                mUsrPhoto.setImageBitmap(ConfigFragment.getRoundedRectBitmap(photo, 100));
            activityName.setText("Ativ. " + sSingleton.activity.getNuOrder() + ": " + sSingleton.activity.getTitle());
        } else {
            studentName.setText(sSingleton.portfolioClass.getStudentName());
            Bitmap photo = sSingleton.portfolioClass.getPhoto();
            if (photo != null)
                mUsrPhoto.setImageBitmap(ConfigFragment.getRoundedRectBitmap(photo, 100));
            activityName.setText("Ativ. " + sSingleton.activity.getNuOrder() + ": " + sSingleton.activity.getTitle());
        }
    }

    private void onClickTopBar(boolean shouldClick) {
        if (shouldClick) {
            if (!sSingleton.portfolioClass.getPerfil().equals("Z")) {
                mPersonalCommentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        mImportPanel.setVisibility(View.GONE);
//                        displayPersonalComment(getView().findViewById(R.id.personal_comment_container));
                        String[] word = mRtMessageField.getText(RTFormat.SPANNED).split("( )|(\n)");
                        ArrayList<String> wordCount = new ArrayList<String>();
                        for (int i = 0; i < word.length; i++) {
                            if (!word[i].equals("")) {
                                wordCount.add(word[i]);
                            }
                        }

                        final Dialog dialog = new Dialog(getActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_info);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                        TextView dispositivo = (TextView) dialog.findViewById(R.id.dispositivo);
                        TextView versionApp1 = (TextView) dialog.findViewById(R.id.version_app1);
                        TextView versionOs1 = (TextView) dialog.findViewById(R.id.version_os1);

                        TextView deviceId = (TextView) dialog.findViewById(R.id.device_id);
                        TextView versionApp = (TextView) dialog.findViewById(R.id.version_app);
                        TextView versionOs = (TextView) dialog.findViewById(R.id.version_os);

                        dispositivo.setText("Caracteres:");
                        versionApp1.setText("Palavras:");
                        versionOs1.setVisibility(View.INVISIBLE);
                        versionOs.setVisibility(View.INVISIBLE);

                        dialog.show();

                        deviceId.setText("" + mRtMessageField.length());
                        versionApp.setText("" + wordCount.size());

                        Button btnOk = (Button) dialog.findViewById(R.id.btn_info_ok);
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                });
                mUsrPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mImportPanel.setVisibility(View.GONE);
                        displayPersonalComment(getView().findViewById(R.id.personal_comment_container));
                    }
                });
            } else {
                //mPersonalCommentButton.setVisibility(View.INVISIBLE);

            }

            mVersionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mVersionsAdapter.refresh(mDataBase.getAllVersionsFromActivityStudent(sSingleton.idActivityStudent));

                    if (mEdtTextPersonal != null) {
                        savePersonalComment(mEdtTextPersonal.getText().toString(), !mEdtTextPersonal.getText().toString().isEmpty());
                        mEdtTextPersonal = null;
                    }

                    getView().findViewById(R.id.personal_comment_container).setVisibility(View.GONE);
                    displayVersionsDialog(mImportPanel);
                }
            });
        } else {
            mPersonalCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            mUsrPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            mVersionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }

    private float getCaretYPosition(int position) {
        Layout layout = mRtMessageField.getLayout();
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
        mSpecificCommentsNotes = new HashMap<>();
        ArrayList<Observation> obs = (ArrayList<Observation>) mDataBase.getObservation(mDataBase.getIDVersionSrvByLocalID(sSingleton.idCurrentVersionActivity)); //mDataBase.listSpecificComments(sSingleton.idActivityStudent);
        for (int i = 0; i < obs.size(); i++) {
            Observation o = obs.get(i);
            mSpecificCommentsNotes.put(o.getNu_comment_activity(), new Note(o.getNu_comment_activity(), o.getTx_reference(), 0));
        }
        mCurrentSpecificComment = (mDataBase.listSpecificComments(sSingleton.idActivityStudent)).size(); //mSpecificCommentsNotes.size();
        Log.d("editor notes", "mCurrentSpecificComment:" + mCurrentSpecificComment);
    }

    private Button createButton(final int id, final String value, final float yPosition) {
        Context context = getContext();
        if (context != null && sSingleton.guestUserComments) {
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Button note = (Button) inflater.inflate(R.layout.partial_specific_comment, mScrollView, false);

            note.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeColor(id);

                    Button btn = (Button) v;
                    btn.setBackgroundResource(R.drawable.bg_rounded_corner);
                    btn.setTextColor(Color.WHITE);

                    ArrayList<Note> arrayAux = new ArrayList<>(mSpecificCommentsNotes.values());

                    for (int i = 0; i < arrayAux.size(); i++) {
                        Button aux = (Button) getView().findViewById(arrayAux.get(i).getBtId());
                        if (aux.getId() != btn.getId()) {
                            aux.setBackgroundResource(R.drawable.btn_border);
                            aux.setTextColor(mGreenDark);
                        }
                    }

                    sSingleton.note.setBtY(yPosition);
                    if (mDataBase.selectListClassAndUserType(Singleton.getInstance().user.getIdUser()).get(mDataBase.selectListClassAndUserType(Singleton.getInstance().user.getIdUser()).size() - 1).getPerfil().equalsIgnoreCase("T")) {
                        sSingleton.note.setSelectedText(mSelectedActualText);
                    } else {
                        sSingleton.note.setSelectedText(mDataBase.getIdObservationTextByNuCommentActivy(sSingleton.idActivityStudent, sSingleton.note.getBtId()));
                    }
                    sSingleton.note.setBtId(id);

                    getView().findViewById(R.id.general_comment_notice).setVisibility(View.GONE);
                    showCommentsTab(true);

                    final float scale = getResources().getDisplayMetrics().density;
                    int btnBegin = (int) (40 * scale + 0.5f);

                    int childs = mRightBarSpecificComments.getChildCount();
                    for (int i = childs - 1; i >= 0; i--)
                        mRightBarSpecificComments.removeViewAt(i);

                    if (btn.getText().equals("..."))
                        createMultiButtonsRightTabBar(inflater, btn, btnBegin);
                    else {
                        Button btnView = (Button) inflater.inflate(R.layout.partial_specific_comment, mRightBarSpecificComments, false);
                        btnView.setText(btn.getText());
                        btnView.setY(btnBegin);
                        btnView.setBackgroundResource(R.drawable.bg_rounded_corner);
                        btnView.setTextColor(Color.WHITE);
                        btnView.setId(id);
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btnView.getLayoutParams();
                        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        btnView.setLayoutParams(params);
                        mRightBarSpecificComments.addView(btnView);
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
        for (final Note n : mSpecificCommentsNotes.values()) {
            if (n.getBtY() == current.getY()) {
                Button btnView = (Button) inflater.inflate(R.layout.partial_specific_comment, mRightBarSpecificComments, false);

                btnView.setText(String.valueOf(n.getBtId()));
                if (n.getBtId() == current.getId()) {
                    btnView.setBackgroundResource(R.drawable.bg_rounded_corner);
                    btnView.setTextColor(Color.WHITE);
                } else {
                    btnView.setBackgroundResource(R.drawable.btn_border);
                    btnView.setTextColor(mGreenDark);
                }
                btnView.setY(i);
                btnView.setTag(n.getBtId());
                btnView.setId(n.getBtId());

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btnView.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                btnView.setLayoutParams(params);

                btnView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeColor((int) v.getTag());

                        Button btn = (Button) v;
                        btn.setBackgroundResource(R.drawable.bg_rounded_corner);
                        btn.setTextColor(Color.WHITE);

                        ArrayList<Note> arrayAux = new ArrayList<>(mSpecificCommentsNotes.values());

                        for (int i = 0; i < arrayAux.size(); i++) {
                            Button aux = (Button) getView().findViewWithTag(arrayAux.get(i).getBtId());
                            if (aux != null && aux.getTag() != btn.getTag()) {
                                aux.setBackgroundResource(R.drawable.btn_border);
                                aux.setTextColor(mGreenDark);
                            }
                        }
                        sSingleton.note.setBtY(n.getBtY());
                        sSingleton.note.setSelectedText(mSelectedActualText);
                        sSingleton.note.setBtId((int) btn.getTag());

                        getView().findViewById(R.id.general_comment_notice).setVisibility(View.GONE);
                        showCommentsTab(true);
                    }
                });

                mRightBarSpecificComments.addView(btnView);

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
            aux.addAll(mSpecificCommentsNotes.values());

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
        int selStart = mRtMessageField.getSelectionStart();
        int selEnd = mRtMessageField.getSelectionEnd();

        sSingleton.actualObservation.setNu_size(selEnd - selStart);
        sSingleton.actualObservation.setNu_initial_position(selStart);

        Spannable text = (Spannable) mRtMessageField.getText().subSequence(selStart, selEnd);
        RTHtml<RTImage, RTAudio, RTVideo> rtHtml = new ConverterSpannedToHtml().convert(text, RTFormat.HTML);
        String thatsMySelectionInHTML = rtHtml.getText();
        return thatsMySelectionInHTML;
    }

    /**
     * MÉTODO PARA ALTERAR A COR DO TEXTO PELA ID DE UMA NOTA
     */
    private void changeColor(int id) {
        Spannable textSpanned = mRtMessageField.getText();
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
                if (spm.getId() != -1 && spm.getBackgroundColor() != mGreenLight)
                    spm.setColor(mGreenLight);
            }
        }

        if (aux != null) {
            textSpanned.removeSpan(aux);
            aux.setColor(mGreenDark);
            textSpanned.setSpan(aux, auxStart, auxEnd, 1);
        }

        mRtMessageField.invalidate();
    }

    public void showCommentsTab(Boolean isSpecificComment) {
        if (sSingleton.guestUserComments) {
            mSpecificCommentsOpen = isSpecificComment;
            if (isSpecificComment) {
                if (sSingleton.note.getBtId() != 0)
                    mSlider.findViewById(R.id.rightbar_green).setVisibility(View.VISIBLE);
                int childs = mRightBarSpecificComments.getChildCount();
                for (int i = childs - 1; i >= 0; i--)
                    mRightBarSpecificComments.getChildAt(i).setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.comments_container, new SpecificCommentsFragment(), "S").commit();
                SlidingPaneLayout layout = (SlidingPaneLayout) getView().findViewById(R.id.rteditor_fragment);
                layout.closePane();
            } else {
                removeGeneralCommentsNotifications();
//                getView().findViewById(R.id.general_comment_notice).setVisibility(View.GONE);

                int childs = mRightBarSpecificComments.getChildCount();
                for (int i = childs - 1; i >= 0; i--)
                    mRightBarSpecificComments.getChildAt(i).setVisibility(View.GONE);
                mSlider.findViewById(R.id.rightbar_green).setVisibility(View.INVISIBLE);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.comments_container, new CommentsFragment(), "G").commit();
            }
        }
    }

    private void changeNotePosition() {
        try {
            if (mSpecificCommentsNotes != null && mSpecificCommentsNotes.size() != 0 && !mSwitchNote.isChecked()) {
                Spannable textSpanned = mRtMessageField.getText();
                BackgroundColorSpan[] spans = textSpanned.getSpans(0, textSpanned.length(), BackgroundColorSpan.class);

                for (BackgroundColorSpan spm : spans) {
                    if (spm.getId() != -1) {
                        Note aux = mSpecificCommentsNotes.get(spm.getId());
                        float bcsPosition = getCaretYPosition(textSpanned.getSpanStart(spm));
                        if (aux != null && bcsPosition != aux.getBtY()) {
                            aux.setBtY(bcsPosition);
                            Button btn = (Button) mScrollView.findViewById(aux.getBtId());
                            btn.setY(bcsPosition);
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
        }
    }

    private void noteFollowText() {
        Spannable textSpanned = mRtMessageField.getText();
        BackgroundColorSpan[] spans = textSpanned.getSpans(0, textSpanned.length(), BackgroundColorSpan.class);

        for (BackgroundColorSpan spm : spans) {
            if (spm.getId() != -1) {
                Note aux = mSpecificCommentsNotes.get(spm.getId());
                if (aux != null)
                    aux.setBtY(getCaretYPosition(textSpanned.getSpanStart(spm)));
                else
                    textSpanned.removeSpan(spm);
            }
        }

        if (mRtManager.getActiveEditor() != null) {
            RTHtml<RTImage, RTAudio, RTVideo> rtHtml = new ConverterSpannedToHtml().convert(textSpanned, RTFormat.HTML);
            mRtMessageField.setRichTextEditing(true, rtHtml.getText());
        }

        ArrayList<Note> arrayAux = new ArrayList<>(mSpecificCommentsNotes.values());

        for (int i = 0; i < arrayAux.size(); i++) {
            Note aux = arrayAux.get(i);
            Button btn = createButton(aux.getBtId(), String.valueOf(aux.getBtId()), aux.getBtY());
            if (btn != null) {
                mScrollView.addView(btn);
            }
        }
    }

    private void putAttachment(String url) {
        int selStart = mRtMessageField.getSelectionStart();
        Spannable textBefore = (Spannable) mRtMessageField.getText().subSequence(0, selStart);
        Spannable textAfter = (Spannable) mRtMessageField.getText().subSequence(selStart, mRtMessageField.length());
        RTHtml<RTImage, RTAudio, RTVideo> rtHtmlBefore = new ConverterSpannedToHtml().convert(textBefore, RTFormat.HTML);
        RTHtml<RTImage, RTAudio, RTVideo> rtHtmlAfter = new ConverterSpannedToHtml().convert(textAfter, RTFormat.HTML);

        mRtMessageField.setRichTextEditing(true, rtHtmlBefore.getText() + "<img src=\"" + url + "\">" + rtHtmlAfter.getText());
    }

    private boolean canCreateButton(int start, int end) {
        boolean canCreate = true;
        Spannable textSpanned = mRtMessageField.getText();
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
            TextView studentName = (TextView) anchorView.findViewById(R.id.p_student_name);
            TextView studentCell = (TextView) anchorView.findViewById(R.id.student_phone);
            mEdtTextPersonal = (EditText) anchorView.findViewById(R.id.edittext_personal_comment);
            ArrayList<Comentario> cs = (ArrayList) mDataBase.listGComments(sSingleton.idActivityStudent, "P");
            if (cs != null && cs.size() != 0) {
                if (cs.get(0).getTxtComment() != null) {
                    mEdtTextPersonal.setText(cs.get(0).getTxtComment());
                }
            }

            String cellphone;
            if (sSingleton.portfolioClass.getPerfil().equals("S")) {
                studentName.setText(userPerfil.getName());
                cellphone = userPerfil.getCellphone();
                mEdtTextPersonal.setVisibility(View.GONE);
                TextView lblPrivacy = (TextView) anchorView.findViewById(R.id.lbl_privacy);
                TextView lblTextPersonal = (TextView) anchorView.findViewById(R.id.lbl_personal_comment);
                lblPrivacy.setVisibility(View.GONE);
                lblTextPersonal.setVisibility(View.GONE);
                anchorView.findViewById(R.id.circle).setVisibility(View.GONE);
                RelativeLayout l = (RelativeLayout)  anchorView.findViewById(R.id.layout_personal_dialog);



            } else {
                studentName.setText(sSingleton.portfolioClass.getStudentName());
                cellphone = sSingleton.portfolioClass.getCellphone();
                if (cellphone != null && !cellphone.equals("null") && sSingleton.portfolioClass.getPerfil().equals("S") )
                    studentCell.setText(sSingleton.portfolioClass.getCellphone());
                else
                    studentCell.setText("");
            }


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
                aux.addAll(mSpecificCommentsNotes.values());

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
        Spannable textSpanned = mRtMessageField.getText();
        BackgroundColorSpan[] spans = textSpanned.getSpans(0, textSpanned.length(), BackgroundColorSpan.class);

        for (BackgroundColorSpan spm : spans) {
            if (spm.getId() != -1) {
                if (flag)
                    spm.setColor(mTrasnparent);
                else
                    spm.setColor(mGreenLight);
            }
        }
        mRtMessageField.invalidate();
        //noteFollowText();
    }

    private void savePersonalComment(String txtPersonal, boolean v) {
        Comentario c = new Comentario();
        c.setTxtComment(txtPersonal);
        c.setIdAuthor(sSingleton.user.getIdUser());
        c.setDateComment(getActualTime());
        c.setIdActivityStudent(sSingleton.idActivityStudent);
        c.setTypeComment("P");


        if (v)
            mDataBase.insertPersonalComment(c);
        else {
            mDataBase.updatePersonalComment(c);
            mPersonalCommentChanged = true;
        }
    }

    /**
     * NOTIFICATIONS
     */
    private void removeOthersNotifications() {
        ArrayList<Integer> idsNotification = mDataBase.getNonCommentsNotifications(sSingleton.idActivityStudent);
        for (Integer id : idsNotification) {
            Sync sync = new Sync(sSingleton.device.get_id_device(), "tb_notice", id, sSingleton.idActivityStudent);
            DataBase.getInstance(getContext()).insertIntoTBSync(sync);
        }

        mDataBase.deleteAllNotifications(idsNotification);
    }

    private void removeGeneralCommentsNotifications() {
        ArrayList<Integer> idsNotification = mDataBase.getGeneralCommentsNotifications(sSingleton.idActivityStudent);
        for (Integer id : idsNotification) {
            Sync sync = new Sync(sSingleton.device.get_id_device(), "tb_notice", id, sSingleton.idActivityStudent);
            DataBase.getInstance(getContext()).insertIntoTBSync(sync);
        }
        mDataBase.deleteAllNotifications(idsNotification);
    }

    private void removeSpecificCommentsNotifications() {
        ArrayList<Integer> idsNotification = mDataBase.getSpecificCommentsNotificationsID(sSingleton.idActivityStudent, mDataBase.getIdVersionSrvFromIdVersion(sSingleton.idCurrentVersionActivity));
        for (Integer id : idsNotification) {
            Sync sync = new Sync(sSingleton.device.get_id_device(), "tb_notice", id, sSingleton.idActivityStudent);
            DataBase.getInstance(getContext()).insertIntoTBSync(sync);
        }
        mDataBase.deleteAllNotifications(idsNotification);
    }

    private void verifyNotifications(View view) {
        if (view != null) {
            int generalCommentsNotifications = mDataBase.getAllGeneralCommentsNotifications(sSingleton.idActivityStudent);
            TextView generalNotice = (TextView) view.findViewById(R.id.general_comment_notice);
            if (generalCommentsNotifications != 0) {
                generalNotice.setText(String.valueOf(generalCommentsNotifications));
                generalNotice.setVisibility(View.VISIBLE);
            } else {
                generalNotice.setVisibility(View.GONE);
            }

            ImageView specificCommentNotice = (ImageView) view.findViewById(R.id.specific_comment_notice);
            if (!mDataBase.hasSpecificComment(sSingleton.idActivityStudent))
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
            if (sSingleton.portfolioClass.getPerfil().equals("S")) {
                if (sSingleton.idVersionActivity != sSingleton.idCurrentVersionActivity) {
                    menu.removeItem(android.R.id.paste);
                    menu.removeItem(android.R.id.cut);
                }
            }
//            createAddSpecificCommentButton(getCaretYPosition(mRtMessageField.getSelectionStart())); //Criando mesmo se usuário for aluno?
            if (sSingleton.portfolioClass.getPerfil().equals("T") && sSingleton.guestUserComments) {
                createAddSpecificCommentButton(getCaretYPosition(mRtMessageField.getSelectionStart()));
                menu.removeItem(android.R.id.paste);
                menu.removeItem(android.R.id.cut);
            }

            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            View v = getView().findViewWithTag("plus");
            if (v != null)
                mScrollView.removeView(v);
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        private void createSpecificCommentNote(float yPosition, String selectedText) {
            mCurrentSpecificComment++;
            Log.d("editor ", "current note is now:" + mCurrentSpecificComment);
            float yButton = 0;
            int idButton = -1;

            if (yPosition != 0)
                yButton = yPosition - 2;

            idButton = mCurrentSpecificComment;

            mSelectedActualText = selectedText;

            mSpecificCommentsNotes.put(idButton, new Note(idButton, selectedText, yButton));
            Button btn = createButton(idButton, String.valueOf(mCurrentSpecificComment), yButton);
            if (btn != null) {
                mScrollView.addView(btn);
            }

            mRtManager.onEffectSelected(Effects.BGCOLOR, mGreenLight, idButton);
            mRtMessageField.setSelected(false);
            mRtMessageField.clearFocus();

            setSpecificCommentNoteValue();

            saveText();

            btn.callOnClick();
        }

        private void createAddSpecificCommentButton(float y) {
            Context context = getContext();
            if (context != null) {
                final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                Button addButton = (Button) inflater.inflate(R.layout.partial_specific_comment, mScrollView, false);
                addButton.setY(y);
                addButton.setX(5);
                addButton.setTag("plus");
                addButton.setText("+");

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mRtMessageField.getText().toString().isEmpty()) {
                            startSelection = mRtMessageField.getSelectionStart();
                            endSelection = mRtMessageField.getSelectionEnd();
                            String selectedText = getSelectedText();

                            if (!selectedText.isEmpty()) {
                                if (selectedText.length() > 0) {
                                    if (canCreateButton(startSelection, endSelection)) {
                                        sSingleton.selectedText = mRtMessageField.getText().toString().substring(startSelection, endSelection);
                                        createSpecificCommentNote(getCaretYPosition(startSelection), selectedText);
                                    }
                                }
                            }
                        }
                    }
                });

                mScrollView.addView(addButton);
            }
        }
    }
}