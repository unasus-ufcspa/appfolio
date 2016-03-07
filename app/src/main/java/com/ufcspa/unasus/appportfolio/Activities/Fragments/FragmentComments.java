package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ufcspa.unasus.appportfolio.Adapter.CommentAdapter;
import com.ufcspa.unasus.appportfolio.Model.Comentario;
import com.ufcspa.unasus.appportfolio.Model.OneComment;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Desenvolvimento on 07/12/2015.
 */
public class FragmentComments extends Frag {
    ArrayList<OneComment> oneComments;
    CommentAdapter adapterComments;
    boolean attach;
    private ListView lv;
    private Button btGenMess;
    private Button btAttachment;
    //private LoremIpsum ipsum;
    private EditText edtMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, null);

        source = DataBaseAdapter.getInstance(getActivity());

        singleton = Singleton.getInstance();
//        singleton.idActivityStudent = source.getActivityStudentID(singleton.activity.getIdAtivity(), singleton.portfolioClass.getIdPortfolioStudent());


        //btGenMess.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addRandomItem();
//                lv.setAdapter(adapter);
//            }
//        });
        //ipsum = new LoremIpsum();

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
        adapterComments = new CommentAdapter(getContext(),oneComments);
        loadCom();
        Log.d("Comments", "On create entrou");
    }

    @Override
    public void onResume() {
        Log.d("Comments", "On resume entrou");
        super.onResume();
        edtMessage = (EditText) getView().findViewById(R.id.edtMessage);
        btGenMess = (Button) getView().findViewById(R.id.gen_messag_bt);
        btAttachment = (Button) getView().findViewById(R.id.bt_add_attachment);

        //btGenMess.setVisibility(View.INVISIBLE);

        lv = (ListView) getView().findViewById(R.id.listView1);

        //adapter = new CommentArrayAdapter(getActivity().getApplicationContext(), R.layout.comment_item);

        //lv.setAdapter(adapter);

        btAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAttachmentToComments();
            }
        });


        btGenMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addRandomItem();
                //lv.setAdapter(adapter);

                if (attach) {
                    Log.d("Comments attach", "tentando inserir view anexo");
                    loadCom();
                    addOneComment(true);
                    attach=false;
                    edtMessage.setText("");
                } else {

                    if (!edtMessage.getText().toString().isEmpty()) {
                        //addItems();
                        //loadCom();
//                    Comentario c = getCommentFromText();
//                    insertComment(c);
//                    OneComment oneComment;
//                    oneComment = new OneComment(false, edtMessage.getText().toString(), convertDateToTime(c.getDateComment()), convertDateToDate(c.getDateComment()));
                        //oneComments.add(addOneComment(false));
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
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("comment frag ", "pausou a fragment");
    }

    public void addOneComment(boolean attachment){
        Comentario c = getCommentFromText();
        OneComment oneComment;
        if(attachment){
            insertComment(c);
            Log.d("comment attachment ", "é anexo a ser inserido");
            oneComment = new OneComment(false, "Anexo", convertDateToTime(c.getDateComment()), convertDateToDate(c.getDateComment()),true);
        }else {
            insertComment(c);
            oneComment = new OneComment(false, edtMessage.getText().toString(), convertDateToTime(c.getDateComment()), convertDateToDate(c.getDateComment()));
        }
        //oneComments.clear();
        oneComments.add(oneComment);
        adapterComments.refresh(oneComments);
        //adapterComments.notifyDataSetChanged();
    }
    public void addAtach(){
        Log.d("comments attach", "add atach selecionado");
        //adapterComments.refresh(oneComments);
        attach=true;
        edtMessage.setText("anexo");
        btGenMess.performClick();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        Log.d("comment attachment ", "entrando no onActivity for Result");
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
//            galleryAddPic();
//            insertFileIntoDataBase(mCurrentPhotoPath, "I");
//        }
//
//        // Quando o usuário escolhe a opção Gallery
//        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK) {
//            Uri selectedUri = data.getData();
//            String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.MIME_TYPE};
//
//            Cursor cursor = getActivity().getContentResolver().query(selectedUri, columns, null, null, null);
//            cursor.moveToFirst();
//
//            int pathColumnIndex = cursor.getColumnIndex(columns[0]);
//            int mimeTypeColumnIndex = cursor.getColumnIndex(columns[1]);
//
//            String contentPath = cursor.getString(pathColumnIndex);
//            String mimeType = cursor.getString(mimeTypeColumnIndex);
//
//            cursor.close();
//
//            mCurrentPhotoPath = contentPath;
//
//            if (mimeType.startsWith("image")) {
//                insertFileIntoDataBase(mCurrentPhotoPath, "I");
//            } else if (mimeType.startsWith("video")) {
//                insertFileIntoDataBase(mCurrentPhotoPath, "V");
//                mCurrentPhotoPath = getThumbnailPathForLocalFile(getActivity(), selectedUri);
//            }
//        }
//
//        if (requestCode == PICKFILE_RESULT_CODE && resultCode == Activity.RESULT_OK) {
//            insertFileIntoDataBase(data.getData().getPath(), "T");
//        }
//
//        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {
//            insertFileIntoDataBase(data.getData().getPath(), "V");
//            galleryAddPic();
////            mCurrentPhotoPath = getThumbnailPathForLocalFile(getActivity(), data.getData());
//        }
        addAtach();
    }

        public void insertAtach(){
            Log.d("comment attachment ", "entrando no insertAtach");
            Comentario c = getCommentFromText();
            insertComment(c);
            OneComment oneComment= new OneComment(false, "Anexo",
                    convertDateToTime(c.getDateComment()),
                    convertDateToDate(c.getDateComment()),true);
            Log.d("comment attachment ", "itens size:" + oneComments.size());
            oneComments.add(oneComment);
            Log.d("comment attachment ", "itens size is now:" + oneComments.size());
            adapterComments.notifyDataSetChanged();
        }




    public Comentario getCommentFromText(){
        Singleton singleton = Singleton.getInstance();
        Comentario c = new Comentario();
        c.setDateComment(getActualTime());
        c.setIdAuthor(singleton.user.getIdUser());
        c.setTypeComment("C");
        c.setTxtComment(edtMessage.getText().toString());
        c.setIdActivityStudent(singleton.activity.getIdAtivity());
        return c;
    }




    public void loadCom(){
        //adapter.clearAdapter();
        DataBaseAdapter db = DataBaseAdapter.getInstance(getActivity());
        Singleton singleton = Singleton.getInstance();
        ArrayList<Comentario> lista = (ArrayList<Comentario>) db.listComments(singleton.activity.getIdAtivity(),"C",0);//lista comentario gerais filtrando por C
        if (lista.size() != 0) {
            for (int i = 0; i < lista.size(); i++) {
                oneComments.add(new OneComment(lista.get(i).getIdAuthor() != singleton.user.getIdUser(),
                        lista.get(i).getTxtComment(),convertDateToTime(lista.get(i).getDateComment()),convertDateToDate(lista.get(i).getDateComment())));
            }
            Log.d("Banco", "Lista populada:" + lista);
        } else {
            Log.d("Banco", "Lista retornou vazia!");
        }
    }



    public void setarListView(){
        lv.setAdapter(adapterComments);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("comments","clicou no item na position:"+position+" conteudo:"+oneComments.get(position));
            }
        });
    }

    private void insertComment(Comentario c) {
//        System.out.println("id:" + c.getIdActivityStudent());
//        System.out.println("comentario inserido:" + c);

        try {
            Singleton single = Singleton.getInstance();
            DataBaseAdapter db = DataBaseAdapter.getInstance(getActivity());
            int lastID = db.insertComment(c);
            if (attach) {
                db.insertAttachComment(lastID, single.lastIdAttach);
            }
            Log.d("Banco:", "comentario inserido no bd interno com sucesso");
        }
        catch (Exception e) {
            Log.e("Erro:", e.getMessage());
        }

//        Log.d("Banco:", c.toJSON().toString());
//        try{
//            HttpClient client = new HttpClient(getActivity().getApplicationContext(),c);
//            System.out.println(c.toJSON().toString());
//            client.postJson(c.toJSON());
//        } catch (Exception e){
//            Log.e("JSON act",e.getMessage());
//        }

    }



    public String getActualTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        return strDate;
    }
    public String convertDateToTime(String atualDate){
        String shortTimeStr="00:00";
        //Log.d("comments","date receiving :"+atualDate);
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            date = df.parse(atualDate);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            shortTimeStr = sdf.format(date);
            //Log.d("comments","date to hour :"+shortTimeStr);
        } catch (ParseException e) {
            // To change body of catch statement use File | Settings | File Templates.
            e.printStackTrace();
        }
        return shortTimeStr;
    }

    public String convertDateToDate(String atualDate){
        String shortTimeStr="00:00";
        //Log.d("comments","date receiving :"+atualDate);
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            date = df.parse(atualDate);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            shortTimeStr = sdf.format(date);
            //Log.d("comments","date to other date format :"+shortTimeStr);
        } catch (ParseException e) {
            // To change body of catch statement use File | Settings | File Templates.
            e.printStackTrace();
        }
        return shortTimeStr;
    }

}
