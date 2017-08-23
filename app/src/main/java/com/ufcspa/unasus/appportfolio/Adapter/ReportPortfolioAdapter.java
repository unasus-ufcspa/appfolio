package com.ufcspa.unasus.appportfolio.Adapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Activities.MainActivity;
import com.ufcspa.unasus.appportfolio.Model.ActivityStudent;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.Database.DataBase;

import java.util.HashMap;
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
        rowView = inflater.inflate(R.layout.item_report, null);

        ActivityStudent aux = list.get(position);

        holder.titulo_report = (TextView) rowView.findViewById(R.id.titulo_report);
        holder.titulo_report.setText(DataBase.getInstance(rowView.getContext()).getActivityTitleByIdActivityStudent(aux.getIdActivityStudent()));
        holder.texto_report = (WebView) rowView.findViewById(R.id.texto_report);

        HashMap<String, String> attachment = DataBase.getInstance(rowView.getContext()).getAllAttachmentsNames(Singleton.getInstance().idActivityStudent);

        holder.texto_report.loadDataWithBaseURL("file:"+Environment.getExternalStorageDirectory()+"/Android/data/com.ufcspa.unasus.appportfolio/files/images",aux.getTxtActivity(),"text/html","UTF-8","about:blank");

        return rowView;
    }

    public class Holder
    {
        TextView titulo_report;
        WebView texto_report;
    }
}
