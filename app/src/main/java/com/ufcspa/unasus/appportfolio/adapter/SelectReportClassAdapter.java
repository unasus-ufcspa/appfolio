package com.ufcspa.unasus.appportfolio.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.database.DataBase;
import com.ufcspa.unasus.appportfolio.model.PortfolioClass;
import com.ufcspa.unasus.appportfolio.model.Singleton;
import com.ufcspa.unasus.appportfolio.R;

import java.util.List;

/**
 * Created by Desenvolvimento on 08/01/2016.
 */
public class SelectReportClassAdapter extends BaseAdapter {
    private static LayoutInflater sInflater = null;
    private Context mContext;
    private List<PortfolioClass> mPortClassList;
    private DataBase mDataBase;

    public SelectReportClassAdapter(Context mContext, List<PortfolioClass> classes) {
        this.mContext = mContext;
        this.mPortClassList = classes;
        this.mDataBase = DataBase.getInstance(mContext);

        sInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mPortClassList.size();
    }

    @Override
    public PortfolioClass getItem(int position) {
        return mPortClassList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        final View rowView;

        rowView = sInflater.inflate(R.layout.item_class_finalized, null);

        holder.txtClassCodeInfo = (TextView) rowView.findViewById(R.id.txt_class_code_info);
        holder.btnInfoClose = (ImageButton) rowView.findViewById(R.id.btn_info_close);
        holder.txtFinalizeActivity = (TextView) rowView.findViewById(R.id.txt_finalize_activity);
        holder.txtSendMessage = (TextView) rowView.findViewById(R.id.txt_send_message);
        holder.txtPort = (TextView) rowView.findViewById(R.id.adapter_item_class_txv_ds_port);
        holder.txtClassCode = (TextView) rowView.findViewById(R.id.adapter_item_class_txv_code);
        holder.background = (TextView) rowView.findViewById(R.id.item_color_background);
        holder.btnInfo = (ImageButton) rowView.findViewById(R.id.btn_info);
        holder.notificationIcon = (TextView) rowView.findViewById(R.id.item_class_notification_icon);
        final PortfolioClass portClass = mPortClassList.get(position);

//        if(portClass.getPerfil().equals("S")){
//            holder.background.setBackgroundResource(R.color.base_green);
//            holder.txtClassCode.setTextColor(Color.GRAY);
//            holder.txt_port_title.setTextColor(Color.GRAY);
//            holder.txtPort.setTextColor(Color.GRAY);
//            holder.btnInfo.setImageResource(R.drawable.ic_hamburger_white);
//            holder.notificationIcon.setBackgroundResource(R.drawable.bg_green_rounded);
//            holder.notificationIcon.setVisibility(View.VISIBLE);
//        }
        holder.txtClassCode.setText(portClass.getClassCode());
        holder.txtPort.setText(portClass.getPortfolioTitle());

        holder.txtClassCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeScreen(portClass);
            }
        });
        holder.txtPort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeScreen(portClass);
            }
        });

//        int notifications = mDataBase.getPortfolioClassNotification(portClass.getIdPortClass());
//        if (notifications == 0)
        holder.notificationIcon.setVisibility(View.INVISIBLE);
//        else {
//            holder.notificationIcon.setVisibility(View.VISIBLE);
//            holder.notificationIcon.setText(notifications + "");
//        }

        return rowView;
    }

    private void changeScreen(PortfolioClass portClass) {
        Singleton singleton = Singleton.getInstance();
        singleton.portfolioClass = portClass;
        if (singleton.portfolioClass.getPerfil().equals("T")) {
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("call.fragments.action").putExtra("ID", 8));
        } else {
            singleton.idStudent = singleton.user.getIdUser();
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("call.fragments.action").putExtra("ID", 7));
        }
    }

    public class Holder {
        TextView txtClassCode;
        TextView txtPort;
        TextView background;
        ImageButton btnInfo;
        TextView notificationIcon;

        TextView txtClassCodeInfo;
        ImageButton btnInfoClose;
        TextView txtFinalizeActivity;
        TextView txtSendMessage;
    }
}

