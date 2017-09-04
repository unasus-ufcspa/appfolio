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
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.onegravity.rteditor.utils.Constants;
import com.ufcspa.unasus.appportfolio.activities.MainActivity;
import com.ufcspa.unasus.appportfolio.adapter.CommentAdapter;
import com.ufcspa.unasus.appportfolio.database.DataBase;
import com.ufcspa.unasus.appportfolio.model.Attachment;
import com.ufcspa.unasus.appportfolio.model.Comentario;
import com.ufcspa.unasus.appportfolio.model.OneComment;
import com.ufcspa.unasus.appportfolio.model.Singleton;
import com.ufcspa.unasus.appportfolio.model.Sync;
import com.ufcspa.unasus.appportfolio.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//import com.ufcspa.unasus.appportfolio.model.webClient.CommentClient;

/**
 * Created by Desenvolvimento on 07/12/2015.
 */
public class CommentsFragment extends HelperFragment {
    private final Handler mHandler = new Handler();
    ArrayList<OneComment> oneComments;
    CommentAdapter adapterComments;
    int lastID;
    ArrayList<Comentario> lista;
    private boolean mAttach;
    private ListView mListView;
    private Button mBtGenMess;
    private Button mBtAttachment;
    private EditText mEdtMessage;
    private LoadComments mLoadComments;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d("Handler", "Handler is running...");
            MainActivity main = ((MainActivity) getActivity());
            if (main != null)
                main.downloadFullDataComments(Singleton.getInstance().idActivityStudent);

            loadCom();
            adapterComments.refresh(oneComments);

            mHandler.postDelayed(this, 5000);
        }
    };

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            loadCom();
            adapterComments.refresh(oneComments);

            removeGeneralCommentsNotifications();
        }
    };

    private void removeGeneralCommentsNotifications() {
        ArrayList<Integer> idsNotification = source.getGeneralCommentsNotifications(singleton.idActivityStudent);
        for (Integer id : idsNotification) {
            Sync sync = new Sync(singleton.device.get_id_device(), "tb_notice", id, singleton.idActivityStudent);
            DataBase.getInstance(getContext()).insertIntoTBSync(sync);
        }
        source.deleteAllNotifications(idsNotification);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, null);

        source = DataBase.getInstance(getActivity());

        singleton = Singleton.getInstance();
//        singleton.idActivityStudent = source.getActivityStudentID(singleton.activity.getIdAtivity(), singleton.portfolioClass.getIdPortfolioStudent());
        Log.d("Comments", "On createView entrou");

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("Comments", "On view Created entrou");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oneComments = new ArrayList<>(70);
        adapterComments = new CommentAdapter(getContext(), oneComments);
