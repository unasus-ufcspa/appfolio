package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.onegravity.rteditor.utils.Constants;
import com.ufcspa.unasus.appportfolio.Adapter.CommentAdapter;
import com.ufcspa.unasus.appportfolio.Adapter.SpecificCommentAdapter;
import com.ufcspa.unasus.appportfolio.Adapter.SpecificCommentArrayAdapter;
import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.Model.Comentario;
import com.ufcspa.unasus.appportfolio.Model.Note;
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
 * Created by icaromsc on 15/02/2016.
 */
public class FragmentSpecificComments extends Frag {

    private static boolean EXPANDED_FLAG = false;
    private SpecificCommentAdapter spcAdapter;
    private ListView lv;
    private boolean attach;
    private Button btGenMess;
    private Button btAttachment;
    //private LoremIpsum ipsum;
    private TextView txNote;
    private EditText edtMessage;
    private String reference;
    private Note noteNow;
    private ArrayList<Comentario> lista;
    private ArrayList<OneComment> oneComments;
    private ImageButton btExpand;
    int lastID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_specific_comment, null);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oneComments = new ArrayList<>(70);
        spcAdapter = new SpecificCommentAdapter(getContext(),oneComments);
        loadCommentsFromDB();
        Log.d("Comments", "On create entrou");
    }

    @Override
    public void onResume() {
        super.onResume();
        btGenMess = (Button) getView().findViewById(R.id.gen_messag_bt);
        btAttachment = (Button) getView().findViewById(R.id.bt_add_attachment);
        edtMessage = (EditText) getView().findViewById(R.id.edtMessage);
        txNote = (TextView) getView().findViewById(R.id.txSelectedNote);
        //btGenMess.setVisibility(View.INVISIBLE);
        btExpand = (ImageButton) getView().findViewById(R.id.btn_expand_ref);

        lv = (ListView) getView().findViewById(R.id.listView1);
        lv.setAdapter(spcAdapter);

        //adapter = new SpecificCommentArrayAdapter(getActivity().getApplicationContext(), R.layout.comment_item);
        loadCommentsFromDB();
        if(oneComments!=null) {

        }
        insertReference();
        ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getView().getWindowToken(), 0);
//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        btGenMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comentario c = getCommentFromText();
                oneComments.add(new OneComment(false, edtMessage.getText().toString(), convertDateToTime(c.getDateComment()), convertDateToDate(c.getDateComment())));
                edtMessage.setText("");
                insertComment(c);
            }
        });
        btExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EXPANDED_FLAG == false) {
                    txNote.getLayoutParams().height = 300;
                    txNote.setMovementMethod(new ScrollingMovementMethod());
                    EXPANDED_FLAG = true;
                    txNote.requestLayout();
                    Log.d("editor", "layout reference expanded true");
                } else {
                    txNote.getLayoutParams().height = 70;
                    txNote.setMovementMethod(null);
                    EXPANDED_FLAG = false;
                    txNote.requestLayout();
                    Log.d("editor", "layout reference expanded false");
                }
            }
        });

        btAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAttachmentToComments();
            }
        });

        setarListView();
    }

    public Comentario getCommentFromText(){
        Singleton singleton = Singleton.getInstance();
        Comentario c = new Comentario();
        c.setDateComment(getActualTime());
        c.setIdAuthor(singleton.user.getIdUser());
        c.setTypeComment("O");
        //Log.d("comments", "reference setting in C spcific comment:" + txNote.getText().toString());
        c.setTxtReference(txNote.getText().toString());
        c.setTxtComment(edtMessage.getText().toString());
        c.setIdActivityStudent(singleton.activity.getIdAtivity());
        return c;
    }


    public void loadCommentsFromDB(){
        try {
            DataBaseAdapter db = DataBaseAdapter.getInstance(getActivity());
            Singleton singleton = Singleton.getInstance();
            lista = (ArrayList<Comentario>) db.listComments(singleton.activity.getIdAtivity(),"O",singleton.note.getBtId());//lista comentario gerais filtrando por O
            oneComments= new ArrayList<>(10);

            if (lista.size() != 0) {

                for(Comentario c : lista){
                    OneComment one = new OneComment(c.getIdAuthor() != singleton.user.getIdUser(),
                            c.getTxtComment(), convertDateToTime(c.getDateComment()), convertDateToDate(c.getDateComment()));
                    if(c.getIdAttach()!=0) {
                        one.atach = true;
                        one.idAttach=c.getIdAttach();
                        Log.d("comments", "id attach:" + one.idAttach);
                    }
                    oneComments.add(one);
                }
//                for (int i = 0; i < lista.size(); i++) {
//                    oneComments.add(new OneComment(lista.get(i).getIdAuthor() != singleton.user.getIdUser(),
//                            lista.get(i).getTxtComment(), convertDateToTime(lista.get(i).getDateComment()), convertDateToDate(lista.get(i).getDateComment())));
//                }
                Log.d("Banco", "Lista populada:" + lista);
            } else {
                Log.d("Banco", "Lista retornou vazia!");
            }
            Log.d("specific comments","one Comments exist, size:"+oneComments.size());
            //spcAdapter = new SpecificCommentAdapter(getActivity(), oneComments);
            //lv.setAdapter(spcAdapter);
            spcAdapter.refresh(oneComments);
            Log.d("specific comments", "adapter itens:" + spcAdapter.getCount());



        } catch (Exception e) {

        }
    }



    public void insertFileIntoDataBase(final String path, final String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Escolha um nome:");

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
                single.lastIdAttach = DataBaseAdapter.getInstance(getActivity()).insertAttachment(new Attachment(0, path, "", type, name, 0));
                if(lastID!=0 && single.lastIdAttach!=-1 && single.lastIdAttach!=0) {
                    DataBaseAdapter.getInstance(getActivity()).insertAttachComment(lastID, single.lastIdAttach);
                }
            }
        });

        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("comment attachment ", "entrando no onActivity for Result");
