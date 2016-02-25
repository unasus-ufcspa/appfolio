package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SlidingPaneLayout;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onegravity.rteditor.RTEditText;
import com.onegravity.rteditor.RTManager;
import com.onegravity.rteditor.RTToolbar;
import com.onegravity.rteditor.api.RTApi;
import com.onegravity.rteditor.api.RTMediaFactoryImpl;
import com.onegravity.rteditor.api.RTProxyImpl;
import com.onegravity.rteditor.api.format.RTFormat;
import com.onegravity.rteditor.api.format.RTHtml;
import com.onegravity.rteditor.api.media.RTAudio;
import com.onegravity.rteditor.api.media.RTImage;
import com.onegravity.rteditor.api.media.RTVideo;
import com.onegravity.rteditor.converter.ConverterSpannedToHtml;
import com.onegravity.rteditor.effects.Effects;
import com.onegravity.rteditor.spans.BackgroundColorSpan;
import com.ufcspa.unasus.appportfolio.Activities.MainActivity;
import com.ufcspa.unasus.appportfolio.Model.ActivityStudent;
import com.ufcspa.unasus.appportfolio.Model.Note;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FragmentRTEditor extends Fragment {
    private RTManager mRTManager;
    private RTEditText mRTMessageField;
    private RTApi rtApi;
    private RTToolbar rtToolbar;
    private int currentSpecificComment;
    private ViewGroup scrollview;
    private ImageButton fullScreen;
    private ActivityStudent acStudent;
    private DataBaseAdapter source;
    private HashMap<Integer,Note> specificCommentsNotes;
    private String selectedActualText = "null";

    private RelativeLayout slider;

    private int greenLight;
    private int greenDark;

    private Singleton singleton;

    public FragmentRTEditor() {}

    public void loadLastText() {
        Singleton singleton = Singleton.getInstance();
        source = DataBaseAdapter.getInstance(getActivity());
        acStudent = source.listActivityStudent(singleton.idActivityStudent);
        mRTMessageField.setRichTextEditing(true, acStudent.getTxtActivity());
    }

    public void saveText() {
        Log.d("editor DB","salvando texto..");
        acStudent.toString();
        source = DataBaseAdapter.getInstance(getActivity());
        acStudent.setTxtActivity(mRTMessageField.getText(RTFormat.HTML));
        source.updateActivityStudent(acStudent);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.hasExtra("URL"))
            {
                String url = intent.getStringExtra("URL");
                String type = intent.getStringExtra("Type");

                switch (type) {
                    case "I":
//                        mRTManager.insertImage(rtApi.createImage(url));
                        mRTMessageField.setRichTextEditing(true, "<img src=\"" + url + "\">");
                        break;
                    case "V":
//                        mRTManager.insertVideo(rtApi.createVideo(url));
                        mRTMessageField.setRichTextEditing(true,"<img src=\""+url+"\">");
                        break;
                    case "T":
                        break;
                    default:
                        break;
                }
            }
            else
            {
                singleton.isRTEditor = true;
                FragmentAttachment fragmentAttachment = new FragmentAttachment();
                if (getActivity() != null)
                    fragmentAttachment.show(getActivity().getSupportFragmentManager(), "Anexos");
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rteditor, null);

        singleton = Singleton.getInstance();

        greenLight = getResources().getColor(R.color.base_green_light);
        greenDark = getResources().getColor(R.color.base_green);

        specificCommentsNotes = new HashMap<>();
        getIdNotesFromDB();

        scrollview = (ViewGroup) view.findViewById(R.id.comments);

        // create RTManager
        rtApi = new RTApi(getActivity(), new RTProxyImpl(getActivity()), new RTMediaFactoryImpl(getActivity(), true));

        mRTManager = new RTManager(rtApi, savedInstanceState, getContext());

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
        loadLastText();

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
                changeNotePosition();
            }
        });

        mRTMessageField.post(new Runnable() {
            @Override
            public void run() {
                noteFollowText();
            }
        });

        mRTMessageField.setLineSpacing(15, 1);

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.info_rteditor_container);
        layout.clearFocus();

        fullScreen = (ImageButton) view.findViewById(R.id.fullscreen);
        fullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("rteditor", mRTMessageField.getText(RTFormat.HTML));
