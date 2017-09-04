package com.ufcspa.unasus.appportfolio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.model.Reference;
import com.ufcspa.unasus.appportfolio.R;

import java.util.List;

/**
 * Created by Desenvolvimento on 04/01/2016.
 */
public class ReferenceAdapter extends BaseAdapter {
    private TextView mReference;
    private List<Reference> mRefList;
    private Context mContext;
    private static LayoutInflater sInflater = null;


    public ReferenceAdapter(Context ctxt, List refeList) {
        this.mContext = ctxt;
        this.mRefList = refeList;
        sInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public void add(Reference obj) {
        mRefList.add(obj);
    }

    public void delete(int position) {
        mRefList.remove(position);
    }


    public int getCount() {
        return this.mRefList.size();
    }

    public Reference getItem(int index) {
        return this.mRefList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return (long) mRefList.get(position).getIdRef();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        rowView = sInflater.inflate(R.layout.item_reference, null);

        Reference ref = getItem(position);

        mReference = (TextView) rowView.findViewById(R.id.ref_item_txt_url);
        mReference.setText(ref.getDsUrl());
        return rowView;
    }

    public void clearAdapter() {
        mRefList.clear();
        notifyDataSetChanged();
    }

    public void refresh(List r) {
        this.mRefList = r;
    }
}
