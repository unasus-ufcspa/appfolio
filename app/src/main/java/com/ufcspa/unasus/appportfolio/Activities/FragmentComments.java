package com.ufcspa.unasus.appportfolio.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.ufcspa.unasus.appportfolio.Adapter.CommentArrayAdapter;
import com.ufcspa.unasus.appportfolio.Model.Comentario;
import com.ufcspa.unasus.appportfolio.Model.HttpClient;
import com.ufcspa.unasus.appportfolio.Model.OneComment;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Desenvolvimento on 07/12/2015.
 */
public class FragmentComments extends Frag {
    private CommentArrayAdapter adapter;
    private ListView lv;
    private Button btGenMess;
    private Button btAttachment;
    //private LoremIpsum ipsum;
    private EditText editText1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, null);

        source = new DataBaseAdapter(getActivity());

        singleton = Singleton.getInstance();
        singleton.idActivityStudent = source.getActivityStudentID(singleton.activity.getIdAtivity(), singleton.portfolioClass.getIdPortfolioStudent());


        //btGenMess.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addRandomItem();
//                lv.setAdapter(adapter);
//            }
//        });
        //ipsum = new LoremIpsum();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        btGenMess = (Button) getView().findViewById(R.id.gen_messag_bt);
        btAttachment = (Button) getView().findViewById(R.id.bt_add_attachment);

        //btGenMess.setVisibility(View.INVISIBLE);

        lv = (ListView) getView().findViewById(R.id.listView1);

        adapter = new CommentArrayAdapter(getActivity().getApplicationContext(), R.layout.comment_item);

        lv.setAdapter(adapter);

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
                addItems();
                Singleton singleton = Singleton.getInstance();
                Comentario c = new Comentario();
                c.setDateComment(getActualTime());
                c.setIdAuthor(singleton.user.getIdUser());
                c.setTypeComment("C");
                c.setTxtComment(editText1.getText().toString());
                c.setIdActivityStudent(singleton.activity.getIdAtivity());
                insertComment(c);
                adapter.add(new OneComment(false, editText1.getText().toString()));
                editText1.setText("");
            }
        });
        editText1 = (EditText) getView().findViewById(R.id.editText1);
        editText1.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    return true;
                }
                return false;
            }
        });
        addItems();
    }

    public void addItems() {
        //adapter.add(new OneComment(true, "Hello bubbles!"));
        try {
            DataBaseAdapter db = new DataBaseAdapter(getActivity());
            Singleton singleton = Singleton.getInstance();
            ArrayList<Comentario> lista = (ArrayList<Comentario>) db.listComments(singleton.activity.getIdAtivity());
            if (lista.size() != 0) {
                for (int i = 0; i < lista.size(); i++) {
                    adapter.add(new OneComment(lista.get(i).getIdAuthor() != singleton.user.getIdUser(),
                            lista.get(i).getTxtComment() + "\n" + lista.get(i).getDateComment()));
                }
                Log.d("Banco", "Lista populada:" + lista);
            } else {
                Log.d("Banco", "Lista retornou vazia!");
            }
        } catch (Exception e) {

        }
        //for (int i = 0; i < 4; i++) {
//			boolean left = getRandomInteger(0, 1) == 0 ? true : false;
//			int word = getRandomInteger(1, 10);
//			int start = getRandomInteger(1, 40);
//			//String words = ipsum.getWords(word, start);
//			String words=""+System.currentTimeMillis();
//			adapter.add(new OneComment(left, words));

        //}
        lv.setAdapter(adapter);
    }

    private void insertComment(Comentario c) {
//        System.out.println("id:" + c.getIdActivityStudent());
//        System.out.println("comentario inserido:" + c);
        try{
            HttpClient client = new HttpClient(getActivity().getApplicationContext(),c);
            System.out.println(c.toJSON().toString());
            client.postJson(c.toJSON());
            //Log.d("Banco:", "comentario enviado ao bd externo com sucesso");
        }
        catch (Exception e){
            Log.e("JSON act",e.getMessage());
        }

        try {
            DataBaseAdapter db = new DataBaseAdapter(getActivity());
            db.insertComment(c);
            Log.d("Banco:", "comentario inserido no bd interno com sucesso");
        }
        catch (Exception e) {
            Log.e("Erro:", e.getMessage());
        }
    }

    private void addRandomItem() {
        //boolean left = getRandomInteger(0, 1) == 0 ? true : false;
        //int word = getRandomInteger(1, 10);
        //int start = getRandomInteger(1, 40);
        //String words = ipsum.getWords(word, start);
        boolean orientation = true;
        String words = "" + System.currentTimeMillis();
        words += "\n" + getActualTime();
        adapter.add(new OneComment(orientation, words));
    }

    public String getActualTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        return strDate;
    }
}
