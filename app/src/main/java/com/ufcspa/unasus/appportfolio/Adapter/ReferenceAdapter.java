package com.ufcspa.unasus.appportfolio.Adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Model.Reference;
import com.ufcspa.unasus.appportfolio.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Desenvolvimento on 04/01/2016.
 */
public class ReferenceAdapter extends BaseAdapter {
    private TextView reference;
    private List<Reference> refs;
    private Context context;
    private static LayoutInflater inflater = null;



    public ReferenceAdapter(Context ctxt,List refeList){
        this.context=ctxt;
        this.refs=refeList;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public void add(Reference obj) {
        refs.add(obj);
        notifyDataSetChanged();
    }
    public void delete(int position){
        refs.remove(position);
        notifyDataSetChanged();
    }


    public int getCount() {
        return this.refs.size();
    }

    public Reference getItem(int index) {
        return this.refs.get(index);
    }

    @Override
    public long getItemId(int position) {
        return (long)refs.get(position).getIdRef();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
            View rowView;
            rowView = inflater.inflate(R.layout.reference_item, null);


        Reference ref = getItem(position);

        reference = (TextView) rowView.findViewById(R.id.ref_item_txt_url);
        reference.setText(ref.getDsUrl());
        return rowView;
    }

    public void clearAdapter(){
        refs.clear();
        notifyDataSetChanged();
    }
}
