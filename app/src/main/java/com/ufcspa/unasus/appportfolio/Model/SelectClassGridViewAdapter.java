package com.ufcspa.unasus.appportfolio.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Activities.SelectClassActivity;
import com.ufcspa.unasus.appportfolio.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Convidado on 04/11/2015.
 */
public class SelectClassGridViewAdapter extends BaseAdapter
{
    private SelectClassActivity context;
    private List<Team> classes;

    private static LayoutInflater inflater = null;

    public SelectClassGridViewAdapter(SelectClassActivity context, List<Team> classes)
    {
        this.context = context;
        this.classes = classes;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return classes.size();
    }

    @Override
    public Team getItem(int position) {
        return classes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView txt_class_code;
        TextView txt_class_description;
        TextView txt_class_dates;
        TextView txt_class_status;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.celladapter_activity_classes, null);
        holder.txt_class_code = (TextView) rowView.findViewById(R.id.txt_activity_title);
        holder.txt_class_description = (TextView) rowView.findViewById(R.id.txt_activity_description);
        holder.txt_class_dates = (TextView) rowView.findViewById(R.id.txt_class_dates);
        holder.txt_class_status = (TextView) rowView.findViewById(R.id.txt_class_status);

        Team class_aux = classes.get(position);

        holder.txt_class_code.setText(class_aux.getCode());
        holder.txt_class_description.setText(class_aux.getDescription());

        String dateStart = new SimpleDateFormat("dd-MM-yyyy").format(class_aux.getDateStart());
        String dateFinish = new SimpleDateFormat("dd-MM-yyyy").format(class_aux.getDateFinish());

        holder.txt_class_dates.setText(dateStart + "/" + dateFinish);

        if(class_aux.getStatus() == 'A')
            holder.txt_class_status.setText("Ativo");
        else
            holder.txt_class_status.setText("Inativo");

        return rowView;
    }
}
