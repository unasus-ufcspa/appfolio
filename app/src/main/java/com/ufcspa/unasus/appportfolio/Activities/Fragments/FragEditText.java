package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onegravity.rteditor.RTEditText;
import com.onegravity.rteditor.RTToolbar;
import com.onegravity.rteditor.api.RTApi;
import com.onegravity.rteditor.api.RTMediaFactoryImpl;
import com.onegravity.rteditor.api.RTProxyImpl;
import com.ufcspa.unasus.appportfolio.Model.NewRTManager;
import com.ufcspa.unasus.appportfolio.R;

/**
 * Created by desenvolvimento on 15/12/2015.
 */
public class FragEditText extends Frag {
    private NewRTManager rtManager;
    private RTEditText rtEditText;
//    private Button btCopy;

    private Toolbar toolbar;

    public FragEditText() {
    }

    public static String insert(String bag, String marble, int index) {
        String bagBegin = bag.substring(0, index);
        //System.out.println(bagBegin);
        String bagEnd = bag.substring(index);
        //System.out.println(bagEnd);
        return bagBegin + marble + bagEnd;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTheme(R.style.RTE_ThemeLight);

        View view = inflater.inflate(R.layout.activity_text, null);

        // create RTManager
        RTApi rtApi = new RTApi(getActivity(), new RTProxyImpl(getActivity()), new RTMediaFactoryImpl(getActivity(), true));
        rtManager = new NewRTManager(rtApi, savedInstanceState);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        btCopy = (Button) getView().findViewById(R.id.btCopyText);
//        btCopy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int i = rtEditText.getSelectionStart();
//                int j = rtEditText.getSelectionEnd();
//                String selectedText = rtEditText.getText().toString().substring(i, j);
//                Log.d("select text:", "" + selectedText);
//
////                FragmentComments fragment2 = new FragmentComments();
////                FragmentManager fragmentManager = getFragmentManager();
////                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////                fragmentTransaction.replace(R.id.content_frame, fragment2);
////                fragmentTransaction.addToBackStack(null);
////                fragmentTransaction.commit();
//            }
//        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // register toolbar
        ViewGroup toolbarContainer = (ViewGroup) getView().findViewById(R.id.rte_toolbar_container);
        RTToolbar rtToolbar = (RTToolbar) getView().findViewById(R.id.rte_toolbar);
        if (rtToolbar != null) {
            rtManager.registerToolbar(toolbarContainer, rtToolbar);
        }

        // register editor & set text
        rtEditText = (RTEditText) getView().findViewById(R.id.rtEditText);
        rtManager.registerEditor(rtEditText, true);
        rtEditText.setRichTextEditing(true, "<b>Bem Vindo!</b>");

        rtEditText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        rtManager.onSaveInstanceState(outState);
    }

//    String s = rtEditText.getText(RTFormat.SPANNED);
//    String s2 = rtEditText.getText(RTFormat.HTML);
//    String s3 = rtEditText.getText(RTFormat.PLAIN_TEXT);
//
//    System.out.println(s);
//    System.out.println(s2);
//    System.out.println(s3);
//    int i = rtEditText.getSelectionEnd();
//    //System.out.println(i);
//    rtEditText.setRichTextEditing(true, insert(s,"BANANA", i));

    @Override
    public void onDestroy() {
        super.onDestroy();

        rtManager.onDestroy(getActivity().isFinishing());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Quando o usuário escolhe a opção Take Picture
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            galleryAddPic();
            //saveImage();
            //mEditor.insertImage(mCurrentPhotoPath, "Testando imagem!");
            insertFileIntoDataBase(mCurrentPhotoPath, "I");
            //insertImageIntoEditor(320, 240);
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
                //insertImageIntoEditor(320, 240);
                insertFileIntoDataBase(mCurrentPhotoPath, "I");
            } else if (mimeType.startsWith("video")) {
//                insertVideoIntoEditor(320, 240, "mp4");
                insertFileIntoDataBase(mCurrentPhotoPath, "V");
                mCurrentPhotoPath = getThumbnailPathForLocalFile(getActivity(), selectedUri);
                //insertImageIntoEditor(320, 240);
            }
        }

        if (requestCode == PICKFILE_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            String FilePath = data.getData().getPath();
            insertFileIntoDataBase(FilePath, "T");
            //openPDF(FilePath);
        }

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {
//            mCurrentPhotoPath = data.getData().getPath();
//            insertVideoIntoEditor(320, 240, "mp4");
            insertFileIntoDataBase(data.getData().getPath(), "I");
            mCurrentPhotoPath = getThumbnailPathForLocalFile(getActivity(), data.getData());
            // insertImageIntoEditor(320, 240);
        }
//        saveText();
//        Log.d("Cycle", "Activity Result");
    }
}
