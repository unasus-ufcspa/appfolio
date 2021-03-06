package com.ufcspa.unasus.appportfolio.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coolerfall.download.DownloadCallback;
import com.coolerfall.download.DownloadManager;
import com.coolerfall.download.DownloadRequest;
import com.ufcspa.unasus.appportfolio.activities.MainActivity;
import com.ufcspa.unasus.appportfolio.database.DataBase;
import com.ufcspa.unasus.appportfolio.model.Activity;
import com.ufcspa.unasus.appportfolio.model.Attachment;
import com.ufcspa.unasus.appportfolio.model.Singleton;
import com.ufcspa.unasus.appportfolio.model.StudFrPortClass;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.webClient.HttpClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

/**
 * Created by arthurzettler on 1/5/16.
 */
public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ViewHolder> {
    private static final String URL = "/webfolio/app_dev.php/download/";
    private List<Activity> mAcitivtyList;
    private MainActivity mMainActivity;
    private static Singleton sSingleton;
    private String mStudentName;
    private List<Attachment> mAttachmentList;
    private DataBase mDatabase;
    private StudFrPortClass mStudFrPortClass;

    public interface OnInfoButtonClick {
        void openInfo(View v, Activity activity);
    }

    private OnInfoButtonClick mCallBack;

    public void setOnInfoButtonClickListener(OnInfoButtonClick listener) {
        this.mCallBack = listener;
    }

    public ActivitiesAdapter(MainActivity mMainActivity, List<Activity> mAcitivtyList, String studName, StudFrPortClass aux) {
        this.mAcitivtyList = mAcitivtyList;
        this.mMainActivity = mMainActivity;
        this.sSingleton = Singleton.getInstance();
        Collections.sort(this.mAcitivtyList);
        this.mStudentName = studName;
        this.mStudFrPortClass = aux;
        mDatabase = DataBase.getInstance(mMainActivity);
    }

    @Override
    public ActivitiesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        ViewHolder vh;
        switch (viewType) {
            case 1:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_finished, parent, false);
                vh = new ViewHolder(v);

                vh.title = (TextView) v.findViewById(R.id.adapter_item_class_txv_code);
//                vh.moreInfo = (ImageButton) v.findViewById(R.id.btn_info);
//                vh.description = "";
                vh.notificationIcon = (TextView) v.findViewById(R.id.item_class_notification_icon);

//                vh.txtClassCodeInfo = (TextView) v.findViewById(R.id.adapter_item_class_txv_ds_port);
                return vh;
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_portfolio_activity, parent, false);
                vh = new ViewHolder(v);

                vh.title = (TextView) v.findViewById(R.id.adapter_item_class_txv_ds_title);
                vh.moreInfo = (ImageButton) v.findViewById(R.id.btn_info);
                vh.description = "";
                vh.notificationIcon = (TextView) v.findViewById(R.id.item_class_notification_icon);

                vh.txtClassCodeInfo = (TextView) v.findViewById(R.id.txt_class_code_info);
                vh.btnInfoClose = (ImageButton) v.findViewById(R.id.btn_info_close);
                vh.txtFinalizeActivity = (TextView) v.findViewById(R.id.txt_finalize_activity);
                vh.txtSendMessage = (TextView) v.findViewById(R.id.txt_send_message);
                vh.infoView = (RelativeLayout) v.findViewById(R.id.info);
                return vh;

        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Activity aux = mAcitivtyList.get(position);
        int r;

        switch (holder.getItemViewType()) {
            case 1:
                holder.title.setText(aux.getTitle());
//                holder.txtClassCodeInfo.setText(aux.getTitle());

                holder.title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sSingleton.guestUser = DataBase.getInstance(mMainActivity).userIsGuest(mAcitivtyList.get(position).getIdPortfolio());
                        sSingleton.guestUserComments = DataBase.getInstance(mMainActivity).guestCanComment(mAcitivtyList.get(position).getIdPortfolio());
                        sSingleton.activity = mAcitivtyList.get(position);
                        sSingleton.idActivityStudent = sSingleton.activity.getIdActivityStudent();
                        sSingleton.idVersionActivity = mDatabase.getLastIDVersionActivity(sSingleton.idActivityStudent);
                        sSingleton.idCurrentVersionActivity = sSingleton.idVersionActivity;
                        sSingleton.portfolioClass.setIdPortfolioStudent(mAcitivtyList.get(position).getIdPortfolio());
                        sSingleton.portfolioClass.setStudentName(mStudentName);
                        sSingleton.portfolioClass.setPhoto(mStudFrPortClass.getPhoto());
                        sSingleton.portfolioClass.setCellphone(mStudFrPortClass.getCellphone());
                        if (sSingleton.guestUser) {
                            mAttachmentList = mDatabase.getAttachmentsFromActivityStudent(aux.getIdActivityStudent());
                            if (mAttachmentList.size() > 0) {
                                downloadAttachments(mAttachmentList);
                            }
                        }
                        if (!sSingleton.guestUser || mAttachmentList.size() == 0) {
                            LocalBroadcastManager.getInstance(mMainActivity).sendBroadcast(new Intent("call.fragments.action").putExtra("ID", 6));
                        }
                    }
                });
                r = mDatabase.getActivityNotification(aux.getIdActivityStudent());
                if (r == 0) {
                    holder.notificationIcon.setVisibility(View.INVISIBLE);
                } else {
                    holder.notificationIcon.setBackgroundResource(R.drawable.ic_done_notification);
                    holder.notificationIcon.setVisibility(View.VISIBLE);
                }

                break;
            default:
                holder.title.setText(aux.getTitle());
                holder.txtClassCodeInfo.setText(aux.getTitle());
                holder.description = aux.getDescription();
                holder.moreInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCallBack != null) {
                            mCallBack.openInfo(v, aux);
                        }