//                ((MainActivity) getActivity()).hideDrawer();
            }
        });

        initCommentsTab(view);
        initTopBar(view);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, new IntentFilter("call.attachments.action"));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
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
                specificCommentsNotes = (HashMap<Integer,Note>) savedInstanceState.getSerializable("specificCommentsNotes" );
            }
            currentSpecificComment = savedInstanceState.getInt("currentSpecificComment", -1);
        }
    }

    @Override
    public void onDestroy() {
        saveText();
        super.onDestroy();
        if (mRTManager != null) {
            mRTManager.onDestroy(true);
        }
    }

    private void initCommentsTab(View view)
    {
        slider = (RelativeLayout) view.findViewById(R.id.slider);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? dm.widthPixels / 4 : dm.widthPixels / 3;

        SlidingPaneLayout.LayoutParams relativeParams = new SlidingPaneLayout.LayoutParams(new SlidingPaneLayout.LayoutParams(SlidingPaneLayout.LayoutParams.MATCH_PARENT, SlidingPaneLayout.LayoutParams.MATCH_PARENT));
        relativeParams.setMargins(width, 0, 0, 0);
        slider.setLayoutParams(relativeParams);

        slider.requestLayout();
        slider.bringToFront();

        TextView geral = (TextView)view.findViewById(R.id.btn_geral);
        TextView specific = (TextView)view.findViewById(R.id.btn_specific);

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
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.comments_container, new FragmentSpecificComments()).commit();
            }
        });

        SlidingPaneLayout layout = (SlidingPaneLayout) view.findViewById(R.id.rteditor_fragment);
        layout.setSliderFadeColor(Color.TRANSPARENT);
        layout.setBackgroundColor(Color.TRANSPARENT);

        layout.openPane();
    }

    private void initTopBar(View view) {
        TextView studentName = (TextView) view.findViewById(R.id.student_name);
        TextView activityName = (TextView) view.findViewById(R.id.activity_name);

//        studentName.setText(singleton.portfolioClass.getStudentName());
        activityName.setText("Atividade " + singleton.activity.getNuOrder() + ": " + singleton.activity.getTitle());
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


    public void getIdNotesFromDB(){
        DataBaseAdapter data= DataBaseAdapter.getInstance(getActivity());
        Singleton single= Singleton.getInstance();
        ArrayList<Integer>ids = (ArrayList<Integer>) data.listSpecificComments(single.idActivityStudent);
        for(int id : ids){
            specificCommentsNotes.put(id,new Note(id,"",0));
        }
        currentSpecificComment=specificCommentsNotes.size();
        Log.d("editor notes","currentSpecificComment:"+currentSpecificComment);
    }

    private Button createButton(final int id, final String value, final float yPosition) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Button note = (Button) inflater.inflate(R.layout.btn_specific_comment, scrollview, false);

        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(id);

                Button btn = (Button) v;
                btn.setBackgroundResource(R.drawable.rounded_corner);
                btn.setTextColor(Color.WHITE);

                ArrayList<Note> arrayAux = new ArrayList<>(specificCommentsNotes.values());

                for (int i = 0; i < arrayAux.size(); i++) {
                    Button aux = (Button) getView().findViewById(arrayAux.get(i).getBtId());
                    if (aux.getId() != btn.getId()) {
                        aux.setBackgroundResource(R.drawable.btn_border);
                        aux.setTextColor(greenDark);
                    }
                }

                Singleton single = Singleton.getInstance();
                single.note.setBtY(yPosition);
                single.note.setSelectedText(selectedActualText);
                single.note.setBtId(id);
                Log.d("editor", "button id in singleton is now:"+single.note.getBtId());
                showCommentsTab(true);
            }
        });

        note.setY(yPosition);
        note.setX(5);
        note.setText(value);
        note.setId(id);

        return note;
    }

    private String getSelectedText(){
        int selStart = mRTMessageField.getSelectionStart();
        int selEnd = mRTMessageField.getSelectionEnd();
        Spannable text = (Spannable) mRTMessageField.getText().subSequence(selStart, selEnd);
        RTHtml<RTImage, RTAudio, RTVideo> rtHtml = new ConverterSpannedToHtml().convert(text, RTFormat.HTML);
        String thatsMySelectionInHTML = rtHtml.getText();
        return thatsMySelectionInHTML;
    }

    private void changeColor(int id)
    {
        Spannable textSpanned = (Spannable) mRTMessageField.getText();
        BackgroundColorSpan[] spans = textSpanned.getSpans(0, textSpanned.length(), BackgroundColorSpan.class);

        BackgroundColorSpan aux = null;
        int auxStart = 0;
        int auxEnd = 0;

        for(BackgroundColorSpan spm : spans)
        {
            if(spm.getId() == id)
            {
                aux = spm;
                auxStart = textSpanned.getSpanStart(spm);
                auxEnd = textSpanned.getSpanEnd(spm);
            }
            else {
                if (spm.getId() != -1 && spm.getBackgroundColor() != greenLight)
                    spm.setColor(greenLight);
            }
        }

        if(aux != null)
        {
            textSpanned.removeSpan(aux);
            aux.setColor(greenDark);
            textSpanned.setSpan(aux, auxStart, auxEnd, 1);
        }
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
                    String selectedText = getSelectedText();
                    //mRTMessageField.getText(RTFormat.HTML).substring(startSelection, endSelection);

                    if (!selectedText.isEmpty()) {
                        if (selectedText.length() > 0) {
                            //findText(selectedText, mRTMessageField.getText(RTFormat.HTML));
                            createSpecificCommentNote(getCaretYPosition(startSelection), selectedText);
                            Singleton single=Singleton.getInstance();
                            single.selectedText = mRTMessageField.getText().toString().substring(startSelection,endSelection);
                            DataBaseAdapter data = DataBaseAdapter.getInstance(getActivity());
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
            Log.d("editor ","current note is now:"+currentSpecificComment);
            float yButton = 0;
            int idButton = -1;

            if (yPosition != 0)
                yButton = yPosition - 2;

            idButton = currentSpecificComment;
            
            selectedActualText = selectedText;
            specificCommentsNotes.put(idButton, new Note(idButton, selectedText, yButton));

            scrollview.addView(createButton(idButton, String.valueOf(currentSpecificComment), yButton));

            mRTManager.onEffectSelected(Effects.BGCOLOR, greenLight, idButton);
            mRTMessageField.setSelection(endSelection);
            mRTMessageField.setSelected(false);
        }
    }

    public void showCommentsTab(Boolean isSpecificComment)
    {
        if(isSpecificComment)
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.comments_container, new FragmentSpecificComments()).commit();
        else
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.comments_container, new FragmentComments()).commit();
    }

    private void changeNotePosition()
    {
        if(specificCommentsNotes != null && specificCommentsNotes.size() != 0) {
            Spannable textSpanned = (Spannable) mRTMessageField.getText();
            BackgroundColorSpan[] spans = textSpanned.getSpans(0, textSpanned.length(), BackgroundColorSpan.class);

            for (BackgroundColorSpan spm : spans) {
                if (spm.getId() != -1) {
                    Note aux = specificCommentsNotes.get(spm.getId());
                    float bcsPosition = getCaretYPosition(textSpanned.getSpanStart(spm));
                    if (bcsPosition != aux.getBtY()) {
                        aux.setBtY(bcsPosition);
                        Button btn = (Button) scrollview.findViewById(aux.getBtId());
                        btn.setY(bcsPosition);
                    }
                }
            }
        }
    }

    private void noteFollowText()
    {
        Spannable textSpanned = (Spannable) mRTMessageField.getText();
        BackgroundColorSpan[] spans = textSpanned.getSpans(0, textSpanned.length(), BackgroundColorSpan.class);

        for(BackgroundColorSpan spm : spans)
        {
            if(spm.getId() != -1)
            {
                Note aux = specificCommentsNotes.get(spm.getId());
                aux.setBtY(getCaretYPosition(textSpanned.getSpanStart(spm)));
            }
        }


        ArrayList<Note> arrayAux = new ArrayList<>(specificCommentsNotes.values());

        for (int i = 0; i < arrayAux.size(); i++) {
            Note aux = arrayAux.get(i);
            scrollview.addView(createButton(aux.getBtId(), String.valueOf(aux.getBtId()), aux.getBtY()));
        }
    }
