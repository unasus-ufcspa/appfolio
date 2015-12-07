package com.ufcspa.unasus.appportfolio.Activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ufcspa.unasus.appportfolio.Adapter.CommentArrayAdapter;
import com.ufcspa.unasus.appportfolio.Model.OneComment;
import com.ufcspa.unasus.appportfolio.R;

/**
 * Created by Desenvolvimento on 07/12/2015.
 */
public class FragmentComments extends Fragment {
    private CommentArrayAdapter adapter;
    private ListView lv;
    private Button btGenMess;
    //private LoremIpsum ipsum;
    private EditText editText1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, null);

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
        lv = (ListView) getView().findViewById(R.id.listView1);

        adapter = new CommentArrayAdapter(getActivity().getApplicationContext(), R.layout.comment_item);

        lv.setAdapter(adapter);

        btGenMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRandomItem();
                lv.setAdapter(adapter);
            }
        });
        editText1 = (EditText) getView().findViewById(R.id.editText1);
        editText1.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    adapter.add(new OneComment(false, editText1.getText().toString()));
                    editText1.setText("");
                    return true;
                }
                return false;
            }
        });
        addItems();
    }

    private void addItems() {
        adapter.add(new OneComment(true, "Hello bubbles!"));

        //for (int i = 0; i < 4; i++) {
//			boolean left = getRandomInteger(0, 1) == 0 ? true : false;
//			int word = getRandomInteger(1, 10);
//			int start = getRandomInteger(1, 40);
//			//String words = ipsum.getWords(word, start);
//			String words=""+System.currentTimeMillis();
//			adapter.add(new OneComment(left, words));

        //}
    }

    private void addRandomItem() {
        //boolean left = getRandomInteger(0, 1) == 0 ? true : false;
        //int word = getRandomInteger(1, 10);
        //int start = getRandomInteger(1, 40);
        //String words = ipsum.getWords(word, start);
        boolean orientation = true;
        String words = "" + System.currentTimeMillis();
        adapter.add(new OneComment(orientation, words));
    }
}