//        loadCom();
        mLoadComments = new LoadComments();
        mLoadComments.execute();
        Log.d("Comments", "On create entrou");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mBroadcastReceiver, new IntentFilter("call.connection.action"));
    }

    @Override
    public void onResume() {
        Log.d("Comments", "On resume entrou");
        super.onResume();
        mEdtMessage = (EditText) getView().findViewById(R.id.edtMessage);
        mBtGenMess = (Button) getView().findViewById(R.id.gen_messag_bt);
        mBtAttachment = (Button) getView().findViewById(R.id.bt_add_attachment);
        mListView = (ListView) getView().findViewById(R.id.listView1);
//                    loadCom();
//        if (mLoadComments.getStatus()!= AsyncTask.Status.RUNNING) {
//            mLoadComments.execute();
//        }
        if (!singleton.guestUser) {
            mBtAttachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addAttachmentToComments();
                }
            });
            mBtGenMess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Comments mAttach", "inserindo comentario");
                    if (mAttach) {
                        Log.d("Comments mAttach", "tentando inserir view anexo");
                        addOneComment(true);
                        mAttach = false;
                        mEdtMessage.setText("");
                    } else {
                        if (!mEdtMessage.getText().toString().isEmpty()) {
                            Log.d("Comments mAttach", "tentando inserir comentario normal");
                            adapterComments.refresh(new ArrayList<OneComment>());
                            addOneComment(false);
                            mEdtMessage.setText("");
                        } else {
                            Log.e("comment", "tentou inserir comentario vazio");
                        }
                    }
                }
            });
        } else {
            RelativeLayout form = (RelativeLayout) getView().findViewById(R.id.form);
            form.setBackgroundResource(R.color.gray_4);
            mBtGenMess.setAlpha((float) 0.5);
            mBtAttachment.setAlpha((float) 0.5);
            mEdtMessage.setEnabled(false);
            mBtAttachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Acesso restrito a usuários convidados", Toast.LENGTH_LONG).show();
                }
            });
            mBtGenMess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Acesso restrito a usuários convidados", Toast.LENGTH_LONG).show();
                }
            });
            mEdtMessage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Toast.makeText(getContext(), "Acesso restrito a usuários convidados", Toast.LENGTH_LONG).show();
                    return false;
                }
            });
        }
        //addItems();
        setarListView();

        mHandler.postDelayed(mRunnable, 5000);
    }

    @Override
    public void onDestroy() {
        Log.d("LifeCycle", "onDestroy");
        mHandler.removeCallbacks(mRunnable);
        MainActivity.shouldSend = false;
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    /**
     *  MÉTODO PARA ADICIONAR UM COMENTARIO NA VIEW, CASO PASSE TRUE POR PARAMETRO SERÁ INSERIDO UM ANEXO
     *
     *
     * */
    public void addOneComment(boolean attachment) {
        Comentario c = getCommentFromText();
        OneComment oneComment;
        if (attachment) {
            insertComment(c);
            Log.d("comment attachment ", "é anexo a ser inserido");
            oneComment = new OneComment(false, "", convertDateToTime(c.getDateComment()), convertDateToDate(c.getDateComment()), true);
            oneComment.idAttach = Singleton.getInstance().lastIdAttach;
            oneComment.idAuthor = c.getIdAuthor();
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());
            c.setDateComment(currentDateandTime); //TODO
            c.setDateSend(c.getDateComment());
            c.setTxtReference("");
            insertComment(c);
            oneComment = new OneComment(false, mEdtMessage.getText().toString(), convertDateToTime(c.getDateComment()), convertDateToDate(c.getDateComment()));
            oneComment.idAuthor = c.getIdAuthor();
        }
        //oneComments.clear();
        oneComments.add(oneComment);
        adapterComments.refresh(oneComments);
        adapterComments.notifyDataSetChanged();
    }



    /**
     *  MÉTODO PARA INSERIR UM NOVO ANEXO
     *
     *
     * */
    public void addAtach() {
        Log.d("comments mAttach", "add atach selecionado");
        adapterComments.refresh(oneComments);
        adapterComments.notifyDataSetChanged();
        mAttach = true;
        mEdtMessage.setText("");
        mBtGenMess.performClick();
//        insertAtach();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        Log.d("comment attachment ", "entrando no onActivity for Result");
//
//        if (resultCode == Activity.RESULT_OK && requestCode != Constants.CROP_IMAGE)
//            //Log.d("comments","request code:"+requestCode);
//            addAtach();
//        else
//            Log.d("comment attachment ", "mAttach cancelado");
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.CROP_IMAGE:
                    saveImageOnAppDir();
                    insertFileIntoDataBase(mCurrentPhotoPath + ".jpg", "I");
                    addAtach();
                    break;
                case REQUEST_IMAGE_CAPTURE:
                    galleryAddPic();
                    launchCropImageIntent();
                    addAtach();
                    break;
                case RESULT_LOAD_IMAGE:
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
                        insertFileIntoDataBase(mCurrentPhotoPath + ".jpg", "I");
                        addAtach();
                    } else if (mimeType.startsWith("video")) {
                        saveVideoOnAppDir();
                        insertFileIntoDataBase(mCurrentPhotoPath + ".mp4", "V");
                        addAtach();
                    }
                    break;
                case PICKFILE_RESULT_CODE:
                    mCurrentPhotoPath = getPDFPath(getContext(), data.getData());
                    savePDFOnAppDir();
                    insertFileIntoDataBase(mCurrentPhotoPath + ".pdf", "T");
                    addAtach();
                    break;
                case REQUEST_VIDEO_CAPTURE:
                    galleryAddPic();
                    saveVideoOnAppDir();
                    insertFileIntoDataBase(mCurrentPhotoPath + ".mp4", "V");
                    addAtach();
                    break;
                case REQUEST_FOLIO_ATTACHMENT:
                    int idAttachment = data.getIntExtra("idAttachment", -1);
                    Log.d("Comments", "Id Attachment: " + idAttachment);
                    if (idAttachment != -1) {
                        Singleton.getInstance().lastIdAttach = idAttachment;
                        addAtach();
                    }
                    break;
            }
        } else
            Log.d("comment attachment ", "mAttach cancelado");
    }


    /**
     *  MÉTODO PARA INSERIR ANEXO NA VIEW
     *
     *
     * */
    public void insertAtach() {
        Log.d("Comments mAttach", "tentando inserir view anexo");
//        loadCom();
//        mLoadComments.execute();
        addOneComment(true);
        mAttach = false;
        mEdtMessage.setText("");
    }

    /**
     *  MÉTODOS PARA RECUPERAR OBJETO COMENTARIO DA VIEW
     *
     *
     * */
    public Comentario getCommentFromText() {
        Singleton singleton = Singleton.getInstance();
        Comentario c = new Comentario();
        c.setDateComment(getActualTime());
        c.setIdAuthor(singleton.user.getIdUser());
        c.setTypeComment("G");
        c.setDateSend(c.getDateComment());
        c.setTxtComment(mEdtMessage.getText().toString());
        c.setIdActivityStudent(singleton.activity.getIdActivityStudent());
        return c;
    }

    public void loadCom() {
        DataBase db = DataBase.getInstance(getActivity());
        Singleton singleton = Singleton.getInstance();
        lista = (ArrayList<Comentario>) db.listGComments(singleton.activity.getIdActivityStudent(), "G"); //lista comentario gerais
        //lista = (ArrayList<Comentario>) db.listCommentsTESTE();
        oneComments = new ArrayList<OneComment>(20);
        if (lista.size() != 0) {

            for (Comentario c : lista) {
                OneComment one = new OneComment(c.getIdAuthor() != singleton.user.getIdUser(),
                        c.getTxtComment(), convertDateToTime(c.getDateSend()), convertDateToDate(c.getDateSend()));
                if (c.getIdAttach() != 0) {
                    one.atach = true;
                    one.idAttach = c.getIdAttach();
                    Log.d("comments", "id mAttach:" + one.idAttach);
                }
                one.idAuthor = c.getIdAuthor();
                oneComments.add(one);
            }

            Log.d("Banco", "Lista populada:" + lista.size());
        } else {
            Log.d("Banco", "Lista retornou vazia!");
        }

    }

    /**
     *  MÉTODO PARA SETAR O ADAPTER COMMENTS NA LISTVIEW E ABRIR O ANEXO CONFORME SEU TIPO
     **/
    public void setarListView() {
        mListView.setAdapter(adapterComments);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("comments", "clicou no item na position:" + position + " conteudo:" + oneComments.get(position));
                if (oneComments.get(position).idAttach != 0) {
                    Log.d("comments", "selecionou um anexo");
                    Attachment att = DataBase.getInstance(getActivity()).getAttachmentByID(oneComments.get(position).idAttach);
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

    /**
     *  MÉTODO PARA SALVAR COMENTARIO NO BANCO E ENVIAR VIA JSON
     *
     *
     * */
    private void insertComment(Comentario c) {
        try {
            Singleton single = Singleton.getInstance();
            DataBase db = DataBase.getInstance(getActivity());
            lastID = db.insertComment(c);
            if (mAttach) {
                db.insertAttachComment(lastID, single.lastIdAttach);
                String idDevice = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                Sync sync = new Sync(idDevice, "tb_attach_comment", DataBase.getInstance(getContext()).getLastIdAttachComment(), singleton.idActivityStudent);
                DataBase.getInstance(getContext()).insertIntoTBSync(sync);
            }
            Log.d("Banco:", "comentario inserido no bd interno com sucesso");
        } catch (Exception e) {
            Log.e("Erro:", "" + e.getMessage());
        }
        String idDevice = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
//        if(isOnline()) {
//            Log.d("Banco:", c.toJSON().toString());
//            try {
//                CommentClient client = new CommentClient(getActivity().getApplicationContext(), c);
//                //System.out.println(c.toJSON().toString());
//
//                Sync sync = new Sync(idDevice, "tb_comment", 42);
//                client.postJson(c.toJSON(), sync.toJSON());
//            } catch (Exception e) {
//                Log.e("JSON act", "" + e.getMessage());
//            }
//        }else{
            // add in sync queue
        Sync sync = new Sync(idDevice, "tb_comment", lastID, singleton.idActivityStudent);
        DataBase.getInstance(getContext()).insertIntoTBSync(sync);

        MainActivity main = ((MainActivity) getActivity());
        if (main != null)
            main.sendFullData();

//        }
    }



    /**
     *  REESCRITA DO MÉTODO DA CLASSE FRAG, USADO PARA SALVAR COMETARIO COM ANEXO NA TABELA ATTACH_COMMENT
     *
     *
     * */
    @Override
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
                Singleton single = Singleton.getInstance();
                String name = input.getText().toString();
                if (name.isEmpty()) {
                    name = "Anexo";
                }
                single.lastIdAttach = source.insertAttachment(new Attachment(0, type, name, path, 0, 0));
                if (lastID != 0 && single.lastIdAttach != -1 && single.lastIdAttach != 0) {
//                    source.insertAttachComment(lastId, single.lastIdAttach);
                }
            }
        });
        builder.show();
    }

    /**
     * MÉTODO PARA PEGAR A DATA ATUAL
     */

    public String getActualTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        return strDate;
    }

    /**
     * MÉTODOS PARA CONVERTER O FORMATO DE DATA VINDO DO BANCO PARA EXIBIR NA VIEW
     */


    public String convertDateToTime(String atualDate) {
        String shortTimeStr = "00:00";
        //Log.d("comments","date receiving :"+atualDate);
        try {
            if (atualDate != null) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = null;
                date = df.parse(atualDate);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                shortTimeStr = sdf.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return shortTimeStr;
    }

    public String convertDateToDate(String atualDate) {
        String shortDateStr = "12/12/2012";
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            date = df.parse(atualDate);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            shortDateStr = sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return shortDateStr;
    }

    private class LoadComments extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            loadCom();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            adapterComments.refresh(oneComments);
        }
    }



}
