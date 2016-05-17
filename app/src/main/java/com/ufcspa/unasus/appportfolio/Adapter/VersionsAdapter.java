package com.ufcspa.unasus.appportfolio.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Model.VersionActivity;
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

    public VersionsAdapter(Context context, ArrayList<VersionActivity> list) {
        this.context = context;
        this.list = list;
        this.holder = new Holder();

        Collections.sort(this.list);

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
            convertView = inflater.inflate(R.layout.adapter_versions, null);

        VersionActivity aux = list.get(position);

        holder.date = (TextView) convertView.findViewById(R.id.version_date);
        holder.time = (TextView) convertView.findViewById(R.id.version_time);
        holder.versionNumber = (TextView) convertView.findViewById(R.id.version_number);

        String[] dateAndTime = null;
        if (aux.getDt_submission() != null) {
            dateAndTime = aux.getDt_submission().split(" ");
        } else {
            dateAndTime = aux.getDt_last_access().split(" ");
        }

        if (dateAndTime != null) {
            if (position == 0) {
                holder.date.setText("Versão Atual");
                holder.time.setVisibility(View.GONE);
            } else {
                holder.date.setText(dateAndTime[0]);
                holder.time.setText(dateAndTime[1]);
            }
        }

        holder.versionNumber.setText("" + (list.size() - position));

        return convertView;
    }

    public void refresh(ArrayList<VersionActivity> versionActivities) {
        this.list = versionActivities;

        Collections.sort(this.list);

        notifyDataSetChanged();
    }

    public class Holder {
        TextView date;
        TextView time;
        TextView versionNumber;
    }
}
