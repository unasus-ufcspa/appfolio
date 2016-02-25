package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.ufcspa.unasus.appportfolio.Dialog.FullImageDialog;
import com.ufcspa.unasus.appportfolio.Dialog.FullVideoDialog;
import com.ufcspa.unasus.appportfolio.Adapter.FragmentAttachmentAdapter;
import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.util.ArrayList;

/**
 * Created by desenvolvimento on 10/12/2015.
 */
public class FragmentAttachment extends Frag {

    private GridView attachmentGrid;
    private FragmentAttachmentAdapter listAdapter;
    private ArrayList<Attachment> attachments;

    private boolean isRTEditor;

    public FragmentAttachment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attachment, null);

        singleton = Singleton.getInstance();

        if (singleton.isRTEditor)
        {
            isRTEditor = true;
            setMargins(view.findViewById(R.id.attachment_layout), 0, 0, 0, 0);
            setMargins(view.findViewById(R.id.attachment_label), 50, 0, 50, 0);
            setMargins(view.findViewById(R.id.divider), 50, 0, 50, 0);
        }
        else
            isRTEditor = false;

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

        singleton = Singleton.getInstance();

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
        if (!isRTEditor)
            attachments.add(new Attachment(-1, -1, -1, "", "", ""));
    }

    private void loadPhoto(final String url, final int position) {
        if (url != null) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.custom_fullimage_dialog);

            final ImageView image = (ImageView) dialog.findViewById(R.id.fullimage);
            image.setImageBitmap(BitmapFactory.decodeFile(url));

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
                        returnFromDialog(url, position);
                    dialog.dismiss();
                }
            });

            btnNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isRTEditor)
                        returnFromDialog(url, position);
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
                        returnFromDialog(url, position);
                    dialog.dismiss();
                }
            });

            btnNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isRTEditor)
                        returnFromDialog(url, position);
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

    private void returnFromDialog(String url, int position) {
        if(isRTEditor)
        {
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent("call.attachments.action").putExtra("URL", url).putExtra("Type", attachments.get(position).getType()));
            dismiss();
        }
        else
        {
            deleteMedia(position);
        }
    }

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
}
