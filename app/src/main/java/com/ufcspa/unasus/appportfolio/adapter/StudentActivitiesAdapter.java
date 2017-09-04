package com.ufcspa.unasus.appportfolio.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.activities.fragments.ConfigFragment;
import com.ufcspa.unasus.appportfolio.activities.MainActivity;
import com.ufcspa.unasus.appportfolio.model.DividerItemDecoration;
import com.ufcspa.unasus.appportfolio.model.StudFrPortClass;
import com.ufcspa.unasus.appportfolio.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by UNASUS on 10/11/2015.
 */
public class StudentActivitiesAdapter extends BaseAdapter implements Filterable {
    private static LayoutInflater sInflater = null;
    private MainActivity mMainActivity;
    private List<StudFrPortClass> mOriginalList;
    private List<StudFrPortClass> mFilteredList;
    private ItemFilter mItemFilter;
    private ActivitiesAdapter.OnInfoButtonClick mOnInfoButtonClick;

    public StudentActivitiesAdapter(MainActivity mMainActivity, List<StudFrPortClass> mOriginalList, ActivitiesAdapter.OnInfoButtonClick mOnInfoButtonClick) {
        this.mMainActivity = mMainActivity;
        this.mOriginalList = mOriginalList;
        this.mFilteredList = mOriginalList;
        this.mOnInfoButtonClick = mOnInfoButtonClick;

        mItemFilter = new ItemFilter();

        sInflater = (LayoutInflater) mMainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mFilteredList.size();
    }

    @Override
    public StudFrPortClass getItem(int position) {
        return mFilteredList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = sInflater.inflate(R.layout.item_portfolio_activity_list, null);

        StudFrPortClass aux = mFilteredList.get(position);

        holder.recyclerView = (RecyclerView) rowView.findViewById(R.id.activities_list);
        holder.recyclerView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of  child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(mMainActivity, LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.addItemDecoration(new DividerItemDecoration(mMainActivity, DividerItemDecoration.HORIZONTAL_LIST, 100));

        ActivitiesAdapter activitiesAdapter = new ActivitiesAdapter(mMainActivity, aux.getListActivities(), aux.getNameStudent(), aux);
        activitiesAdapter.setOnInfoButtonClickListener(mOnInfoButtonClick);
        holder.recyclerView.setAdapter(activitiesAdapter);

        holder.studentName = (TextView) rowView.findViewById(R.id.p_student_name);
        holder.studentName.setText(aux.getNameStudent());

        holder.studentPhoto = (ImageView) rowView.findViewById(R.id.student_image);
        Bitmap bitmap = aux.getPhoto();
        if (bitmap != null)
            holder.studentPhoto.setImageBitmap(ConfigFragment.getRoundedRectBitmap(bitmap, 100));

        return rowView;
    }

    @Override
    public Filter getFilter() {
        return mItemFilter;
    }

    public class Holder {
        RecyclerView recyclerView;
        TextView studentName;
        ImageView studentPhoto;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<StudFrPortClass> list = mOriginalList;

            int count = list.size();
            final ArrayList<StudFrPortClass> nlist = new ArrayList<StudFrPortClass>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getNameStudent();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mFilteredList = (ArrayList<StudFrPortClass>) results.values;
            notifyDataSetChanged();
        }

    }
}
