package com.ufcspa.unasus.appportfolio.Adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
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
import com.ufcspa.unasus.appportfolio.Activities.MainActivity;
import com.ufcspa.unasus.appportfolio.Model.Activity;
import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.Model.StudFrPortClass;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.WebClient.HttpClient;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

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
    private List<Activity> list;
    private MainActivity context;
    private Singleton singleton;
    private String studentName;
    private List<Attachment> attachments;
    private DataBaseAdapter source;
    private StudFrPortClass studFrPortClass;
    private static final String URL="/webfolio/app_dev.php/download/";

    public interface OnInfoButtonClick{
        void openInfo(View v,Activity activity);
    }

    private OnInfoButtonClick callback;

    public void setOnInfoButtonClickListener(OnInfoButtonClick listener){
        this.callback = listener;
    }

    public ActivitiesAdapter(MainActivity context, List<Activity> list, String studName, StudFrPortClass aux) {
        this.list = list;
        this.context = context;
        this.singleton = Singleton.getInstance();
        Collections.sort(this.list);
        this.studentName = studName;
        this.studFrPortClass = aux;
        source = DataBaseAdapter.getInstance(context);
    }

    @Override
    public ActivitiesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        ViewHolder vh;
        switch (viewType) {
            case 1:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_class_finished, parent, false);
                vh = new ViewHolder(v);

                vh.title = (TextView) v.findViewById(R.id.adapter_item_class_txv_code);
//                vh.moreInfo = (ImageButton) v.findViewById(R.id.btn_info);
//                vh.description = "";
                vh.notificationIcon = (TextView) v.findViewById(R.id.item_class_notification_icon);

//                vh.txt_class_code_info = (TextView) v.findViewById(R.id.adapter_item_class_txv_ds_port);
                return vh;
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_portfolio_activity, parent, false);
                vh = new ViewHolder(v);

                vh.title = (TextView) v.findViewById(R.id.adapter_item_class_txv_ds_title);
                vh.moreInfo = (ImageButton) v.findViewById(R.id.btn_info);
                vh.description = "";
                vh.notificationIcon = (TextView) v.findViewById(R.id.item_class_notification_icon);

                vh.txt_class_code_info = (TextView) v.findViewById(R.id.txt_class_code_info);
                vh.btnInfoClose = (ImageButton) v.findViewById(R.id.btn_info_close);
                vh.txt_finalize_activity = (TextView) v.findViewById(R.id.txt_finalize_activity);
                vh.txt_send_message = (TextView) v.findViewById(R.id.txt_send_message);
                vh.infoView = (RelativeLayout) v.findViewById(R.id.info);
                return vh;

        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Activity aux = list.get(position);
        int r;

        switch (holder.getItemViewType()) {
            case 1:
                holder.title.setText(aux.getTitle());
//                holder.txt_class_code_info.setText(aux.getTitle());

                holder.title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        singleton.activity = list.get(position);
                        singleton.idActivityStudent = singleton.activity.getIdActivityStudent();
                        singleton.idVersionActivity = source.getLastIDVersionActivity(singleton.idActivityStudent);
                        singleton.idCurrentVersionActivity = singleton.idVersionActivity;
                        singleton.portfolioClass.setIdPortfolioStudent(list.get(position).getIdPortfolio());
                        singleton.portfolioClass.setStudentName(studentName);
                        singleton.portfolioClass.setPhoto(studFrPortClass.getPhoto());
                        singleton.portfolioClass.setCellphone(studFrPortClass.getCellphone());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("call.fragments.action").putExtra("ID", 6));
                    }
                });
                r = source.getActivityNotification(aux.getIdActivityStudent());
                if (r == 0) {
                    holder.notificationIcon.setVisibility(View.INVISIBLE);
                } else {
                    holder.notificationIcon.setBackgroundResource(R.drawable.done_notification);
                    holder.notificationIcon.setVisibility(View.VISIBLE);
                }

                break;
            default:
                holder.title.setText(aux.getTitle());
                holder.txt_class_code_info.setText(aux.getTitle());
                holder.description = aux.getDescription();
                holder.moreInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (callback != null){
                            callback.openInfo(v,aux);
                        }
