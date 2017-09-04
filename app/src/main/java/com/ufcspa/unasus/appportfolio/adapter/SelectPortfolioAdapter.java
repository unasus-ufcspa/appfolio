package com.ufcspa.unasus.appportfolio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.model.PortfolioClass;
import com.ufcspa.unasus.appportfolio.R;

import java.util.List;

/**
 * Created by Desenvolvimento on 17/11/2015.
 */
public class SelectPortfolioAdapter extends BaseAdapter {
    private Context mContext;
    private List<PortfolioClass> mPortfoliolist;
    private static LayoutInflater sInflater = null;


    public SelectPortfolioAdapter(Context mContext, List<PortfolioClass> port) {
        this.mContext = mContext;
        this.mPortfoliolist = port;
        sInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class Components {
        TextView txtClassCode;
        TextView txtNameStudent;
    }


    @Override
    public int getCount() {
        return mPortfoliolist.size();
    }

    @Override
    public Object getItem(int position) {
        return mPortfoliolist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Components components = new Components();
        View rowView;
        rowView = sInflater.inflate(R.layout.celladapter_acitivity_portfolios, null);
        PortfolioClass aux = mPortfoliolist.get(position);
        // components.txtClassCode=(TextView)rowView.findViewById(R.id.adapater_txt_portfolio_classCode);
        components.txtNameStudent = (TextView) rowView.findViewById(R.id.adapater_txt_portfolio_nameStudent);
        components.txtNameStudent.setText(aux.getStudentName());
        // components.txtClassCode.setText("");
        return rowView;
    }
}
