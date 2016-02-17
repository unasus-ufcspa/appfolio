package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ufcspa.unasus.appportfolio.Adapter.CommentArrayAdapter;
import com.ufcspa.unasus.appportfolio.Adapter.SpecificCommentArrayAdapter;
import com.ufcspa.unasus.appportfolio.Model.Note;
import com.ufcspa.unasus.appportfolio.Model.OneComment;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

/**
 * Created by icaromsc on 15/02/2016.
 */
public class FragmentSpecificComments extends Frag {

    private SpecificCommentArrayAdapter adapter;
    private ListView lv;
    private Button btGenMess;
    private Button btAttachment;
    //private LoremIpsum ipsum;
    private EditText edtMessage;
    private Note noteNow;



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

        //btGenMess.setVisibility(View.INVISIBLE);

        lv = (ListView) getView().findViewById(R.id.listView1);

        adapter = new SpecificCommentArrayAdapter(getActivity().getApplicationContext(), R.layout.comment_item);

        lv.setAdapter(adapter);

        Singleton single= Singleton.getInstance();
        noteNow=single.note;

        edtMessage.setText("\"" + noteNow.getSelectedText() + "\"");
//        edtMessage.setFocusable(true);
        //edtMessage.setShowSoftInputOnFocus(true);
        edtMessage.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        btGenMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertComment();
            }
        });
    }

    private void insertComment(){
        adapter.add(new OneComment(false, edtMessage.getText().toString()));
    }





}
