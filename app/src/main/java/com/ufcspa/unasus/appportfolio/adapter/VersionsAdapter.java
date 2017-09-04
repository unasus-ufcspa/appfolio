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
    private static LayoutInflater sInflater = null;
    private Context mContext;
    private ArrayList<VersionActivity> mVersionList;
    private Holder mHolder;
    private DataBase mDataBase;

    public VersionsAdapter(Context mContext, ArrayList<VersionActivity> mVersionList) {
        this.mContext = mContext;
        this.mVersionList = mVersionList;
        this.mHolder = new Holder();
        this.mDataBase = DataBase.getInstance(mContext);

        if (this.mVersionList.size() > 1) {
            Collections.sort(this.mVersionList.subList(1, this.mVersionList.size() - 1));
        } else {
            Collections.sort(this.mVersionList);
        }

        sInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mVersionList.size();
    }

    @Override
    public VersionActivity getItem(int position) {
        return mVersionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = sInflater.inflate(R.layout.item_version, null);

        VersionActivity aux = mVersionList.get(position);

        mHolder.date = (TextView) convertView.findViewById(R.id.version_date);
        mHolder.time = (TextView) convertView.findViewById(R.id.version_time);
        mHolder.versionNumber = (TextView) convertView.findViewById(R.id.version_number);
        mHolder.notificationIcon = (TextView) convertView.findViewById(R.id.specific_comment_notice_version);

        String[] dateAndTime = null;
        if (aux.getDt_submission() != null) {
            dateAndTime = aux.getDt_submission().split(" ");
        } else {
            dateAndTime = aux.getDt_last_access().split(" ");
        }

        if (dateAndTime != null) {
            if (position == 0 && Singleton.getInstance().portfolioClass.getPerfil().equals("S")) {
                mHolder.date.setText("Versão Atual");
                mHolder.time.setVisibility(View.GONE);
            } else {
                String[] date = dateAndTime[0].split("-");
                mHolder.date.setText(date[2] + "/" + date[1] + "/" + date[0]);
                String[] time = dateAndTime[1].split(":");
                mHolder.time.setText(time[0] + ":" + time[1]);
            }
        }

        mHolder.versionNumber.setText("" + (mVersionList.size() - position));

        int notifications = mDataBase.getSpecificCommentNotifications(aux.getId_activity_student(), aux.getId_version_activit_srv());
        if (notifications > 0)
            mHolder.notificationIcon.setText(String.valueOf(notifications));
        else
            mHolder.notificationIcon.setVisibility(View.GONE);

        return convertView;
    }

    public void refresh(ArrayList<VersionActivity> versionActivities) {
        this.mVersionList = versionActivities;

        if (this.mVersionList.size() > 1) {
            Collections.sort(this.mVersionList.subList(1, this.mVersionList.size() - 1));
        } else {
            Collections.sort(this.mVersionList);
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
