package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.onegravity.rteditor.utils.Constants;
import com.ufcspa.unasus.appportfolio.Adapter.FragmentAttachmentAdapter;
import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by desenvolvimento on 10/12/2015.
 */
public class FragmentAttachment extends Frag {

    private GridView attachmentGrid;
    private FragmentAttachmentAdapter listAdapter;
    private ArrayList<Attachment> attachments;

    public FragmentAttachment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attachment, null);

        singleton = Singleton.getInstance();
        source = DataBaseAdapter.getInstance(getContext());

        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void init() {
        source = DataBaseAdapter.getInstance(getActivity());

        attachmentGrid = (GridView) getView().findViewById(R.id.attachment_gridview);

        attachments = source.getAttachments();
        createPlusButton();

        listAdapter = new FragmentAttachmentAdapter(this, attachments);
        attachmentGrid.setAdapter(listAdapter);
    }

    public void imageClicked(int position) {
        Attachment attachment = attachments.get(position);
        if (attachment.getNmSystem() != null && attachment.getNmSystem() != "") {
            loadPhoto(attachment.getNmSystem(), position);
        }
    }

    public void videoClicked(int position) {
        Attachment attachment = attachments.get(position);
        if (attachment.getNmSystem() != null && attachment.getNmSystem() != "") {
            loadVideo(attachment.getNmSystem(), position);
        }
    }

    public void textClicked(int position) {
        Attachment attachment = attachments.get(position);
        if (attachment.getNmSystem() != null && attachment.getNmSystem() != "") {
            loadPDF(attachment.getNmSystem(), position);
        }
    }

    public void plusClicked() {
        addAttachment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.CROP_IMAGE) {
                saveImageOnAppDir();
                insertFileIntoDataBase(mCurrentPhotoPath+".jpg", "I");
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                galleryAddPic();
                launchCropImageIntent();
            } else if (requestCode == RESULT_LOAD_IMAGE) {
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
                    saveImage();
                    launchCropImageIntent();
                } else if (mimeType.startsWith("video")) {
                    saveVideoOnAppDir();
                    insertFileIntoDataBase(mCurrentPhotoPath+".mp4", "V");
                }

            } else if (requestCode == PICKFILE_RESULT_CODE) {
                mCurrentPhotoPath = getPDFPath(getContext(), data.getData());
                savePDFOnAppDir();
                insertFileIntoDataBase(mCurrentPhotoPath+".pdf", "T");
            } else if (requestCode == REQUEST_VIDEO_CAPTURE) {
                galleryAddPic();
                saveVideoOnAppDir();
                insertFileIntoDataBase(mCurrentPhotoPath+".mp4", "V");
            }
        }
    }

    @Override
    public void insertFileIntoDataBase(final String path, final String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Escolha um nome:");
        builder.setCancelable(false);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            builder.setOnDismissListener(null);
//        }

        // Set up the input
        final EditText input = new EditText(getContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                if (name.isEmpty()) {
                    name = "Anexo";
                }
                singleton.lastIdAttach = source.insertAttachment(new Attachment(0, type, name, path,0,0));
                attachments = source.getAttachments();
                createPlusButton();
                listAdapter.refresh(attachments);
            }
        });

        builder.show();
    }

    public void createPlusButton() {
        attachments.add(new Attachment(-1, "", "", "", 0,0));
    }

    private void loadPhoto(final String url, final int position) {
        if (url != null) {
            loadPhoto(url);
        }
    }

    private void loadVideo(final String url, final int position) {
        if (url != null) {
            loadVideo(url);
        }
    }

    public void loadPDF(final String url, final int position) {
        if (url != null) {
            openPDF(url);
        }
    }


    public void deleteMedia(Set<Attachment> positions) {
        boolean allAttachmentDeleted = true;
        if (positions != null) {
            for (Attachment attachment : positions) {
                boolean bool = source.deleteAttachment(attachment);
                if (allAttachmentDeleted)
                    allAttachmentDeleted = bool;
                if (bool) {
                    attachments.remove(attachment);
                    File file = new File(attachment.getNmSystem());
                    if (file.exists())
                        file.delete();
                }
            }

            listAdapter.refresh(attachments);
        }

        if (!allAttachmentDeleted)
            Toast.makeText(getActivity(), "Alguns anexos não foram deletados, pois estão sendo utilizados em outra parte do aplicativo.", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(), "Anexos deletados com sucesso.", Toast.LENGTH_SHORT).show();
    }

    public void deleteOneMedia(int position) {
        attachments.remove(position);
        listAdapter.refresh(attachments);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
