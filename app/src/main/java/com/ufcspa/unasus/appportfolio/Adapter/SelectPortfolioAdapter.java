package com.ufcspa.unasus.appportfolio.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Model.PortfolioClass;
import com.ufcspa.unasus.appportfolio.R;

import java.util.List;

/**
 * Created by Desenvolvimento on 17/11/2015.
 */
public class SelectPortfolioAdapter extends BaseAdapter {
    private Context context;
    private List<PortfolioClass> portfolios;
    private static LayoutInflater inflater = null;


    public SelectPortfolioAdapter(Context context, List<PortfolioClass> port) {
        this.context = context;
        this.portfolios = port;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class Components{
        TextView txt_class_code;
        TextView txt_name_student;
    }


    @Override
    public int getCount() {
        return portfolios.size();
    }

    @Override
    public Object getItem(int position) {
        return portfolios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Components  components= new Components();
        View rowView;
        rowView = inflater.inflate(R.layout.celladapter_acitivity_portfolios, null);
        PortfolioClass aux=portfolios.get(position);
        components.txt_class_code=(TextView)rowView.findViewById(R.id.adapater_txt_portfolio_classCode);
        components.txt_name_student=(TextView)rowView.findViewById(R.id.adapater_txt_portfolio_nameStudent);
        components.txt_name_student.setText(aux.getStudentName());
        components.txt_class_code.setText(aux.getClassCode() + " - " + aux.getPortfolioTitle());
        return rowView;
    }
}
