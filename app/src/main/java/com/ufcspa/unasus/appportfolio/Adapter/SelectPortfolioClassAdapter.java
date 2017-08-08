package com.ufcspa.unasus.appportfolio.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ufcspa.unasus.appportfolio.Model.PortfolioClass;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.util.List;

/**
 * Created by Desenvolvimento on 08/01/2016.
 */
public class SelectPortfolioClassAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Context context;
    private List<PortfolioClass> portclasses;
    private DataBaseAdapter dataBaseAdapter;

    public interface OnInfoButtonClick{
        void openInfo(View v,PortfolioClass portfolioClass);
    }

    private OnInfoButtonClick callback;

    public void setOnInfoButtonClickListener(OnInfoButtonClick listener){
        this.callback = listener;
    }

    public SelectPortfolioClassAdapter(Context context, List<PortfolioClass> classes)
    {
        this.context = context;
        this.portclasses = classes;
        this.dataBaseAdapter = DataBaseAdapter.getInstance(context);

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return portclasses.size();
    }

    @Override
    public PortfolioClass getItem(int position) {
        return portclasses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        final View rowView;

        rowView = inflater.inflate(R.layout.item_class, null);

        holder.txt_class_code_info = (TextView) rowView.findViewById(R.id.txt_class_code_info);
        holder.btnInfoClose = (ImageButton) rowView.findViewById(R.id.btn_info_close);
        holder.txt_finalize_activity = (TextView) rowView.findViewById(R.id.txt_finalize_activity);
        holder.txt_send_message = (TextView) rowView.findViewById(R.id.txt_send_message);
        holder.infoView = (RelativeLayout) rowView.findViewById(R.id.info);
        holder.txt_port = (TextView) rowView.findViewById(R.id.adapter_item_class_txv_ds_port);
        holder.txt_class_code=(TextView) rowView.findViewById(R.id.adapter_item_class_txv_code);
        holder.txt_port_title=(TextView) rowView.findViewById(R.id.adapter_item_class_txv_ds_title);
        holder.background=(TextView) rowView.findViewById(R.id.item_color_background);
        holder.btnInfo=(ImageButton) rowView.findViewById(R.id.btn_info);
        holder.notificationIcon = (TextView) rowView.findViewById(R.id.item_class_notification_icon);
        final PortfolioClass portClass=portclasses.get(position);

        if(portClass.getPerfil().equals("S")){
            holder.background.setBackgroundResource(R.color.base_green);
            holder.txt_class_code.setTextColor(Color.GRAY);
            holder.txt_port_title.setTextColor(Color.GRAY);
            holder.txt_port.setTextColor(Color.GRAY);
            holder.btnInfo.setImageResource(R.drawable.ic_hamburger_white);
            holder.notificationIcon.setBackgroundResource(R.drawable.bg_green_rounded);
            holder.notificationIcon.setVisibility(View.VISIBLE);
        }
        holder.txt_class_code.setText(portClass.getClassCode());
        holder.txt_class_code_info.setText(portClass.getClassCode());
        holder.txt_port_title.setText(portClass.getPortfolioTitle());

        holder.txt_class_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeScreen(portClass);
            }
        });
        holder.txt_port.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeScreen(portClass);
            }
        });
        holder.txt_port_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeScreen(portClass);
            }
        });

        holder.btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null){
                    callback.openInfo(v,portClass);
                }
               /* new AlertDialog.Builder(context)
                        .setTitle(portClass.getPortfolioTitle())
                        .setMessage(dataBaseAdapter.getPortfolioDescription(portClass.getIdPortClass()))
                        .setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();*/
//                holder.infoView.setVisibility(View.VISIBLE);
//                holder.infoView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_right));
            }
        });

        holder.btnInfoClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.infoView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_left));
                holder.infoView.setVisibility(View.GONE);
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

        int notifications = dataBaseAdapter.getPortfolioClassNotification(portClass.getIdPortClass());
        if (notifications == 0)
            holder.notificationIcon.setVisibility(View.INVISIBLE);
        else {
            holder.notificationIcon.setVisibility(View.VISIBLE);
            holder.notificationIcon.setText(notifications + "");
        }

        return rowView;
    }

    private void changeScreen(PortfolioClass portClass) {
        Singleton singleton = Singleton.getInstance();
        singleton.portfolioClass = portClass;
        singleton.guestUserComments = dataBaseAdapter.getFlGuest(portClass.getIdPortClass());
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("call.fragments.action").putExtra("ID", 1));
    }

    public class Holder
    {
        TextView txt_class_code;
        TextView txt_port_title;
        TextView txt_port;
        TextView background;
        ImageButton btnInfo;
        TextView notificationIcon;

        RelativeLayout infoView;
        TextView txt_class_code_info;
        ImageButton btnInfoClose;
        TextView txt_finalize_activity;
        TextView txt_send_message;
    }
}

