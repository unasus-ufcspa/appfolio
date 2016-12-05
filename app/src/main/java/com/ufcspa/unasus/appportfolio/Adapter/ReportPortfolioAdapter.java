package com.ufcspa.unasus.appportfolio.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Activities.MainActivity;
import com.ufcspa.unasus.appportfolio.Model.ActivityStudent;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.util.List;

/**
 * Created by Steffano on 30/11/2016.
 */

public class ReportPortfolioAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private MainActivity context;
    private List<ActivityStudent> list;

    public ReportPortfolioAdapter(MainActivity context, List<ActivityStudent> list)
    {
        this.context = context;
        this.list = list;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ActivityStudent getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReportPortfolioAdapter.Holder holder = new ReportPortfolioAdapter.Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.adapter_report_item, null);

        ActivityStudent aux = list.get(position);

        holder.titulo_report = (TextView) rowView.findViewById(R.id.titulo_report);
        holder.titulo_report.setText(DataBaseAdapter.getInstance(rowView.getContext()).getActivityTitleByIdActivityStudent(aux.getIdActivityStudent()));
        holder.texto_report = (TextView) rowView.findViewById(R.id.texto_report);
        holder.texto_report.setText(aux.getTxtActivity());

        return rowView;
    }

    public class Holder
    {
        TextView titulo_report;
        TextView texto_report;
    }
}
