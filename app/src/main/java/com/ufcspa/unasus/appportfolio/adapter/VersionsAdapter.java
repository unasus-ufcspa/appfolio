package com.ufcspa.unasus.appportfolio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.database.DataBase;
import com.ufcspa.unasus.appportfolio.model.Singleton;
import com.ufcspa.unasus.appportfolio.model.VersionActivity;
import com.ufcspa.unasus.appportfolio.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Arthur Zettler on 31/03/2016.
 */
public class VersionsAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Context context;
    private ArrayList<VersionActivity> list;
    private Holder holder;
    private DataBase source;

    public VersionsAdapter(Context context, ArrayList<VersionActivity> list) {
        this.context = context;
        this.list = list;
        this.holder = new Holder();
        this.source = DataBase.getInstance(context);

        if (this.list.size()>1) {
            Collections.sort(this.list.subList(1,this.list.size()-1));
        }else{
            Collections.sort(this.list);
        }

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public VersionActivity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.item_version, null);

        VersionActivity aux = list.get(position);

        holder.date = (TextView) convertView.findViewById(R.id.version_date);
        holder.time = (TextView) convertView.findViewById(R.id.version_time);
        holder.versionNumber = (TextView) convertView.findViewById(R.id.version_number);
        holder.notificationIcon = (TextView) convertView.findViewById(R.id.specific_comment_notice_version);

        String[] dateAndTime = null;
        if (aux.getDt_submission() != null) {
            dateAndTime = aux.getDt_submission().split(" ");
        } else {
            dateAndTime = aux.getDt_last_access().split(" ");
        }

        if (dateAndTime != null) {
            if (position == 0 && Singleton.getInstance().portfolioClass.getPerfil().equals("S")) {
                holder.date.setText("Versão Atual");
                holder.time.setVisibility(View.GONE);
            } else {
                String[] date = dateAndTime[0].split("-");
                holder.date.setText(date[2]+"/"+date[1]+"/"+date[0]);
                String[] time = dateAndTime[1].split(":");
                holder.time.setText(time[0]+":"+time[1]);
            }
        }

        holder.versionNumber.setText("" + (list.size() - position));

        int notifications = source.getSpecificCommentNotifications(aux.getId_activity_student(), aux.getId_version_activit_srv());
        if (notifications > 0)
            holder.notificationIcon.setText(String.valueOf(notifications));
        else
            holder.notificationIcon.setVisibility(View.GONE);

        return convertView;
    }

    public void refresh(ArrayList<VersionActivity> versionActivities) {
        this.list = versionActivities;

        if (this.list.size()>1) {
            Collections.sort(this.list.subList(1,this.list.size()-1));
        }else{
            Collections.sort(this.list);
        }

        notifyDataSetChanged();
    }

    public class Holder {
        TextView date;
        TextView time;
        TextView versionNumber;
        TextView notificationIcon;
    }
}
