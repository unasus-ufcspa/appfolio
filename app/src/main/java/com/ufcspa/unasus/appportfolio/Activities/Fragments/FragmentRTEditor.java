package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.onegravity.rteditor.RTEditText;
import com.onegravity.rteditor.RTManager;
import com.onegravity.rteditor.RTToolbar;
import com.onegravity.rteditor.api.RTApi;
import com.onegravity.rteditor.api.RTMediaFactoryImpl;
import com.onegravity.rteditor.api.RTProxyImpl;
import com.onegravity.rteditor.api.format.RTFormat;
import com.onegravity.rteditor.effects.Effects;
import com.ufcspa.unasus.appportfolio.Activities.MainActivity;
import com.ufcspa.unasus.appportfolio.Model.Note;
import com.ufcspa.unasus.appportfolio.R;

import java.util.ArrayList;


public class FragmentRTEditor extends Fragment {
    private RTManager mRTManager;
    private RTEditText mRTMessageField;
    private RTToolbar rtToolbar;
    private int currentSpecificComment;
    private ViewGroup scrollview;
    private ImageButton fullScreen;

    private ArrayList<Note> specificCommentsNotes;

    // View lateral (Comentário específico / Comentário geral)
    private RelativeLayout slider;
    private Animation animLeft;
    private Animation animRight;
    private boolean visible;

    public FragmentRTEditor() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rteditor, null);

        specificCommentsNotes = new ArrayList<>();

        scrollview = (ViewGroup) view.findViewById(R.id.comments);

        // create RTManager
        RTApi rtApi = new RTApi(getActivity(), new RTProxyImpl(getActivity()), new RTMediaFactoryImpl(getActivity(), true));

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

        fullScreen = (ImageButton) view.findViewById(R.id.fullscreen);
        fullScreen.setOnClickListener(new FullScreen());

        initCommentsTab(view);

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

    private void initCommentsTab(View view)
    {
        slider = (RelativeLayout) view.findViewById(R.id.slider);
        slider.setVisibility(View.GONE);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        slider.getLayoutParams().width = width / 2;
        slider.requestLayout();
        slider.bringToFront();

        ImageButton geral = (ImageButton)view.findViewById(R.id.btn_geral);
        ImageButton specific = (ImageButton)view.findViewById(R.id.btn_specific);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.comments_container, new FragmentComments()).commit();

        geral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.comments_container, new FragmentComments()).commit();
            }
        });

        specific.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.comments_container, new FragRef()).commit();
            }
        });

        animLeft = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_right);
        animRight = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_left);

        visible = false;
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

    private void findText(String selTxt,String texto){
        String tag="Processing text:";
        Log.d(tag,"finding selected text:"+selTxt +"\n in text:"+texto);
        int start=texto.indexOf(selTxt);
        int end =start+selTxt.length();
        Log.d(tag,"selected text starts at position '"+start+"' ends at position '"+end+"'");
        Log.d(tag,"substring generated:"+texto.substring(start,end));
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

    private Button createButton(int id, String value, float yPosition) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Button note = new Button(getContext());
        note = (Button) inflater.inflate(R.layout.btn_specific_comment, scrollview, false);

        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentsTab(true);

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
                    String selectedText = mRTMessageField.getText(RTFormat.HTML).substring(startSelection, endSelection);

                    if (!selectedText.isEmpty()) {
                        if (selectedText.length() > 0) {
                            findText(selectedText, mRTMessageField.getText(RTFormat.HTML));
                            createSpecificCommentNote(getCaretYPosition(startSelection), selectedText);
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

        private void createSpecificCommentNote(float yPosition, String selectedText) {

            currentSpecificComment++;

            float yButton = 0;
            int idButton = -1;

            if (yPosition != 0)
                yButton = yPosition - 2;

            idButton = currentSpecificComment;

            specificCommentsNotes.add(new Note(idButton, selectedText, yButton));

            scrollview.addView(createButton(idButton, String.valueOf(currentSpecificComment), yButton));

            mRTManager.onEffectSelected(Effects.BGCOLOR, getResources().getColor(R.color.base_green), idButton);
            mRTMessageField.setSelection(endSelection);
            mRTMessageField.setSelected(false);
        }
    }

    private class FullScreen implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            Log.d("rteditor", mRTMessageField.getText(RTFormat.HTML));
        }
    }

    public void showCommentsTab(Boolean isSpecificComment)
    {
        if(isSpecificComment)
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.comments_container, new FragRef()).commit();
        else
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.comments_container, new FragmentComments()).commit();

        if(!visible)
        {
            slider.setVisibility(View.VISIBLE);
            slider.startAnimation(animLeft);
            visible = true;
        }
        else
        {
            slider.startAnimation(animRight);
            slider.setVisibility(View.GONE);
            visible = false;
        }
    }
}