/*

    private int findText(String selTxt,String texto){
        String tag="Processing text:";
        Log.d(tag,"finding selected text:"+selTxt +"\n in text:"+texto);
        int start=texto.indexOf(selTxt);
        int end =start+selTxt.length();
        Log.d(tag,"selected text starts at position '"+start+"' ends at position '"+end+"'");
        Log.d(tag,"substring generated:"+texto.substring(start,end));
        return start;
    }


    public String changeColor(String text,String tag,String color){
        int start=text.indexOf("#",text.indexOf(tag));
        int end=start+7;
        Log.d("changeColor","text:"+text);
        Log.d("changeColor","cor:"+color);
        Log.d("changeColor","encontrou # na posição:"+start);
        String textWChColor=text;
        Log.d("changeColor","bg color in text:"+text.subSequence(start,start+7));
//        int j=1;
//        for (int i=start+1;i<start+6;i++){
//            char old=text.charAt(i);
//            char newChar=color.charAt(j);
//            textWChColor.replace(old, newChar);
//            j++;
//        }
        StringBuffer buf = new StringBuffer(textWChColor);
        buf.replace(start, end, color);


        Log.d("changeColor", "text is now:" + buf.toString());
        textWChColor=buf.toString();
        return textWChColor;

//        int start=text.indexOf(tag);
//        int end=text.lastIndexOf(tag,start);
//        Log.d("changeColor","text:"+text);
//        Log.d("changeColor","tag:"+tag);
//        mRTMessageField.setSelection(start, end);
        //rtToolbar.setBGColor(color);
        //mRTManager.onEffectSelected(Effects.BGCOLOR,color);
    }

    public void copyNoteObject(){//ultima nota recebe a atual
        btLastNote.setBtId(btNoteNow.getBtId());
        btLastNote.setSelectedText(btNoteNow.getSelectedText());
        btLastNote.setBtY(btNoteNow.getBtY());
    }
*/
//    private Button createButton(final int id, final String value, final float yPosition) {
//        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        Button note = new Button(getContext());
//        note = (Button) inflater.inflate(R.layout.btn_specific_comment, scrollview, false);
//        final String tagID="<!--"+id+"-->";
//        //changeColor(tagID,Color.DKGRAY);
//        note.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                String amarelo= "#d9fce6";
////                String azul="#70E7D0";
////                String newColoredText=null;
////                Log.d("editor", "clicou na nota com id:" + id);
////                if(btNoteFirtClicked==false){
////                    btNoteNow= new Note(id,value,yPosition);
////                    //newColoredText=changeColor(mRTMessageField.getText(RTFormat.HTML), tagID, "#3763c8");
////                    Log.d("editor", "primeira vez que clica no bt:" + id);
////                    btNoteFirtClicked=true; //troca flag botao ja foi clicado
////                }else{
////                    copyNoteObject();
////                    btNoteNow = new Note(id,value,yPosition);
////                    //String txtOld=changeColor(mRTMessageField.getText(RTFormat.HTML), "id="+btLastNote.getBtId(), amarelo); // altera a cor do texto vinculado a ultima nota clicada para claro
////                    //Log.d("tag","text old with color yellow:"+txtOld);
////                    //Log.e("editor","text temp with color yellow:"+txtOld);
////                    //newColoredText=changeColor(txtOld, "font id="+btNoteNow.getBtId(), azul); //altera a cor do texto vinculado a nota atual clicada para cor de marcacao
////                    //Log.d("editor","new colored text with yellow and blue:"+newColoredText);
////                   // mRTMessageField.setRichTextEditing(true, newColoredText);
////                    changeColor(id);
////                    Log.d("editor", "text now in HTML is:" + mRTMessageField.getText(RTFormat.HTML));
////                    //newColoredText=changeColor(mRTMessageField.getText(RTFormat.HTML), btLastNote.getId, "#FF3F3F");
////                    //newColoredText=changeColor(newColoredText,""+btNoteNow.getBtId(), "#3763c8");
////                }
//                changeColor(id);
//
//                Button btn = (Button) v;
//                btn.setBackgroundResource(R.drawable.rounded_corner);
//                btn.setTextColor(Color.WHITE);
//
//                ArrayList<Note> arrayAux = new ArrayList<>(specificCommentsNotes.values());
//
//                for (int i = 0; i < arrayAux.size(); i++) {
//                    Button aux = (Button) getView().findViewById(arrayAux.get(i).getBtId());
//                    if (aux.getId() != btn.getId()) {
//                        aux.setBackgroundResource(R.drawable.btn_border);
//                        aux.setTextColor(greenDark);
//                    }
//                }
//            }
//        });
//
//        note.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Singleton single = Singleton.getInstance();
//                single.note.setBtY(yPosition);
//                single.note.setSelectedText(selectedActualText);
//                single.note.setBtId(id);
//                showCommentsTab(true);
//
//                return false;
//            }
//        });
//
//        note.setY(yPosition);
//        note.setX(5);
//        note.setText(value);
//        note.setId(id);
//
//        return note;
//    }
}
