package com.ufcspa.unasus.appportfolio.activities.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.onegravity.rteditor.converter.ConverterHtmlToText;
import com.onegravity.rteditor.utils.Constants;
import com.ufcspa.unasus.appportfolio.activities.MainActivity;
import com.ufcspa.unasus.appportfolio.adapter.SpecificCommentAdapter;
import com.ufcspa.unasus.appportfolio.database.DataBase;
import com.ufcspa.unasus.appportfolio.model.Attachment;
import com.ufcspa.unasus.appportfolio.model.Comentario;
import com.ufcspa.unasus.appportfolio.model.Note;
import com.ufcspa.unasus.appportfolio.model.OneComment;
import com.ufcspa.unasus.appportfolio.model.Singleton;
import com.ufcspa.unasus.appportfolio.model.Sync;
import com.ufcspa.unasus.appportfolio.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by icaromsc on 15/02/2016.
 */
public class SpecificCommentsFragment extends HelperFragment {

    private static boolean EXPANDED_FLAG = false;
    private final Handler mHandler = new Handler();
    int lastId;
    private SpecificCommentAdapter mSpcAdapter;
    private ListView mListView;
    private boolean mAttach;
    private Button mBtGenMess;
    private Button mBtAttachment;
    //private LoremIpsum ipsum;
    private TextView mTvNote;
    private EditText mEdtMessage;
    private String mReference;
    private Note mNoteNow;
    private ArrayList<Comentario> mCommentList;
    private ArrayList<OneComment> mOneCommentsList;
    //    private ImageButton btExpand;
    private LoadCommentsFromDB mLoadCommentsFromDB;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
//            Log.d("Handler", "Handler is running...");
//            MainActivity main = ((MainActivity) getActivity());
//            if (main != null)
//                main.downloadFullDataComments(Singleton.getInstance().idActivityStudent);
//
////            mLoadCommentsFromDB();
////            mSpcAdapter.refresh(mOneCommentsList);
//
//            mHandler.postDelayed(this, 5000);
        }
    };

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadCommentsFromDB();
            mSpcAdapter.refresh(mOneCommentsList);

            removeSpecificCommentsNotifications();
        }
    };

    private void removeSpecificCommentsNotifications() {
        ArrayList<Integer> idsNotification = source.getSpecificCommentsNotificationsID(singleton.idActivityStudent, singleton.idCurrentVersionActivity);
        for (Integer id : idsNotification) {
            Sync sync = new Sync(singleton.device.get_id_device(), "tb_notice", id, singleton.idActivityStudent);
            DataBase.getInstance(getContext()).insertIntoTBSync(sync);
        }
        source.deleteAllNotifications(idsNotification);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_specific_comment, null);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOneCommentsList = new ArrayList<>(70);
        mSpcAdapter = new SpecificCommentAdapter(getContext(), mOneCommentsList);
//        mLoadCommentsFromDB();
        mLoadCommentsFromDB = new LoadCommentsFromDB();
        mLoadCommentsFromDB.execute();
        Log.d("Comments", "On create entrou");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mBroadcastReceiver, new IntentFilter("call.connection.action"));

        // TODO: 22/11/2016 inserir tutorial
        Target target = new ViewTarget(R.id.email_sign_in_button, getActivity());
        MainActivity mainActivity = new MainActivity();
        mainActivity.showCase(target, "Comentario especifico", "aqui você cria comentários específicos");
    }

    private void hide() {
        mBtGenMess = (Button) getView().findViewById(R.id.gen_messag_bt);
        mBtAttachment = (Button) getView().findViewById(R.id.bt_add_attachment);
        mEdtMessage = (EditText) getView().findViewById(R.id.edtMessage);
        mTvNote = (TextView) getView().findViewById(R.id.txSelectedNote);
//        btExpand = (ImageButton) getView().findViewById(R.id.btn_expand_ref);
        mListView = (ListView) getView().findViewById(R.id.listView1);

        mBtGenMess.setVisibility(View.GONE);
        mBtAttachment.setVisibility(View.GONE);
        mEdtMessage.setVisibility(View.GONE);
        mTvNote.setVisibility(View.GONE);
//        btExpand.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
        TextView txMsgHide = (TextView) getView().findViewById(R.id.txtWhenHiDE);
        txMsgHide.setVisibility(View.VISIBLE);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Singleton.getInstance().note.getBtId() == 0)
            hide();

        singleton = Singleton.getInstance();
        source = DataBase.getInstance(getContext());

        mBtGenMess = (Button) getView().findViewById(R.id.gen_messag_bt);
        mBtAttachment = (Button) getView().findViewById(R.id.bt_add_attachment);
        mEdtMessage = (EditText) getView().findViewById(R.id.edtMessage);
        mTvNote = (TextView) getView().findViewById(R.id.txSelectedNote);
