package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ufcspa.unasus.appportfolio.Adapter.FragmentAttachmentDialogAdapter;
import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 25/11/2015.
 */
public class AttachmentDialogFragment extends DialogFragment {
    private GridView attachmentGrid;
    private FragmentAttachmentDialogAdapter listAdapter;
    private ArrayList<Attachment> attachments;
    private Singleton singleton;
    private DataBaseAdapter source;

    public AttachmentDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attachment_dialog, null);

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

        attachments = source.getAttachments(singleton.guestUser);

        listAdapter = new FragmentAttachmentDialogAdapter(this, attachments);
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

    private void loadPhoto(final String url, final int position) {
        if (url != null) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_image);

            Button btnPositive = (Button) dialog.findViewById(R.id.btn_positive);
            Button btnNegative = (Button) dialog.findViewById(R.id.btn_negative);

            btnNegative.setText(getResources().getText(R.string.attachment_negative));
            btnPositive.setText(getResources().getText(R.string.attachment_positive));
            btnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnFromDialog(url, "I", attachments.get(position).getIdAttachment());
                    dialog.dismiss();
                }
            });

            btnNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
        if (url != null) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_video);

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

            btnNegative.setText(getResources().getText(R.string.attachment_negative));
            btnPositive.setText(getResources().getText(R.string.attachment_positive));

            btnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnFromDialog(url, "V", attachments.get(position).getIdAttachment());
                    dialog.dismiss();
                }
            });

            btnNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }

    public void loadPDF(final String url, final int position) {
        if (url != null) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_pdf);

            Button btnPositive = (Button) dialog.findViewById(R.id.btn_pdf_util);
            Button btnNegative = (Button) dialog.findViewById(R.id.btn_pdf_cancel);
            Button btnView = (Button) dialog.findViewById(R.id.btn_pdf_view);

            btnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnFromDialog(url, "T", attachments.get(position).getIdAttachment());
                    dialog.dismiss();
                }
            });

            btnNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPDF(url);
                }
            });

            dialog.show();
        }
    }

    private void returnFromDialog(String url, String type, int idAttachment) {
        Intent data = new Intent();
        data.putExtra("url", url);
        data.putExtra("type", type);
        data.putExtra("idAttachment", idAttachment);

        if (type.equals("V")) {
            String smallImagePath = saveSmallImage(url);
            if (smallImagePath != null)
                data.putExtra("smallImagePath", smallImagePath);
        }

        getParentFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
        dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void openPDF(String filename) {
        File file = new File(filename);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file), "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
            Toast.makeText(getActivity(), "Nenhum leitor de PDF instalado.", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveSmallImage(String mCurrentPhotoPath) {
        String[] path = mCurrentPhotoPath.split("/");
        String[] secondPath = path[path.length - 1].split("\\.");
        secondPath[0] += "_video";
        path[path.length - 1] = secondPath[0] + "." + secondPath[1];

        String newPath = "";
        for (int i = 1; i < path.length; i++)
            newPath += "/" + path[i];

        OutputStream fOutputStream = null;
        File file = new File(newPath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            fOutputStream = new FileOutputStream(file);

            Bitmap bitmap = createThumbnailFromPath(mCurrentPhotoPath, MediaStore.Images.Thumbnails.MICRO_KIND);

            Bitmap resized = Bitmap.createScaledBitmap(bitmap, 320, 240, true);
            resized.compress(Bitmap.CompressFormat.PNG, 40, fOutputStream);

            fOutputStream.flush();
            fOutputStream.close();

            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Save Failed", Toast.LENGTH_SHORT).show();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Save Failed", Toast.LENGTH_SHORT).show();
            return null;
        }

        return newPath;
    }

    public Bitmap createThumbnailFromPath(String filePath, int type) {
        return ThumbnailUtils.createVideoThumbnail(filePath, type);
    }
}