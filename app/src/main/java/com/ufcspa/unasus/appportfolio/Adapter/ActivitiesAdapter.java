package com.ufcspa.unasus.appportfolio.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ufcspa.unasus.appportfolio.Activities.EditActivity;
import com.ufcspa.unasus.appportfolio.Activities.SelectActivitiesActivity;
import com.ufcspa.unasus.appportfolio.Model.Activity;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by arthurzettler on 1/5/16.
 */
public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ViewHolder> {
    private List<Activity> list;
    private Context context;
    private Singleton singleton;
    private String studentName;

    public ActivitiesAdapter(Context context, List<Activity> list, String studName) {
        this.list = list;
        this.context = context;
        this.singleton = Singleton.getInstance();
        Collections.sort(this.list);
        this.studentName = studName;
    }

    @Override
    public ActivitiesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_portfolio_activity, parent, false);
        ViewHolder vh = new ViewHolder(v);

        vh.title = (TextView)v.findViewById(R.id.adapter_item_class_txv_ds_title);
        vh.moreInfo = (ImageButton)v.findViewById(R.id.btn_info);
        vh.description = "";
        vh.notificationIcon = (TextView)v.findViewById(R.id.item_notification_icon);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Activity aux = list.get(position);

        holder.title.setText(aux.getTitle());
        holder.description = aux.getDescription();
        holder.moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), holder.title.getText()+"\n"+ holder.description, Toast.LENGTH_SHORT).show();
            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.activity = list.get(position);
                singleton.idActivityStudent=singleton.activity.getIdAtivity();
                singleton.portfolioClass.setIdPortfolioStudent(list.get(position).getIdPortfolio());
                singleton.portfolioClass.setStudentName(studentName);
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("call.fragments.action").putExtra("ID",5));
            }
        });

        int r = (int) (Math.random() * (100 - 0)) + 0;
        if(r > 50)
            holder.notificationIcon.setVisibility(View.INVISIBLE);
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
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }
}
