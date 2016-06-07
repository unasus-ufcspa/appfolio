package com.ufcspa.unasus.appportfolio.Activities.Fragments;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.onegravity.rteditor.utils.Constants;
import com.ufcspa.unasus.appportfolio.Activities.MainActivity;
import com.ufcspa.unasus.appportfolio.Adapter.CommentAdapter;
import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.Model.Comentario;
import com.ufcspa.unasus.appportfolio.Model.OneComment;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.Model.Sync;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//import com.ufcspa.unasus.appportfolio.Model.WebClient.CommentClient;

/**
 * Created by Desenvolvimento on 07/12/2015.
 */
public class FragmentComments extends Frag {
    private final Handler h = new Handler();
    ArrayList<OneComment> oneComments;
    CommentAdapter adapterComments;
    int lastID;
    ArrayList<Comentario> lista;
    private boolean attach;
    private ListView lv;
    private Button btGenMess;
    private Button btAttachment;
    //private LoremIpsum ipsum;
    private EditText edtMessage;
    private LoadComments loadComments;
    private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d("Handler","Handler is running...");
            MainActivity main = ((MainActivity) getActivity());
            if (main != null)
                main.downloadFullDataComments(Singleton.getInstance().idActivityStudent);

//            loadCom();
//            adapterComments.refresh(oneComments);

