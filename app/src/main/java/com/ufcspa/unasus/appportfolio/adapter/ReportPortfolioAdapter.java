package com.ufcspa.unasus.appportfolio.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.activities.MainActivity;
import com.ufcspa.unasus.appportfolio.model.ActivityStudent;
import com.ufcspa.unasus.appportfolio.model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBase;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Steffano on 30/11/2016.
 */

public class ReportPortfolioAdapter extends BaseAdapter {
    private static LayoutInflater sInflater = null;
    private MainActivity mMainActivity;
    private List<ActivityStudent> mActivityStudentList;

    public ReportPortfolioAdapter(MainActivity mMainActivity, List<ActivityStudent> mActivityStudentList)
    {
        this.mMainActivity = mMainActivity;
        this.mActivityStudentList = mActivityStudentList;

        sInflater = (LayoutInflater) mMainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mActivityStudentList.size();
    }

    @Override
    public ActivityStudent getItem(int position) {
        return mActivityStudentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReportPortfolioAdapter.Holder holder = new ReportPortfolioAdapter.Holder();
        View rowView;
        rowView = sInflater.inflate(R.layout.item_report, null);

        ActivityStudent aux = mActivityStudentList.get(position);

        holder.tituloReport = (TextView) rowView.findViewById(R.id.titulo_report);
        holder.tituloReport.setText(DataBase.getInstance(rowView.getContext()).getActivityTitleByIdActivityStudent(aux.getIdActivityStudent()));
        holder.textoReport = (WebView) rowView.findViewById(R.id.texto_report);

        HashMap<String, String> attachment = DataBase.getInstance(rowView.getContext()).getAllAttachmentsNames(Singleton.getInstance().idActivityStudent);

        holder.textoReport.loadDataWithBaseURL("file:" + Environment.getExternalStorageDirectory() + "/Android/data/com.ufcspa.unasus.appportfolio/files/images", aux.getTxtActivity(), "text/html", "UTF-8", "about:blank");

        return rowView;
    }

    public class Holder {
        TextView tituloReport;
        WebView textoReport;
    }
}
