package com.ufcspa.unasus.appportfolio.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ufcspa.unasus.appportfolio.Activities.SelectActivitiesActivity;
import com.ufcspa.unasus.appportfolio.Activities.SelectClassActivity;
import com.ufcspa.unasus.appportfolio.Model.PortfolioClass;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.Model.Team;
import com.ufcspa.unasus.appportfolio.R;

import java.util.List;

/**
 * Created by Desenvolvimento on 08/01/2016.
 */
public class SelectPortfolioClassAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Context context;
    private List<PortfolioClass> portclasses;

    public SelectPortfolioClassAdapter(Context context, List<PortfolioClass> classes)
    {
        this.context = context;
        this.portclasses = classes;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.adapter_item_class, null);
        holder.txt_class_code=(TextView) rowView.findViewById(R.id.adapter_item_class_txv_code);
        holder.txt_port_title=(TextView) rowView.findViewById(R.id.adapter_item_class_txv_ds_title);
        holder.background=(TextView) rowView.findViewById(R.id.item_color_background);
        holder.btnInfo=(ImageButton) rowView.findViewById(R.id.btn_info);
        holder.nofificationIcon=(TextView) rowView.findViewById(R.id.item_notification_icon);
        final PortfolioClass portClass=portclasses.get(position);

        if(position%2 == 0)
            holder.nofificationIcon.setVisibility(View.INVISIBLE);

        if(portClass.getPerfil().equals("S")){
            holder.background.setBackgroundResource(R.color.base_green);
            holder.txt_class_code.setTextColor(Color.GRAY);
            holder.txt_port_title.setTextColor(Color.WHITE);
            holder.btnInfo.setBackgroundResource(R.color.base_green);
            holder.nofificationIcon.setBackgroundResource(R.drawable.rounded_student);
            holder.nofificationIcon.setVisibility(View.VISIBLE);
        }
        holder.txt_class_code.setText(portClass.getClassCode());
        holder.txt_port_title.setText(portClass.getPortfolioTitle());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton singleton = Singleton.getInstance();
                singleton.portfolioClass = portClass;
                context.startActivity(new Intent(context, SelectActivitiesActivity.class));
            }
        });

        holder.btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, portClass.getClassCode() + "\n" + portClass.getPortfolioTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        return rowView;
    }

    public class Holder
    {
        TextView txt_class_code;
        TextView txt_port_title;
        TextView background;
        ImageButton btnInfo;
        TextView nofificationIcon;
    }
}

