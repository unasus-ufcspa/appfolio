package com.ufcspa.unasus.appportfolio.Activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Model.ActivityStudent;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import jp.wasabeef.richeditor.RichEditor;

/**
 * Created by Desenvolvimento on 25/11/2015.
 */
public class FragmentEditText extends Frag {
    private RichEditor mEditor;
    private TextView mPreview;
    private ActivityStudent acStudent;
    private Button btSave;

    private String selectedImagePath;
    //ADDED
    private String filemanagerstring;

    //Formatting Controls
    private Boolean bulletPoints = false;

    public FragmentEditText() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_text, null);
        //btSave=(Button)view.findViewById(R.id.edit_acttivity_bt_msg);
        source = new DataBaseAdapter(getActivity());

        singleton = Singleton.getInstance();
        singleton.idActivityStudent = source.getActivityStudentID(singleton.activity.getIdAtivity(),singleton.portfolioClass.getIdPortfolioStudent());

        mEditor = (RichEditor) view.findViewById(R.id.editor);
        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.BLACK);
        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        mEditor.setPadding(10, 10, 10, 10);
//    mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mEditor.setPlaceholder("Insert text here...");
//        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
//            @Override public void onTextChange(String text) {
//                mPreview.setText(text);
//            }
//        });

        System.out.println("ID ACTIVITY: " + singleton.activity.getIdAtivity() + " ID PORTFOLIO STUDENT: " +  singleton.portfolioClass.getIdPortfolioStudent());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.undo();
            }
        });

        getView().findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.redo();
            }
        });

        getView().findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBold();
            }
        });

        getView().findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setItalic();
            }
        });

        getView().findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setSubscript();
            }
        });

        getView().findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setSuperscript();
            }
        });

        getView().findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setStrikeThrough();
            }
        });

        getView().findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

        getView().findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(1);
            }
        });

        getView().findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(2);
            }
        });

        getView().findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(3);
            }
        });

        getView().findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(4);
            }
        });

        getView().findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(5);
            }
        });

        getView().findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(6);
            }
        });
        getView().findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setIndent();
            }
        });

        getView().findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setOutdent();
            }
        });

        getView().findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        getView().findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        getView().findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });

        getView().findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mEditor.setBlockquote();
            }
        });

        getView().findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseGalleryOrTakePicture();
            }
        });

        getView().findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mEditor.insertLink("https://github.com/wasabeef", "wasabeef");
                dispatchTakeVideoIntent();
            }
        });
        getView().findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mEditor.insertTodo();
                openFileBrowser();
            }
        });
        loadLastText();
        Log.d("Cycle", "On Resume");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Quando o usuário escolhe a opção Take Picture
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            galleryAddPic();
//            saveSmallImage();
//            mEditor.insertImage(mCurrentPhotoPath, "Testando imagem!");
            insertFileIntoDataBase(mCurrentPhotoPath, "I");
            insertImageIntoEditor(320, 240);
        }

        // Quando o usuário escolhe a opção Gallery
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri selectedUri = data.getData();
            String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.MIME_TYPE};

            Cursor cursor = getActivity().getContentResolver().query(selectedUri, columns, null, null, null);
            cursor.moveToFirst();

            int pathColumnIndex = cursor.getColumnIndex(columns[0]);
            int mimeTypeColumnIndex = cursor.getColumnIndex(columns[1]);

            String contentPath = cursor.getString(pathColumnIndex);
            String mimeType = cursor.getString(mimeTypeColumnIndex);

            cursor.close();

            mCurrentPhotoPath = contentPath;

            if (mimeType.startsWith("image")) {
//                saveSmallImage();
//                mEditor.insertImage(mCurrentPhotoPath, "Testando imagem!");
                insertImageIntoEditor(320, 240);
                insertFileIntoDataBase(mCurrentPhotoPath, "I");
            } else if (mimeType.startsWith("video")) {
                insertFileIntoDataBase(mCurrentPhotoPath, "V");
                mCurrentPhotoPath = getThumbnailPathForLocalFile(getActivity(), selectedUri);
                insertImageIntoEditor(320, 240);
            }
        }

        if (requestCode == PICKFILE_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            String FilePath = data.getData().getPath();
            insertFileIntoDataBase(FilePath, "T");
            //openPDF(FilePath);
        }

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {
//            mCurrentPhotoPath = data.getData().getPath();
            insertFileIntoDataBase(data.getData().getPath(), "I");
            mCurrentPhotoPath = getThumbnailPathForLocalFile(getActivity(), data.getData());
            insertImageIntoEditor(320, 240);
        }
        saveText();
        Log.d("Cycle", "Activity Result");
    }

    private void insertImageIntoEditor(int width, int height) {
        if (mEditor.getHtml() != null)
            mEditor.setHtml(mEditor.getHtml() + "<img src=\"" + mCurrentPhotoPath + "\" alt=\"Photo\" style=\"width:" + width + "px;height:" + height + "px;\">");
        else
            mEditor.setHtml("<img src=\"" + mCurrentPhotoPath + "\" alt=\"Photo\" style=\"width:" + width + "px;height:" + height + "px;\">");
    }

    @Override
    public void onStop() {
        super.onStop();
        saveText();
        Log.d("Cycle", "On Stop");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    public void loadLastText() {
//        singleton = Singleton.getInstance();
//        source = new DataBaseAdapter(getActivity());
        acStudent = source.listActivityStudent(singleton.idActivityStudent);
        mEditor.setHtml(acStudent.getTxtActivity());
    }

    public void saveText() {
//        singleton = Singleton.getInstance();
//        source = new DataBaseAdapter(getActivity());
        acStudent.setTxtActivity(mEditor.getHtml());
        source.updateActivityStudent(acStudent);
    }

    @Override
    public void onDestroy() {
        saveText();
        super.onDestroy();
    }
}