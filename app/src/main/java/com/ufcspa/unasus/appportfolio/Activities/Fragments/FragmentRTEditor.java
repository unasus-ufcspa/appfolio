package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.TextPaint;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.onegravity.rteditor.effects.Effect;
import com.onegravity.rteditor.effects.Effects;
import com.onegravity.rteditor.spans.BackgroundColorSpan;
import com.ufcspa.unasus.appportfolio.Activities.MainActivity;
import com.ufcspa.unasus.appportfolio.Model.FormatedText;
import com.ufcspa.unasus.appportfolio.Model.Note;
import com.ufcspa.unasus.appportfolio.R;

import java.util.ArrayList;


public class FragmentRTEditor extends Fragment {
    private RTManager mRTManager;
    private RTEditText mRTMessageField;
    private RTToolbar rtToolbar;
    private boolean btNoteFirtClicked=false;
    private int currentSpecificComment;
    private ViewGroup scrollview;
    private ImageButton fullScreen;

    private ArrayList<Note> specificCommentsNotes;
    private Note btNoteNow;
    private Note btLastNote;

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
        btLastNote= new Note(0,"null",0);

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

        mRTMessageField.setRichTextEditing(true, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce euismod nibh in dignissim sagittis. Etiam tincidunt eu libero eget porta. In tempus, tellus eget convallis maximus, lorem nulla tristique ligula, nec luctus lectus nisl ac lectus. Nullam sit amet fringilla quam. Nunc volutpat neque leo, non rutrum nibh pulvinar vel. Donec posuere ex ut commodo mattis. Nunc auctor, velit eu interdum sollicitudin, lorem nunc condimentum quam, a mollis felis magna non purus. Vivamus rhoncus tempus accumsan. Curabitur eget ante condimentum, ornare augue euismod, consectetur dui. Etiam vel rutrum orci, in interdum arcu. Suspendisse ultrices, tellus non ultrices feugiat, elit est hendrerit purus, sed dapibus sem magna ut lectus. Suspendisse cursus non orci et gravida. Vivamus et congue elit. Curabitur molestie sed nulla sit amet dapibus.");

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

        ImageView geral = (ImageView)view.findViewById(R.id.btn_geral);
        ImageView specific = (ImageView)view.findViewById(R.id.btn_specific);

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

    private Button createButton(final int id, final String value, final float yPosition) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Button note = new Button(getContext());
        note = (Button) inflater.inflate(R.layout.btn_specific_comment, scrollview, false);
        final String tagID="<!--"+id+"-->";
        //changeColor(tagID,Color.DKGRAY);
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amarelo= "#d9fce6";
                String azul="#70E7D0";
                String newColoredText=null;
                Log.d("editor", "clicou na nota com id:" + id);
                if(btNoteFirtClicked==false){
                    btNoteNow= new Note(id,value,yPosition);
                    //newColoredText=changeColor(mRTMessageField.getText(RTFormat.HTML), tagID, "#3763c8");
                    Log.d("editor", "primeira vez que clica no bt:" + id);
                    btNoteFirtClicked=true; //troca flag botao ja foi clicado
                }else{
                    copyNoteObject();
                    btNoteNow=new Note(id,value,yPosition);
                    //String txtOld=changeColor(mRTMessageField.getText(RTFormat.HTML), "id="+btLastNote.getBtId(), amarelo); // altera a cor do texto vinculado a ultima nota clicada para claro
                    //Log.d("tag","text old with color yellow:"+txtOld);
                    //Log.e("editor","text temp with color yellow:"+txtOld);
                    //newColoredText=changeColor(txtOld, "font id="+btNoteNow.getBtId(), azul); //altera a cor do texto vinculado a nota atual clicada para cor de marcacao
                    //Log.d("editor","new colored text with yellow and blue:"+newColoredText);
                   // mRTMessageField.setRichTextEditing(true, newColoredText);
                    changeColor(id);
                    Log.d("editor", "text now in HTML is:" + mRTMessageField.getText(RTFormat.HTML));
                    //newColoredText=changeColor(mRTMessageField.getText(RTFormat.HTML), btLastNote.getId, "#FF3F3F");
                    //newColoredText=changeColor(newColoredText,""+btNoteNow.getBtId(), "#3763c8");
                }

                
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

        note.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showCommentsTab(true);
                return false;
            }
        });

        note.setY(yPosition);
        note.setText(value);
        note.setId(id);

        return note;
    }


    public void copyNoteObject(){//ultima nota recebe a atual
        btLastNote.setBtId(btNoteNow.getBtId());
        btLastNote.setSelectedText(btNoteNow.getSelectedText());
        btLastNote.setBtY(btNoteNow.getBtY());
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
        for(BackgroundColorSpan spm : spans)
        {
            if(spm.getId() == id)
            {
                spm.setColor(getResources().getColor(R.color.base_green));
            }
            else
            {
                if(spm.getId() != -1)
                    spm.setColor(getResources().getColor(R.color.base_green_light));
            }
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
                    String selectedText =getSelectedText();
                            //mRTMessageField.getText(RTFormat.HTML).substring(startSelection, endSelection);

                    if (!selectedText.isEmpty()) {
                        if (selectedText.length() > 0) {
                            //findText(selectedText, mRTMessageField.getText(RTFormat.HTML));
                            createSpecificCommentNote(getCaretYPosition(startSelection), selectedText);
                            //changeColor(selectedText,"#FFFFFF");
                            Log.d("editor","text:"+ mRTMessageField.getText(RTFormat.HTML));
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

            mRTManager.onEffectSelected(Effects.BGCOLOR, getResources().getColor(R.color.base_green_light), idButton);
            mRTMessageField.setSelection(endSelection);
            mRTMessageField.setSelected(false);
        }
    }

    private class FullScreen implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            Log.d("rteditor", mRTMessageField.getText(RTFormat.HTML));
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
