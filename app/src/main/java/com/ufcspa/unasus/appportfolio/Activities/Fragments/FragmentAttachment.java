package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.ufcspa.unasus.appportfolio.Activities.MainActivity;
import com.ufcspa.unasus.appportfolio.Dialog.FullImageDialog;
import com.ufcspa.unasus.appportfolio.Dialog.FullVideoDialog;
import com.ufcspa.unasus.appportfolio.Adapter.FragmentAttachmentAdapter;
import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.sql.SQLOutput;
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
    private String type;

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
        attachments = source.getAttachmentsFromActivityStudent(singleton.idActivityStudent);
        createPlusButton();
    }

    private void createPlusButton() {
        if (!isRTEditor && !singleton.isRTEditor)
            attachments.add(new Attachment(-1, -1, -1, "", "", ""));
    }

    private void loadPhoto(final String url, final int position) {
        if (url != null) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.custom_fullimage_dialog);

            final ImageView image = (ImageView) dialog.findViewById(R.id.fullimage);
            Uri uri = Uri.fromFile(new File(url));//Uri.parse(url);
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(uri));
                image.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

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
                    if(isRTEditor)
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

            dialog.show();
        }
    }

    private void loadVideo(final String url, final int position) {
        if(url != null) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.custom_fullvideo_dialog);

            VideoView video = (VideoView) dialog.findViewById(R.id.videoView);

            video.setVideoURI(Uri.parse(url));
            video.requestFocus();
            video.start();

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
            ((MainActivity)getActivity()).callRTEditorToAttachSomething(url, cursorPosition, type);
        }
        else
        {
            deleteMedia(position);
        }
    }
}
