package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.VideoView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ufcspa.unasus.appportfolio.Activities.MainActivity;
import com.ufcspa.unasus.appportfolio.Adapter.FragmentAttachmentAdapter;
import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by desenvolvimento on 10/12/2015.
 */
public class FragmentAttachment extends Frag {

    private GridView attachmentGrid;
    private FragmentAttachmentAdapter listAdapter;
    private ArrayList<Attachment> attachments;

    private boolean isRTEditor;
    private int cursorPosition;

    public FragmentAttachment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attachment, null);

        singleton = Singleton.getInstance();

        if (singleton.isRTEditor)
        {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                cursorPosition = bundle.getInt("Position", -1);;
            }
            isRTEditor = true;
        }
        else
            isRTEditor = false;

        ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);

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

    private void init()
    {
        source = DataBaseAdapter.getInstance(getActivity());

        attachmentGrid = (GridView) getView().findViewById(R.id.attachment_gridview);

        attachments = source.getAttachmentsFromActivityStudent(singleton.idActivityStudent);
        createPlusButton();

        listAdapter = new FragmentAttachmentAdapter(this, attachments);
        attachmentGrid.setAdapter(listAdapter);
    }

    public void imageClicked(int position) {
        Attachment attachment = attachments.get(position);
        if(attachment.getLocalPath() != null && attachment.getLocalPath() != "")
        {
            loadPhoto(attachment.getLocalPath(), position);
        }
    }

    public void videoClicked(int position) {
        Attachment attachment = attachments.get(position);
        if(attachment.getLocalPath() != null && attachment.getLocalPath() != "")
        {
            loadVideo(attachment.getLocalPath(), position);
        }
    }

    public void textClicked(int position) {
        Attachment attachment = attachments.get(position);
        if(attachment.getLocalPath() != null && attachment.getLocalPath() != "")
        {
            openPDF(attachment.getLocalPath());
            showPDFDialog(attachment.getLocalPath(), position);
        }
    }

    public void plusClicked() {
        addAttachmentToComments();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Quando o usuário escolhe a opção Take Picture
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            galleryAddPic();
            insertFileIntoDataBase(mCurrentPhotoPath, "I");
            launchCropImageIntent();
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
//                saveAttachment();
                insertFileIntoDataBase(mCurrentPhotoPath, "I");
                launchCropImageIntent();
            } else if (mimeType.startsWith("video")) {
                insertFileIntoDataBase(mCurrentPhotoPath, "V");
                mCurrentPhotoPath = getThumbnailPathForLocalFile(getActivity(), selectedUri);
            }
        }

        // Quando o usuário escolhe a opção Text
        if (requestCode == PICKFILE_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            insertFileIntoDataBase(data.getData().getPath(), "T");
        }

        // Quando o usuário escolhe a opção Videos
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {
            insertFileIntoDataBase(data.getData().getPath(), "V");
            galleryAddPic();
//            mCurrentPhotoPath = getThumbnailPathForLocalFile(getActivity(), data.getData());
        }
    }

    @Override
    public void insertFileIntoDataBase(final String path, final String type) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("Escolha um nome:");

        // Set up the input
        final EditText input = new EditText(getContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                source.saveAttachmentActivityStudent(path, type, singleton.idActivityStudent); //input.getText().toString()
                attachments = source.getAttachmentsFromActivityStudent(singleton.idActivityStudent);
                createPlusButton();
                listAdapter.refresh(attachments);
            }
        });

        builder.show();
    }

    private void createPlusButton() {
        if (!isRTEditor && !singleton.isRTEditor)
            attachments.add(new Attachment(-1, -1, -1, "", "", ""));
    }

    private void loadPhoto(final String url, final int position) {
        if (url != null) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_fullimage_dialog);

            Button btnPositive = (Button) dialog.findViewById(R.id.btn_positive);
            Button btnNegative = (Button) dialog.findViewById(R.id.btn_negative);

            if (isRTEditor) {
                btnNegative.setText(getResources().getText(R.string.attachment_negative));
                btnPositive.setText(getResources().getText(R.string.attachment_positive));
            } else {
                btnNegative.setText(getResources().getText(R.string.attachment_delete));
                btnPositive.setText(getResources().getText(R.string.attachment_fechar));
            }

            btnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isRTEditor)
                        returnFromDialog(url, position, "I");
                    dialog.dismiss();
                }
            });

            btnNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isRTEditor)
                        returnFromDialog(url, position, "I");
                    dialog.dismiss();
                }
            });

            final ImageView image = (ImageView) dialog.findViewById(R.id.fullimage);
            image.setAdjustViewBounds(true);

            Uri uri = Uri.fromFile(new File(url));
            Picasso.with(getActivity()).load(uri).into(image, new Callback() {
                @Override
                public void onSuccess() {
                    dialog.show();
                }

                @Override
                public void onError() {

                }
            });
        }
    }

    private void loadVideo(final String url, final int position) {
        if(url != null) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_fullvideo_dialog);

            final VideoView video = (VideoView) dialog.findViewById(R.id.videoView);

            video.setVideoPath(url);
            video.requestFocus();
            video.start();
            video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    dialog.dismiss();
                    return false;
                }
            });

            Button btnPositive = (Button) dialog.findViewById(R.id.btn_positive_video);
            Button btnNegative = (Button) dialog.findViewById(R.id.btn_negative_video);

            if (isRTEditor) {
                btnNegative.setText(getResources().getText(R.string.attachment_negative));
                btnPositive.setText(getResources().getText(R.string.attachment_positive));
            } else {
                btnNegative.setText(getResources().getText(R.string.attachment_delete));
                btnPositive.setText(getResources().getText(R.string.attachment_fechar));
            }

            btnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isRTEditor)
                        returnFromDialog(url, position, "V");
                    dialog.dismiss();
                }
            });

            btnNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isRTEditor)
                        returnFromDialog(url, position, "V");
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }

    public void showPDFDialog(String url, final int position)
    {
        AlertDialog.Builder pdfDialog = new AlertDialog.Builder(getActivity());

        pdfDialog.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });
        pdfDialog.setNegativeButton("Deletar", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                deleteMedia(position);
            }

        });

        pdfDialog.create();
        pdfDialog.show();
    }


    public void deleteMedia(int position)
    {
        attachments.remove(position);
    }

    private void returnFromDialog(String url, int position, String type) {
        if(isRTEditor)
        {
            if (type.equals("V")) {
                mCurrentPhotoPath = url;
                saveSmallImage();
                url = mCurrentPhotoPath;
            }
            ((MainActivity)getActivity()).callRTEditorToAttachSomething(url, cursorPosition, type);
        }
        else
        {
            deleteMedia(position);
        }
    }
}
