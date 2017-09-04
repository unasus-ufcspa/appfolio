package com.ufcspa.unasus.appportfolio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.model.Annotation;
import com.ufcspa.unasus.appportfolio.R;

import java.util.List;

/**
 * Created by Desenvolvimento on 04/01/2016.
 */
public class AnnotationAdapter extends BaseAdapter {
    private TextView mTvAnnotation;
    private List<Annotation> mAnnotationList;
    private Context mContext;
    private static LayoutInflater sInflater = null;



    public AnnotationAdapter(Context ctxt, List mAnnotationList) {
        this.mContext = ctxt;
        this.mAnnotationList = mAnnotationList;
        sInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public void add(Annotation obj) {
        mAnnotationList.add(obj);
    }
    public void delete(int position) {
        mAnnotationList.remove(position);
    }


    public int getCount() {
        return this.mAnnotationList.size();
    }

    public Annotation getItem(int index) {
        return this.mAnnotationList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return (long) mAnnotationList.get(position).getIdAnnotation();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        rowView = sInflater.inflate(R.layout.item_reference, null);


        Annotation a = getItem(position);

        mTvAnnotation = (TextView) rowView.findViewById(R.id.ref_item_txt_url);
        mTvAnnotation.setText(a.getDsAnnotation());
        return rowView;
    }

    public void clearAdapter() {
        mAnnotationList.clear();
        notifyDataSetChanged();
    }

    public void refresh(List a) {
        this.mAnnotationList = a;
    }
}