//        btExpand = (ImageButton) getView().findViewById(R.id.btn_expand_ref);
        mListView = (ListView) getView().findViewById(R.id.listView1);
        mListView.setAdapter(mSpcAdapter);
        loadCommentsFromDB();
        try {
            mLoadCommentsFromDB.execute();
        } catch (Exception e) {
        }

        insertReference();
//        ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getView().getWindowToken(), 0);
//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        if (!singleton.guestUser) {
            mBtGenMess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mEdtMessage.getText().toString().isEmpty()) {
                        Comentario c = getCommentFromText();
                        mOneCommentsList.add(new OneComment(false, mEdtMessage.getText().toString(), convertDateToTime(c.getDateComment()), convertDateToDate(c.getDateComment())));
                        mEdtMessage.setText("");
                        insertComment(c);
                    }
                }
            });
            mTvNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (EXPANDED_FLAG == false) {
                        mTvNote.getLayoutParams().height = 330;
                        mTvNote.setMovementMethod(new ScrollingMovementMethod());
                        EXPANDED_FLAG = true;
                        mTvNote.requestLayout();
                        Log.d("editor", "layout mReference expanded true");
                    } else {
                        mTvNote.getLayoutParams().height = 70;
                        //mTvNote.setMovementMethod(null);
                        EXPANDED_FLAG = false;
                        mTvNote.requestLayout();
                        Log.d("editor", "layout mReference expanded false");
                    }
                }
            });

            mBtAttachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addAttachmentToComments();
                }
            });
        } else {
            RelativeLayout form = (RelativeLayout) getView().findViewById(R.id.form);
            form.setBackgroundResource(R.color.gray_4);
            mBtGenMess.setAlpha((float) 0.5);
            mBtAttachment.setAlpha((float) 0.5);
            mEdtMessage.setEnabled(false);
            mBtGenMess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Acesso restrito a usuários convidados", Toast.LENGTH_LONG).show();
                }
            });
            mBtAttachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Acesso restrito a usuários convidados", Toast.LENGTH_LONG).show();
                }
            });
        }
        setarListView();

        mHandler.postDelayed(mRunnable, 5000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
        MainActivity.shouldSend = false;
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mBroadcastReceiver);
    }

    public Comentario getCommentFromText() {
        Singleton singleton = Singleton.getInstance();
        Comentario c = new Comentario();
        c.setDateComment(getActualTime());
        c.setDateSend(c.getDateComment());
        c.setIdAuthor(singleton.user.getIdUser());
        c.setTypeComment("O");
        //Log.d("comments", "mReference setting in C spcific comment:" + mTvNote.getText().toString());
        c.setTxtReference(mTvNote.getText().toString());
        c.setTxtComment(mEdtMessage.getText().toString());
        c.setIdActivityStudent(singleton.activity.getIdActivityStudent());
        return c;
    }

    public void loadCommentsFromDB() {
        try {
            DataBase db = DataBase.getInstance(getActivity());

            Log.d("activity", "actual version_activity:" + singleton.idCurrentVersionActivity);

            Log.d("observations", "list observations:" + db.getObservation(db.getIDVersionSrvByLocalID(singleton.idCurrentVersionActivity)).toString());
            Log.d("observations", "list ALL observations:" + db.getObservationALL().toString());
            Singleton singleton = Singleton.getInstance();
            mCommentList = (ArrayList<Comentario>) db.listObsComments(singleton.idActivityStudent, singleton.note.getBtId()); //mCommentList comentario gerais filtrando por O
            mOneCommentsList = new ArrayList<>(10);
            Log.d("comments", "comentarios especificos:" + mCommentList.toString());

            if (mCommentList.size() != 0) {

                for (Comentario c : mCommentList) {
                    OneComment one = new OneComment(c.getIdAuthor() == singleton.user.getIdUser(),
                            c.getTxtComment(), convertDateToTime(c.getDateSend()), convertDateToDate(c.getDateSend()));
                    if (c.getIdAttach() != 0) {
                        one.atach = true;
                        one.idAttach = c.getIdAttach();
                        Log.d("comments", "id mAttach:" + one.idAttach);
                    }
                    one.idAuthor = c.getIdAuthor();
                    mOneCommentsList.add(one);
                }
                Log.d("comments", "one Comments exist, size:" + mOneCommentsList.size());
//            mSpcAdapter.refresh(mOneCommentsList);
                Log.d("comments", "adapter itens:" + mSpcAdapter.getCount());
            } else {
                Log.d("Banco", "Lista retornou vazia!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.wtf("comments", "erro doido em popular specific comments:" + e.getMessage());
        }
    }

    public void insertFileIntoDataBase(final String path, final String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Escolha um nome:");
        builder.setCancelable(false);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            builder.setOnDismissListener(null);
//        }

        // Set up the input
        final EditText input = new EditText(getContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //source.saveAttachmentActivityStudent(path, type, singleton.idActivityStudent); //input.getText().toString()
                Singleton single = Singleton.getInstance();

                String name = input.getText().toString();
                if (name.isEmpty()) {
                    name = "Anexo";
                }
                single.lastIdAttach = DataBase.getInstance(getActivity()).insertAttachment(new Attachment(0, type, name, path, 0, 0));
                if (lastId != 0 && single.lastIdAttach != -1 && single.lastIdAttach != 0) {
                    DataBase.getInstance(getActivity()).insertAttachComment(lastId, single.lastIdAttach);
                }
            }
        });

        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d("comment attachment ", "entrando no onActivity for Result");
////
//        if(resultCode == Activity.RESULT_OK && requestCode!= Constants.CROP_IMAGE)
//            //Log.d("comments","request code:"+requestCode);
//            addAtach();
//        else
//            Log.d("comment attachment ", "mAttach cancelado");
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.CROP_IMAGE) {
                saveImageOnAppDir();
                insertFileIntoDataBase(mCurrentPhotoPath, "I");
                addAtach();
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                galleryAddPic();
                launchCropImageIntent();
            } else if (requestCode == RESULT_LOAD_IMAGE) {
                Uri selectedUri = data.getData();
                String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.MIME_TYPE};

                Cursor cursor = getActivity().getContentResolver().query(selectedUri, columns, null, null, null);
                cursor.moveToFirst();

                int pathColumnIndex = cursor.getColumnIndex(columns[0]);
                int mimeTypeColumnIndex = cursor.getColumnIndex(columns[1]);

                String contentPath = cursor.getString(pathColumnIndex);
                String mimeType = cursor.getString(mimeTypeColumnIndex);

                cursor.close();

                mCurrentPhotoPath = contentPath;

                if (mimeType.startsWith("image")) {
                    saveImage();
                    launchCropImageIntent();
                } else if (mimeType.startsWith("video")) {
                    saveVideoOnAppDir();
                    insertFileIntoDataBase(mCurrentPhotoPath, "V");
                    addAtach();
                }

            } else if (requestCode == PICKFILE_RESULT_CODE) {
                mCurrentPhotoPath = getPDFPath(getContext(), data.getData());
                savePDFOnAppDir();
                insertFileIntoDataBase(mCurrentPhotoPath, "T");
                addAtach();
            } else if (requestCode == REQUEST_VIDEO_CAPTURE) {
                galleryAddPic();
                saveVideoOnAppDir();
                insertFileIntoDataBase(mCurrentPhotoPath, "V");
                addAtach();
            }
            if (requestCode == REQUEST_FOLIO_ATTACHMENT) {
                int idAttachment = data.getIntExtra("idAttachment", -1);
                if (idAttachment != -1) {
                    Singleton.getInstance().lastIdAttach = idAttachment;
                    addAtach();
                    if (lastId != 0)
                        DataBase.getInstance(getActivity()).insertAttachComment(lastId, idAttachment);
                }
            }
        }
    }

    public void addAtach() {
        Log.d("comments mAttach", "add atach selecionado");
        //adapterComments.refresh(mOneCommentsList);
        mAttach = true;
        mEdtMessage.setText("anexo");
        insertAtach();
    }

    public void insertAtach() {
        Log.d("comment attachment", "entrando no insertAtach");
        Comentario c = getCommentFromText();
//        mLoadCommentsFromDB();
        mLoadCommentsFromDB.execute();
        insertComment(c);
        OneComment oneComment = new OneComment(false, "Anexo", convertDateToTime(c.getDateComment()), convertDateToDate(c.getDateComment()), true);
        oneComment.idAttach = Singleton.getInstance().lastIdAttach;
        Log.d("comment attachment", "itens size:" + mOneCommentsList.size());
        mOneCommentsList.add(oneComment);
        Log.d("comment attachment", "itens size is now:" + mOneCommentsList.size());
        mSpcAdapter.refresh(mOneCommentsList);
        mAttach = false;
        mEdtMessage.setText("");
    }

    public void setarListView() {
        mListView.setAdapter(mSpcAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("comments", "clicou no item na position:" + position + " conteudo:" + mOneCommentsList.get(position));
                if (mOneCommentsList.get(position).idAttach != 0) {
                    Log.d("comments", "selecionou um anexo");
                    Attachment att = DataBase.getInstance(getActivity()).getAttachmentByID(mOneCommentsList.get(position).idAttach);
                    if (att.getTpAttachment() != null) {
                        Log.d("comments", "localpath mAttach:" + att.getNmSystem());
                        if (att.getTpAttachment().equals(Attachment.TYPE_TEXT)) {
                            Log.d("comments", "anexo do tipo texto");
                            //showPDFDialog(att.getLocalPath());
                            openPDF(att.getNmSystem());
                        } else if (att.getTpAttachment().equals(Attachment.TYPE_IMAGE)) {
                            Log.d("comments", "anexo do tipo imagem");
                            loadPhoto(att.getNmSystem());
                        } else if (att.getTpAttachment().equals(Attachment.TYPE_VIDEO)) {
                            Log.d("comments", "anexo do tipo video");
                            loadVideo(att.getNmSystem());
                        }
                    }
                }

            }
        });
    }

    private void insertComment(Comentario c) {
        try {
            //DataBase db = DataBase.getInstance(getActivity());
//            if (singleton.portfolioClass.getPerfil().equals("T") && source.isFirstSpecificComment(singleton.idActivityStudent, mNoteNow.getBtId()))
//                singleton.isFirstSpecificComment = true;



            if (source.isFirstSpecificComment(singleton.idActivityStudent, mNoteNow.getBtId()))
                singleton.isFirstSpecificComment = true;

            // recupera n_nota criada
            singleton.actualObservation.setNu_comment_activity(mNoteNow.getBtId());
            singleton.actualObservation.setTx_reference(mNoteNow.getSelectedText());
            singleton.actualObservation.setId_version_activity(source.getIDVersionSrvByLocalID(singleton.idVersionActivity));
            singleton.actualObservation.setId_comment_version(source.getIdObservationByNuCommentActivy(singleton.idActivityStudent, singleton.actualObservation.getNu_comment_activity()));
            singleton.actualObservation.setId_comment_version_srv(source.getIdObservationSrvByNuCommentActivy(singleton.idActivityStudent, singleton.actualObservation.getNu_comment_activity()));



            Log.d("specific", "actual obs in single:" + singleton.actualObservation);
            int idObservation;
            if (singleton.isFirstSpecificComment) {
                // empilha observation nas syncs
                idObservation = source.insertObservationByVersion(singleton.actualObservation);
                source.updateLastObservation(idObservation);
                Sync sync = new Sync();
                sync.setNm_table("tb_comment_version");
                sync.setCo_id_table(idObservation);
                sync.setId_activity_student(singleton.idActivityStudent);
                sync.setId_device(singleton.device.get_id_device());
                source.insertIntoTBSync(sync);

                // empilha version nas syncs
                sync = new Sync();
                sync.setNm_table("tb_version_activity");
                sync.setCo_id_table(singleton.idVersionActivity);
                sync.setId_activity_student(Singleton.getInstance().idActivityStudent);
                sync.setId_device(singleton.device.get_id_device());
                source.insertIntoTBSync(sync);

                singleton.isFirstSpecificComment = false;

            } else {
                idObservation = source.getIdObservationByNuCommentActivy(singleton.idActivityStudent, singleton.actualObservation.getNu_comment_activity());
            }
            c.setId_comment_version(idObservation);

            lastId = source.insertSpecificComment(c);

            Sync sync = new Sync();
            sync.setNm_table("tb_comment");
            sync.setCo_id_table(lastId);
            sync.setId_activity_student(singleton.idActivityStudent);
            sync.setId_device(singleton.device.get_id_device());
            source.insertIntoTBSync(sync);


            /*//NECESSITA ATUALIZAR -- comentado em 13/06/2016

            CommentVersion cv = new CommentVersion();
            cv.setId_comment(lastId);
            cv.setId_version_activity(singleton.idCurrentVersionActivity);
            int idCommentVersion = source.insertCommentVersionWhenUserComment(cv);

            Sync sync = new Sync();
            sync.setNm_table("tb_comment_version");
            sync.setCo_id_table(idCommentVersion);
            sync.setId_activity_student(singleton.idActivityStudent);
            sync.setId_device(singleton.device.get_id_device());
            source.insertIntoTBSync(sync);*/
           /*
                EMPILHA COMENTARIO ESPECIFICO NA SYNC(ANTIGO)

            Sync sync = new Sync(singleton.device.get_id_device(), "tb_comment", lastId, singleton.idActivityStudent);
            DataBase.getInstance(getContext()).insertIntoTBSync(sync);

            MainActivity main = ((MainActivity) getActivity());
            if (main != null)
                main.sendFullData();
            Log.d("Banco:", "comentario inserido no bd interno com sucesso");*/
            MainActivity main = ((MainActivity) getActivity());
            if (main != null)
                main.sendFullData();
        } catch (Exception e) {
            Log.e("Banco", "Erro:" + e.getMessage());
        }
        loadCommentsFromDB();
        mSpcAdapter.refresh(mOneCommentsList);
    }

    private void insertReference() {
        if (mSpcAdapter != null) {
            Singleton single = Singleton.getInstance();
            if (single.note.getSelectedText().isEmpty() || single.note.getSelectedText().equals("null")) {
                String r = source.getIdObservationTextByNuCommentActivy(single.idActivityStudent, single.note.getBtId());
                if (!r.isEmpty()) {
                    single.note.setSelectedText(r);
                } else
                    single.note.setSelectedText(singleton.selectedText);
            }
            mNoteNow = single.note;

            if (mCommentList != null && mCommentList.size() != 0) { // se a mCommentList nao estiver vazia quer dizer que a nota de referencia já existe no banco
                for (Comentario c: mCommentList) {
                    Log.d("comments mNoteNow", "referencia é :" + c.toString());
                }
                //mNoteNow.setSelectedText(mCommentList.get(0).getTxtReference());
                Log.d("comments mNoteNow", "mCommentList:" + mCommentList.get(0).toJSON());
            }


            if (mNoteNow != null) {
                if (mNoteNow.getSelectedText() != null && !mNoteNow.getSelectedText().toString().equalsIgnoreCase("null")) {
                    Log.d("comments", "receiving mReference...:" + mNoteNow.getSelectedText());
                    mReference = ConverterHtmlToText.convert(mNoteNow.getSelectedText());
                    if (mReference.contains("Referência:")) {
                        Log.d("comments", "specific comments contais referencia 'Referência:' in mReference");
                        mTvNote.setText(mReference);
                    } else {
                        mTvNote.setText("Referência: \n" + "\"" + mReference + "\"");
                    }
                }
            } else {
                Log.d("comments", "NoteNow is NULL");
            }
        }
    }

    public String getActualTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        return strDate;
    }

    public String convertDateToTime(String atualDate) {
        String shortTimeStr = "00:00";
        Log.d("comments", "date receiving :" + atualDate);
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            date = df.parse(atualDate);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            shortTimeStr = sdf.format(date);
            Log.d("comments", "date to hour :" + shortTimeStr);
        } catch (ParseException e) {
            // To change body of catch statement use File | Settings | File Templates.
            e.printStackTrace();
        }
        return shortTimeStr;
    }

    public String convertDateToDate(String atualDate) {
        String shortTimeStr = "12/12/2012";
        Log.d("comments", "date receiving :" + atualDate);
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            if (df != null) {
                date = df.parse(atualDate);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                shortTimeStr = sdf.format(date);
                Log.d("comments", "date to other date format :" + shortTimeStr);
            }
        } catch (ParseException e) {
            // To change body of catch statement use File | Settings | File Templates.
            e.printStackTrace();
        }
        return shortTimeStr;
    }

    private class LoadCommentsFromDB extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            //mLoadCommentsFromDB(); carregar comentarios de forma assincrona
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            mSpcAdapter.refresh(mOneCommentsList);
        }
    }





}
