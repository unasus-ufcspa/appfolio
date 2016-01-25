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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.onegravity.rteditor.RTEditText;
import com.onegravity.rteditor.RTManager;
import com.onegravity.rteditor.RTToolbar;
import com.onegravity.rteditor.api.RTApi;
import com.onegravity.rteditor.api.RTProxyImpl;
import com.onegravity.rteditor.effects.Effects;
import com.ufcspa.unasus.appportfolio.Model.NewRTMediaFactoryImpl;
import com.ufcspa.unasus.appportfolio.Model.Note;
import com.ufcspa.unasus.appportfolio.R;

import java.util.ArrayList;


public class FragmentRTEditor extends Fragment {
    private RTManager mRTManager;
    private RTEditText mRTMessageField;
    private RTToolbar rtToolbar;
    private int currentSpecificComment;
    private ViewGroup scrollview;

    private ArrayList<Note> specificCommentsNotes;

    public FragmentRTEditor() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rteditor, null);

        specificCommentsNotes = new ArrayList<>();

        scrollview = (ViewGroup) view.findViewById(R.id.comments);

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
            private float posStart;
            private float posEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                posStart = getCaretYPosition(mRTMessageField.getSelectionStart());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                posEnd = getCaretYPosition(mRTMessageField.getSelectionEnd());
                changePositionOfNotes(posStart, posEnd);
            }
        });

        currentSpecificComment = 0;

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mRTManager.onSaveInstanceState(outState);
        outState.putInt("currentSpecificComment", currentSpecificComment);
        outState.putSerializable("specificCommentsNotes", specificCommentsNotes);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.getSerializable("specificCommentsNotes") != null) {
                specificCommentsNotes = (ArrayList<Note>) savedInstanceState.getSerializable("specificCommentsNotes");

                for (int i = 0; i < specificCommentsNotes.size(); i++) {
                    Note aux = specificCommentsNotes.get(i);
                    scrollview.addView(createButton(aux.getBtId(), String.valueOf(aux.getBtId()), aux.getBtY()));
                }

                if (specificCommentsNotes.size() > 0)
                    createMarginForRTEditor();
            }

            currentSpecificComment = savedInstanceState.getInt("currentSpecificComment", -1);
        }
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
            float posStart = getCaretYPosition(mRTMessageField.getSelectionStart());
            mRTManager.onActivityResult(requestCode, resultCode, data);
            float posEnd = getCaretYPosition(mRTMessageField.getSelectionEnd());
            changePositionOfNotes(posStart,posEnd);
        }
        else if(resultCode == Activity.RESULT_CANCELED)
        {
        }
    }

    private float getCaretYPosition(int position) {
        Layout layout = mRTMessageField.getLayout();
        if (layout != null) {
            int line = layout.getLineForOffset(position);
            int baseline = layout.getLineBaseline(line);
            int ascent = layout.getLineAscent(line);
            return baseline + ascent;
        }
        return 0;
    }

    private void changePositionOfNotes(float posStart, float posEnd) {
        if (specificCommentsNotes != null) {
            for (int i = 0; i < specificCommentsNotes.size(); i++) {
                Button aux = (Button) getView().findViewById(specificCommentsNotes.get(i).getBtId());
                if (aux != null && aux.getY() > posStart) {
                    aux.setY(aux.getY() - (posStart - posEnd));
                    specificCommentsNotes.get(i).setBtY(aux.getY());
                }
            }
        }
    }

    private void createMarginForRTEditor() {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mRTMessageField.getLayoutParams();
        params.leftMargin = 70;
        mRTMessageField.setLayoutParams(params);
    }

    private Button createButton(int id, String value, float yPosition) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Button note = new Button(getContext());
        note = (Button) inflater.inflate(R.layout.btn_specific_comment, scrollview, false);

        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Abrir aba de comentário específico!", Toast.LENGTH_SHORT).show();

                Button btn = (Button) v;
                btn.setBackgroundResource(R.drawable.rounded_corner);
                btn.setTextColor(Color.WHITE);

                for (int i = 0; i < specificCommentsNotes.size(); i++) {
                    Button aux = (Button) getView().findViewById(specificCommentsNotes.get(i).getBtId());
                    if (aux.getId() != btn.getId()) {
                        aux.setBackgroundResource(R.drawable.btn_border);
                        aux.setTextColor(getResources().getColor(R.color.base_green));
                    }
                }
            }
        });

        note.setY(yPosition);
        note.setText(value);
        note.setId(id);

        return note;
    }

    private class ActionBarCallBack implements ActionMode.Callback {

        int startSelection;
        int endSelection;

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.action_favorite)
            {
                if (!mRTMessageField.getText().toString().isEmpty()) {
                    startSelection = mRTMessageField.getSelectionStart();
                    endSelection = mRTMessageField.getSelectionEnd();
                    String selectedText = mRTMessageField.getText().toString().substring(startSelection, endSelection);

                    if (!selectedText.isEmpty()) {
                        if (selectedText.length() > 0) {
                            createSpecificCommentNote(getCaretYPosition(startSelection), selectedText);
                            mRTManager.onEffectSelected(Effects.BGCOLOR, getResources().getColor(R.color.base_green));
                            mRTMessageField.setSelection(endSelection);
                            mRTMessageField.setSelected(false);
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
            Effects.BGCOLOR.applyToSelection(mRTMessageField, null);
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        private void createSpecificCommentNote(float yPosition, String selectedText) {

            currentSpecificComment++;

            float yButton = 0;
            int idButton = -1;

            if (yPosition != 0)
                yButton = yPosition - 2;

            String.valueOf(currentSpecificComment);
            idButton = currentSpecificComment;

            specificCommentsNotes.add(new Note(idButton, selectedText, yButton));

            scrollview.addView(createButton(idButton, String.valueOf(currentSpecificComment), yButton));

            createMarginForRTEditor();
        }
    }
}
