package com.ufcspa.unasus.appportfolio.Adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ufcspa.unasus.appportfolio.Activities.MainActivity;
import com.ufcspa.unasus.appportfolio.Model.Activity;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.Model.StudFrPortClass;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

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
    private DataBaseAdapter source;
    private StudFrPortClass studFrPortClass;

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_portfolio_activity, parent, false);
        ViewHolder vh = new ViewHolder(v);

        vh.title = (TextView)v.findViewById(R.id.adapter_item_class_txv_ds_title);
        vh.moreInfo = (ImageButton)v.findViewById(R.id.btn_info);
        vh.description = "";
        vh.notificationIcon = (TextView) v.findViewById(R.id.item_class_notification_icon);

        vh.txt_class_code_info = (TextView) v.findViewById(R.id.txt_class_code_info);
        vh.btnInfoClose = (ImageButton) v.findViewById(R.id.btn_info_close);
        vh.txt_finalize_activity = (TextView) v.findViewById(R.id.txt_finalize_activity);
        vh.txt_send_message = (TextView) v.findViewById(R.id.txt_send_message);
        vh.infoView = (RelativeLayout) v.findViewById(R.id.info);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Activity aux = list.get(position);

        holder.title.setText(aux.getTitle());
        holder.txt_class_code_info.setText(aux.getTitle());
        holder.description = aux.getDescription();
        holder.moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                holder.infoView.setVisibility(View.VISIBLE);
//                holder.infoView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_right));

                new AlertDialog.Builder(context)
                        .setTitle(aux.getTitle())
                        .setMessage(aux.getDescription())
                        .setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
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
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("call.fragments.action").putExtra("ID", 6));
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

        int r = source.getActivityNotification(aux.getIdActivityStudent());
        if (r == 0) {
            holder.notificationIcon.setVisibility(View.INVISIBLE);
        } else {
            holder.notificationIcon.setVisibility(View.VISIBLE);
            holder.notificationIcon.setText(r + "");
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
}