//                holder.infoView.setVisibility(View.VISIBLE);
//                holder.infoView.startAnimation(AnimationUtils.loadAnimation(mMainActivity, R.anim.anim_right));

                        /*new AlertDialog.Builder(mMainActivity)
                                .setTitle(aux.getTitle())
                                .setMessage(aux.getDescription())
                                .setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();*/
                    }
                });

                holder.btnInfoClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.infoView.startAnimation(AnimationUtils.loadAnimation(mMainActivity, R.anim.anim_left));
                        holder.infoView.setVisibility(View.GONE);
                    }
                });

                holder.title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sSingleton.guestUser = DataBase.getInstance(mMainActivity).userIsGuest(mAcitivtyList.get(position).getIdPortfolio());
                        sSingleton.guestUserComments = DataBase.getInstance(mMainActivity).guestCanComment(mAcitivtyList.get(position).getIdPortfolio());
                        sSingleton.activity = mAcitivtyList.get(position);
                        sSingleton.idActivityStudent = sSingleton.activity.getIdActivityStudent();
                        if (sSingleton.portfolioClass.getPerfil().equals("S")) {
                            sSingleton.idVersionActivity = mDatabase.getIDVersionAtual(sSingleton.idActivityStudent);
                        } else {
                            sSingleton.idVersionActivity = mDatabase.getLastIDVersionActivity(sSingleton.idActivityStudent);
                        }
                        sSingleton.idCurrentVersionActivity = sSingleton.idVersionActivity;
                        sSingleton.portfolioClass.setIdPortfolioStudent(mAcitivtyList.get(position).getIdPortfolio());
                        sSingleton.portfolioClass.setStudentName(mStudentName);
                        sSingleton.portfolioClass.setPhoto(mStudFrPortClass.getPhoto());
                        sSingleton.portfolioClass.setCellphone(mStudFrPortClass.getCellphone());
                        if (sSingleton.guestUser) {
                            mAttachmentList = mDatabase.getAttachmentsFromActivityStudent(aux.getIdActivityStudent());
                            if (mAttachmentList.size() > 0) {
                                downloadAttachments(mAttachmentList);
                            }
                        }
                        if (!sSingleton.guestUser || mAttachmentList.size() == 0) {
                            LocalBroadcastManager.getInstance(mMainActivity).sendBroadcast(new Intent("call.fragments.action").putExtra("ID", 6));
                        }
                    }
                });

                holder.txtFinalizeActivity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mMainActivity, "Finalizar todas as atividades", Toast.LENGTH_SHORT).show();
                    }
                });
                holder.txtSendMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mMainActivity, "Enviar mensagens a todas as atividades", Toast.LENGTH_SHORT).show();
                    }
                });

                r = mDatabase.getActivityNotification(aux.getIdActivityStudent());
                if (r == 0) {
                    holder.notificationIcon.setVisibility(View.INVISIBLE);
                } else {
                    holder.notificationIcon.setVisibility(View.VISIBLE);
                    holder.notificationIcon.setText(r + "");
                }
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        String dtConclusion = DataBase.getInstance(mMainActivity).getActivityStudentById(mAcitivtyList.get(position).getIdActivityStudent()).getDt_conclusion();
        if (dtConclusion.equals("null") || dtConclusion == null) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return this.mAcitivtyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageButton moreInfo;
        public String description;
        public View view;
        public TextView notificationIcon;

        RelativeLayout infoView;
        TextView txtClassCodeInfo;
        ImageButton btnInfoClose;
        TextView txtFinalizeActivity;
        TextView txtSendMessage;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public void downloadAttachments(final List<Attachment> attachments) {
        DownloadManager manager = new DownloadManager();
        final int[] cont = {attachments.size()};
        for (final Attachment a : attachments) {
            // BAIXAR OS ATTACHMENTS!!!!!!!!
            String path = null;
            if (a.getNmSystem() != null) {
                String[] aux = a.getNmSystem().split("/");
                path = a.getNmSystem();
                if (aux.length > 0)
                    path = aux[aux.length - 1];
            }
            String filePath = Environment.getExternalStorageDirectory() + "/Android/data/com.ufcspa.unasus.appportfolio/files/images" + File.separator + path;
            File file = new File(filePath);
            if (!file.exists()) { //verifica se arquivo já existe antes de baixar
                //Log.d("File Path", filePath);
                String url = "http://" + new HttpClient(mMainActivity).ip + URL + path;
                DownloadRequest request = new DownloadRequest()
                        .setUrl(url)
                        .setDestFilePath(filePath)
                        .setDownloadCallback(new DownloadCallback() {
                            @Override
                            public void onSuccess(int downloadId, String filePath) {
                                super.onSuccess(downloadId, filePath);
                                cont[0]--;
                                mDatabase.updateAttachmentFlDownload(a.getIdAttachment());
                                if (filePath.contains(".mp4")) {
                                    saveSmallImage(filePath);
                                }
                                Log.d("atividade - anexos", "conseguiu baixar anexo com sucesso: " + filePath);
                                Toast.makeText(mMainActivity, "Conseguiu baixar anexo com sucesso", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(int downloadId, int statusCode, String errMsg) {
                                super.onFailure(downloadId, statusCode, errMsg);
                                Toast.makeText(mMainActivity, "Erro ao baixar anexos", Toast.LENGTH_LONG).show();
                            }
                        });
                manager.add(request);
                Log.d("atividade - anexos", "url do anexo: " + url);
            } else {
                cont[0]--;
                mDatabase.updateAttachmentFlDownload(a.getIdAttachment());
                Log.d("atividade - anexos", filePath + " já existe e não baixado");
            }
//                downloadAttachment("http://stuffpoint.com/stardoll/image/54056-stardoll-sdfs.jpg" + a.getNmSystem(), a.getNmFile());
        }
        if (cont[0] == 0) {
            LocalBroadcastManager.getInstance(mMainActivity).sendBroadcast(new Intent("call.fragments.action").putExtra("ID", 6));
        }
    }

    public String saveSmallImage(String videoPath) {
        String[] path = videoPath.split("/");
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

            Bitmap bitmap = createThumbnailFromPath(videoPath, MediaStore.Images.Thumbnails.MICRO_KIND);
            videoPath = newPath;

            Bitmap resized = Bitmap.createScaledBitmap(bitmap, 320, 240, true);
            resized.compress(Bitmap.CompressFormat.PNG, 40, fOutputStream);

            fOutputStream.flush();
            fOutputStream.close();

            MediaStore.Images.Media.insertImage(mMainActivity.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return videoPath;
    }

    public Bitmap createThumbnailFromPath(String filePath, int type) {
        return ThumbnailUtils.createVideoThumbnail(filePath, type);
    }
}