//
        if(resultCode == Activity.RESULT_OK && requestCode!= Constants.CROP_IMAGE)
            //Log.d("comments","request code:"+requestCode);
            addAtach();
        else
            Log.d("comment attachment ", "attach cancelado");
    }

    public void addAtach(){
        Log.d("comments attach", "add atach selecionado");
        //adapterComments.refresh(oneComments);
        attach=true;
        edtMessage.setText("anexo");
        insertAtach();
    }
    public void insertAtach(){
        Log.d("comment attachment ", "entrando no insertAtach");
        Comentario c = getCommentFromText();
        loadCommentsFromDB();
        insertComment(c);
        OneComment oneComment = new OneComment(false, "Anexo", convertDateToTime(c.getDateComment()), convertDateToDate(c.getDateComment()),true);
        Log.d("comment attachment ", "itens size:" + oneComments.size());
        oneComments.add(oneComment);
        Log.d("comment attachment ", "itens size is now:" + oneComments.size());
        spcAdapter.refresh(oneComments);
        attach = false;
        edtMessage.setText("");
    }



    public void setarListView(){
        lv.setAdapter(spcAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("comments", "clicou no item na position:" + position + " conteudo:" + oneComments.get(position));
                if (oneComments.get(position).idAttach != 0) {
                    Log.d("comments", "selecionou um anexo");
                    Attachment att = DataBaseAdapter.getInstance(getActivity()).getAttachmentByID(oneComments.get(position).idAttach);
                    if (att.getType() != null) {
                        Log.d("comments", "localpath attach:" + att.getLocalPath());
                        if (att.getType().equals(Attachment.TYPE_TEXT)) {
                            Log.d("comments", "anexo do tipo texto");
                            //showPDFDialog(att.getLocalPath());
                            openPDF(att.getLocalPath());
                        } else if (att.getType().equals(Attachment.TYPE_IMAGE)) {
                            Log.d("comments", "anexo do tipo imagem");
                            loadPhoto(att.getLocalPath());
                        } else if (att.getType().equals(Attachment.TYPE_VIDEO)) {
                            Log.d("comments", "anexo do tipo video");
                            loadVideo(att.getLocalPath());
                        }
                    }
                }

            }
        });
    }


    private void insertComment(Comentario c){
        try {
            DataBaseAdapter db = DataBaseAdapter.getInstance(getActivity());
            lastID=db.insertSpecificComment(c, noteNow.getBtId());
            Log.d("Banco:", "comentario inserido no bd interno com sucesso");
        }
        catch (Exception e) {
            Log.e("Banco", "Erro:"+e.getMessage());
        }
        spcAdapter.notifyDataSetChanged();
    }

    private void insertReference(){
        if(spcAdapter!=null) {
            Singleton single = Singleton.getInstance();
            noteNow = single.note;
            if(lista.size()!=0) { // se a lista nao estiver vazia quer dizer que a nota de referencia já existe no banco
                for (Comentario c:lista) {
                    Log.d("comments noteNow","referencia é :"+c.toString());
                }
                noteNow.setSelectedText(lista.get(0).getTxtReference());
                Log.d("comments noteNow","lista:"+lista.get(0).toJSON());
            }


            if(noteNow!=null){
                if (noteNow.getSelectedText()!=null && !noteNow.getSelectedText().toString().equalsIgnoreCase("null")){
                    Log.d("comments","receiving reference...:"+noteNow.getSelectedText());
                    reference=noteNow.getSelectedText();
                    if(reference.contains("Referência:")){
                        Log.d("comments ","specific comments contais referencia 'Referência:' in reference");
                        txNote.setText(reference);
                    }else {
                        txNote.setText("Referência: \n" + "\"" + reference + "\"");
                    }

                }
            }else{
                Log.d("comments","NoteNow is NULL");
            }


        }
    }

    public String getActualTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        return strDate;
    }

    public String convertDateToTime(String atualDate){
        String shortTimeStr="00:00";
        Log.d("comments","date receiving :"+atualDate);
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            date = df.parse(atualDate);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            shortTimeStr = sdf.format(date);
            Log.d("comments","date to hour :"+shortTimeStr);
        } catch (ParseException e) {
            // To change body of catch statement use File | Settings | File Templates.
            e.printStackTrace();
        }
        return shortTimeStr;
    }
    public String convertDateToDate(String atualDate){
        String shortTimeStr="12/12/2012";
        Log.d("comments","date receiving :"+atualDate);
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            date = df.parse(atualDate);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            shortTimeStr = sdf.format(date);
            Log.d("comments","date to other date format :"+shortTimeStr);
        } catch (ParseException e) {
            // To change body of catch statement use File | Settings | File Templates.
            e.printStackTrace();
        }
        return shortTimeStr;
    }





}