            h.postDelayed(this, 5000);
        }
    };

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadCom();
            adapterComments.refresh(oneComments);

            removeGeneralCommentsNotifications();
        }
    };

    private void removeGeneralCommentsNotifications() {
        ArrayList<Integer> idsNotification = source.getGeneralCommentsNotifications(singleton.idActivityStudent);
        for (Integer id : idsNotification) {
            Sync sync = new Sync(singleton.device.get_id_device(), "tb_notice", id, singleton.idActivityStudent);
            DataBaseAdapter.getInstance(getContext()).insertIntoTBSync(sync);
        }
        source.deleteAllNotifications(idsNotification);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, null);

        source = DataBaseAdapter.getInstance(getActivity());

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
        loadComments = new LoadComments();
        loadComments.execute();
        Log.d("Comments", "On create entrou");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, new IntentFilter("call.connection.action"));
    }

    @Override
    public void onResume() {
        Log.d("Comments", "On resume entrou");
        super.onResume();
        edtMessage = (EditText) getView().findViewById(R.id.edtMessage);
        btGenMess = (Button) getView().findViewById(R.id.gen_messag_bt);
        btAttachment = (Button) getView().findViewById(R.id.bt_add_attachment);
        lv = (ListView) getView().findViewById(R.id.listView1);
        btAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAttachmentToComments();
            }
        });
        btGenMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Comments attach", "inserindo comentario");
                if (attach) {
                    Log.d("Comments attach", "tentando inserir view anexo");
//                    loadCom();
                    loadComments.execute();
                    addOneComment(true);
                    attach = false;
                    edtMessage.setText("");
                } else {

                    if (!edtMessage.getText().toString().isEmpty()) {
                        Log.d("Comments attach", "tentando inserir comentario normal");
                        adapterComments.refresh(new ArrayList<OneComment>());
                        addOneComment(false);
                        edtMessage.setText("");
                    } else {
                        Log.e("comment", "tentou inserir comentario vazio");
                    }
                }
            }
        });
        //addItems();
        setarListView();

        h.postDelayed(myRunnable, 5000);
    }

    @Override
    public void onDestroy() {
        Log.d("LifeCycle", "onDestroy");
        h.removeCallbacks(myRunnable);
        MainActivity.shouldSend = false;
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
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
            oneComment = new OneComment(false, "Anexo", convertDateToTime(c.getDateComment()), convertDateToDate(c.getDateComment()), true);
            oneComment.idAttach= Singleton.getInstance().lastIdAttach;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());
            c.setDateComment(currentDateandTime); //TODO
            c.setDateSend(c.getDateComment());
            c.setTxtReference("");
            insertComment(c);
            oneComment = new OneComment(false, edtMessage.getText().toString(), convertDateToTime(c.getDateComment()), convertDateToDate(c.getDateComment()));
        }
        //oneComments.clear();
        oneComments.add(oneComment);
        adapterComments.refresh(oneComments);
        //adapterComments.notifyDataSetChanged();
    }



    /**
     *  MÉTODO PARA INSERIR UM NOVO ANEXO
     *
     *
     * */
    public void addAtach() {
        Log.d("comments attach", "add atach selecionado");
        //adapterComments.refresh(oneComments);
        attach = true;
        edtMessage.setText("anexo");
        //btGenMess.performClick();
        insertAtach();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d("comment attachment ", "entrando no onActivity for Result");
////
//        if (resultCode == Activity.RESULT_OK && requestCode != Constants.CROP_IMAGE)
//            //Log.d("comments","request code:"+requestCode);
//            addAtach();
//        else
//            Log.d("comment attachment ", "attach cancelado");
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
            } else if (requestCode == REQUEST_FOLIO_ATTACHMENT) {
                int idAttachment = data.getIntExtra("idAttachment", -1);
                Log.d("Comments", "Id Attachment: " + idAttachment);
                if (idAttachment != -1) {
                    Singleton.getInstance().lastIdAttach = idAttachment;
                    addAtach();
                    if (lastID != 0)
                        DataBaseAdapter.getInstance(getActivity()).insertAttachComment(lastID, idAttachment);
                }
            }
        }
    }


    /**
     *  MÉTODO PARA INSERIR ANEXO NA VIEW
     *
     *
     * */
    public void insertAtach() {
        Log.d("Comments attach", "tentando inserir view anexo");
//        loadCom();
        loadComments.execute();
        addOneComment(true);
        attach = false;
        edtMessage.setText("");
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
        c.setTxtComment(edtMessage.getText().toString());
        c.setIdActivityStudent(singleton.activity.getIdActivityStudent());
        return c;
    }

    public void loadCom() {
        DataBaseAdapter db = DataBaseAdapter.getInstance(getActivity());
        Singleton singleton = Singleton.getInstance();
        lista = (ArrayList<Comentario>) db.listComments(singleton.activity.getIdActivityStudent(), "G", 0);//lista comentario gerais filtrando por C
        oneComments = new ArrayList<OneComment>(20);
        if (lista.size() != 0) {

            for (Comentario c : lista) {
                OneComment one = new OneComment(c.getIdAuthor() != singleton.user.getIdUser(),
                        c.getTxtComment(), convertDateToTime(c.getDateSend()), convertDateToDate(c.getDateSend()));
                if (c.getIdAttach() != 0) {
                    one.atach = true;
                    one.idAttach = c.getIdAttach();
                    Log.d("comments", "id attach:" + one.idAttach);
                }
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
        lv.setAdapter(adapterComments);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("comments", "clicou no item na position:" + position + " conteudo:" + oneComments.get(position));
                if (oneComments.get(position).idAttach != 0) {
                    Log.d("comments", "selecionou um anexo");
                    Attachment att = DataBaseAdapter.getInstance(getActivity()).getAttachmentByID(oneComments.get(position).idAttach);
                    if (att.getTpAttachment() != null) {
                        Log.d("comments", "localpath attach:" + att.getNmSystem());
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
            DataBaseAdapter db = DataBaseAdapter.getInstance(getActivity());
            lastID = db.insertComment(c);
            if (attach) {
                //db.insertAttachComment(lastID, single.lastIdAttach);
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
        DataBaseAdapter.getInstance(getContext()).insertIntoTBSync(sync);

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
                single.lastIdAttach = source.insertAttachment(new Attachment(0, type, name, path, 0));
                if (lastID != 0 && single.lastIdAttach != -1 && single.lastIdAttach != 0) {
                    DataBaseAdapter.getInstance(getActivity()).insertAttachComment(lastID, single.lastIdAttach);
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
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            date = df.parse(atualDate);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            shortTimeStr = sdf.format(date);
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
