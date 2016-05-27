package com.ufcspa.unasus.appportfolio.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Activities.MainActivity;
import com.ufcspa.unasus.appportfolio.Model.DividerItemDecoration;
import com.ufcspa.unasus.appportfolio.Model.StudFrPortClass;
import com.ufcspa.unasus.appportfolio.R;

import java.util.List;

/**
 * Created by UNASUS on 10/11/2015.
 */
public class StudentActivitiesAdapter extends BaseAdapter
{
    private static LayoutInflater inflater = null;
    private MainActivity context;
    private List<StudFrPortClass> list;

    public StudentActivitiesAdapter(MainActivity context, List<StudFrPortClass> list)
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
    public StudFrPortClass getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.adapter_portfolio_activity, null);

        StudFrPortClass aux = list.get(position);

        holder.recyclerView = (RecyclerView) rowView.findViewById(R.id.activities_list);
        holder.recyclerView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of  child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL_LIST, 100));

        holder.recyclerView.setAdapter(new ActivitiesAdapter(context, aux.getListActivities(), aux.getNameStudent(), aux));

        holder.studentName = (TextView) rowView.findViewById(R.id.p_student_name);
        holder.studentName.setText(aux.getNameStudent());

        holder.studentPhoto = (ImageView) rowView.findViewById(R.id.student_image);
        Bitmap bitmap = aux.getPhoto();
        if (bitmap != null)
            holder.studentPhoto.setImageBitmap(bitmap);

        return rowView;
    }

    public class Holder
    {
        RecyclerView recyclerView;
        TextView studentName;
        ImageView studentPhoto;
    }
}