//                holder.infoView.setVisibility(View.VISIBLE);
//                holder.infoView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_right));

                        /*new AlertDialog.Builder(context)
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
                        holder.infoView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_left));
                        holder.infoView.setVisibility(View.GONE);
                    }
                });

                holder.title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (singleton.guestUser) {
                            attachments = source.getAttachmentsFromActivityStudent(aux.getIdActivityStudent());
                            if (attachments.size()>0) {
                                downloadAttachments(attachments);
                            }
                        }
                        singleton.activity = list.get(position);
                        singleton.idActivityStudent = singleton.activity.getIdActivityStudent();
                        if (singleton.portfolioClass.getPerfil().equals("S")) {
                            singleton.idVersionActivity = source.getIDVersionAtual(singleton.idActivityStudent);
                        } else {
                            singleton.idVersionActivity = source.getLastIDVersionActivity(singleton.idActivityStudent);
                        }
                        singleton.idCurrentVersionActivity = singleton.idVersionActivity;
                        singleton.portfolioClass.setIdPortfolioStudent(list.get(position).getIdPortfolio());
                        singleton.portfolioClass.setStudentName(studentName);
                        singleton.portfolioClass.setPhoto(studFrPortClass.getPhoto());
                        singleton.portfolioClass.setCellphone(studFrPortClass.getCellphone());
                        if (!singleton.guestUser || attachments.size()==0) {
                            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("call.fragments.action").putExtra("ID", 6));
                        }
                    }
                });

                holder.txt_finalize_activity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Finalizar todas as atividades", Toast.LENGTH_SHORT).show();
                    }
                });
                holder.txt_send_message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Enviar mensagens a todas as atividades", Toast.LENGTH_SHORT).show();
                    }
                });

                r = source.getActivityNotification(aux.getIdActivityStudent());
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
        String dt_conclusion = DataBaseAdapter.getInstance(context).getActivityStudentById(list.get(position).getIdActivityStudent()).getDt_conclusion();
        if (dt_conclusion.equals("null") || dt_conclusion == null) {
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageButton moreInfo;
        public String description;
        public View view;
        public TextView notificationIcon;

        RelativeLayout infoView;
        TextView txt_class_code_info;
        ImageButton btnInfoClose;
        TextView txt_finalize_activity;
        TextView txt_send_message;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public void downloadAttachments(List<Attachment> attachments){
        DownloadManager manager = new DownloadManager();
        for (Attachment a : attachments) {
            // BAIXAR OS ATTACHMENTS!!!!!!!!
            String path=null;
            if (a.getNmSystem()!=null) {
                String[] aux = a.getNmSystem().split("/");
                path = a.getNmSystem();
                if (aux.length > 0)
                    path = aux[aux.length - 1];
            }
            String filePath = Environment.getExternalStorageDirectory()+"/Android/data/com.ufcspa.unasus.appportfolio/files/images" + File.separator + path;
            File file = new File(filePath);
            if (!file.exists()) { //verifica se arquivo já existe antes de baixar
                //Log.d("File Path", filePath);
                String url = "http://" + new HttpClient(context).ip + URL + path;
                DownloadRequest request = new DownloadRequest()
                        .setUrl(url)
                        .setDestFilePath(filePath)
                        .setDownloadCallback(new DownloadCallback() {
                            @Override
                            public void onSuccess(int downloadId, String filePath) {
                                super.onSuccess(downloadId, filePath);
                                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("call.fragments.action").putExtra("ID", 6));
                                if (filePath.contains(".mp4")) {
                                    saveSmallImage(filePath);
                                }
                                Log.d("atividade - anexos","conseguiu baixar anexo com sucesso: "+filePath);
                                Toast.makeText(context,"Conseguiu baixar anexo com sucesso",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(int downloadId, int statusCode, String errMsg) {
                                super.onFailure(downloadId, statusCode, errMsg);
                                Toast.makeText(context,"Erro ao baixar anexos",Toast.LENGTH_LONG).show();
                            }
                        });
                manager.add(request);
                Log.d("atividade - anexos", "url do anexo: " + url);
            } else
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("call.fragments.action").putExtra("ID", 6));
                Log.d("atividade - anexos",filePath+" já existe e não baixado");
//                downloadAttachment("http://stuffpoint.com/stardoll/image/54056-stardoll-sdfs.jpg" + a.getNmSystem(), a.getNmFile());
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

            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
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
