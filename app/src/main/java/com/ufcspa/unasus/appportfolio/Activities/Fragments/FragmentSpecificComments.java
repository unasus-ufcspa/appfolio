package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Adapter.CommentArrayAdapter;
import com.ufcspa.unasus.appportfolio.Adapter.SpecificCommentArrayAdapter;
import com.ufcspa.unasus.appportfolio.Model.Comentario;
import com.ufcspa.unasus.appportfolio.Model.Note;
import com.ufcspa.unasus.appportfolio.Model.OneComment;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by icaromsc on 15/02/2016.
 */
public class FragmentSpecificComments extends Frag {

    private SpecificCommentArrayAdapter adapter;
    private ListView lv;
    private Button btGenMess;
    private Button btAttachment;
    //private LoremIpsum ipsum;
    private TextView txNote;
    private EditText edtMessage;
    private String reference;
    private Note noteNow;
    private ImageButton btExpand;
    private static boolean EXPANDED_FLAG=false;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_specific_comment, null);
        return view;
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

        adapter = new SpecificCommentArrayAdapter(getActivity().getApplicationContext(), R.layout.comment_item);

        lv.setAdapter(adapter);

        Singleton single= Singleton.getInstance();
        noteNow=single.note;
        insertReference();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        btGenMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertComment(getCommentFromText());
            }
        });
        btExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EXPANDED_FLAG==false) {
                    txNote.getLayoutParams().height=300;
                    txNote.setMovementMethod(new ScrollingMovementMethod());
                    EXPANDED_FLAG=true;
                    txNote.requestLayout();
                    Log.d("editor","flag true");
                }else{
                    txNote.getLayoutParams().height=70;
                    txNote.setMovementMethod(null);
                    EXPANDED_FLAG=false;
                    txNote.requestLayout();
                    Log.d("editor", "flag false");
                }
            }
        });
    }


    public Comentario getCommentFromText(){
        Singleton singleton = Singleton.getInstance();
        Comentario c = new Comentario();
        c.setDateComment(getActualTime());
        c.setIdAuthor(singleton.user.getIdUser());
        c.setTypeComment("O");
        c.setTxtComment(edtMessage.getText().toString());
        c.setIdActivityStudent(singleton.activity.getIdAtivity());
        return c;
    }

    private void insertComment(Comentario c){
        adapter.add(new OneComment(false, edtMessage.getText().toString()));
        edtMessage.setText("");
        try {
            DataBaseAdapter db = DataBaseAdapter.getInstance(getActivity());
            db.insertComment(c);
            Log.d("Banco:", "comentario inserido no bd interno com sucesso");
        }
        catch (Exception e) {
            Log.e("Erro:", e.getMessage());
        }
    }

    private void insertReference(){
        if(adapter!=null) {
            Singleton single = Singleton.getInstance();
            noteNow = single.note;
            if (!noteNow.getSelectedText().equalsIgnoreCase("null")){
                reference=noteNow.getSelectedText();
                txNote.setText("Referência: \n"+"\""+reference+"\"");
            }
        }
    }

    public String getActualTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        return strDate;
    }





}
