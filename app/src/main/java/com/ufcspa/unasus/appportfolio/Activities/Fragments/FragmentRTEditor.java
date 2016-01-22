package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.onegravity.rteditor.RTEditText;
import com.onegravity.rteditor.RTManager;
import com.onegravity.rteditor.RTToolbar;
import com.onegravity.rteditor.api.RTApi;
import com.onegravity.rteditor.api.RTProxyImpl;
import com.onegravity.rteditor.api.format.RTFormat;
import com.ufcspa.unasus.appportfolio.Model.NewRTMediaFactoryImpl;
import com.ufcspa.unasus.appportfolio.R;

import java.sql.SQLOutput;
import java.util.ArrayList;


public class FragmentRTEditor extends Fragment {
    private RTManager mRTManager;
    private RTEditText mRTMessageField;
    private RTToolbar rtToolbar;

    private ArrayList<Button> specificCommentsNotes;

    public FragmentRTEditor() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rteditor, null);

        specificCommentsNotes = new ArrayList<>();

        // create RTManager
        RTApi rtApi = new RTApi(getActivity(), new RTProxyImpl(getActivity()), new NewRTMediaFactoryImpl(getActivity(), true));

        mRTManager = new RTManager(rtApi, savedInstanceState);

        ViewGroup toolbarContainer = (ViewGroup) view.findViewById(R.id.rte_toolbar_container);

        // register toolbar (if it exists)
        rtToolbar = (RTToolbar) view.findViewById(R.id.rte_toolbar);
        if (rtToolbar != null) {
            mRTManager.registerToolbar(toolbarContainer, rtToolbar);
        }
        // register message editor
        mRTMessageField = (RTEditText) view.findViewById(R.id.rtEditText);
        mRTManager.registerEditor(mRTMessageField, true);

        mRTMessageField.setCustomSelectionActionModeCallback(new ActionBarCallBack());

        mRTMessageField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                System.out.println("beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("onTextChanged");
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("afterTextChanged");
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mRTManager.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRTManager != null) {
            mRTManager.onDestroy(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK)
        {
            mRTManager.onActivityResult(requestCode, resultCode, data);
        }
        else if(resultCode == Activity.RESULT_CANCELED)
        {
        }
    }

    class ActionBarCallBack implements ActionMode.Callback {

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.action_favorite)
            {
                if(!mRTMessageField.getText().toString().isEmpty())
                {
                    int startSelection = mRTMessageField.getSelectionStart();
                    int endSelection = mRTMessageField.getSelectionEnd();
                    String selectedText = mRTMessageField.getText().toString().substring(startSelection, endSelection);

//                    mRTMessageField.setRichTextEditing(true, "<b" + mRTMessageField.getText(RTFormat.PLAIN_TEXT).substring(startSelection,endSelection) + "/>");

                    if(!selectedText.isEmpty()){
                        if(selectedText.length() > 0)
                        {
                            Layout layout = mRTMessageField.getLayout();
                            int line = layout.getLineForOffset(startSelection);
                            int baseline = layout.getLineBaseline(line);
                            int ascent = layout.getLineAscent(line);
                            float y = baseline + ascent;

                            createSpecificCommentNote(y);
                        }
                    }
                }
                return true;
            }
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            menu.clear();
            mode.getMenuInflater().inflate(R.menu.menu, menu);
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        private void createSpecificCommentNote(float yPosition)
        {
            ViewGroup scrollview = (ViewGroup) getView().findViewById(R.id.comments);

            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Button note = new Button(getContext());
            note = (Button) inflater.inflate(R.layout.btn_specific_comment, scrollview, false);

            specificCommentsNotes.add(note);

            if(yPosition != 0)
                note.setY(yPosition - 2);
            note.setText(specificCommentsNotes.size() + "");
            note.setId(specificCommentsNotes.size());

            note.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Abrir aba de comentário específico!", Toast.LENGTH_SHORT).show();
                    Button btn = (Button) v;
                    btn.setBackgroundResource(R.drawable.rounded_corner);
                    btn.setTextColor(Color.WHITE);

                    for (int i = 0; i < specificCommentsNotes.size(); i++) {
                        Button aux = specificCommentsNotes.get(i);
                        if (aux.getId() != btn.getId()) {
                            aux.setBackgroundResource(R.drawable.btn_border);
                            aux.setTextColor(getResources().getColor(R.color.base_green));
                        }
                    }
                }
            });

            scrollview.addView(note);

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mRTMessageField.getLayoutParams();
            params.leftMargin = 60;
            mRTMessageField.setLayoutParams(params);
        }
    }
}
