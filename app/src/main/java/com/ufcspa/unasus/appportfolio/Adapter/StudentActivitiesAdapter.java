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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentConfig;
import com.ufcspa.unasus.appportfolio.Activities.MainActivity;
import com.ufcspa.unasus.appportfolio.Model.DividerItemDecoration;
import com.ufcspa.unasus.appportfolio.Model.StudFrPortClass;
import com.ufcspa.unasus.appportfolio.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by UNASUS on 10/11/2015.
 */
public class StudentActivitiesAdapter extends BaseAdapter implements Filterable
{
    private static LayoutInflater inflater = null;
    private MainActivity context;
    private List<StudFrPortClass> originalList;
    private List<StudFrPortClass> filteredList;
    private ItemFilter itemFilter;

    public StudentActivitiesAdapter(MainActivity context, List<StudFrPortClass> originalList)
    {
        this.context = context;
        this.originalList = originalList;
        this.filteredList = originalList;

        itemFilter = new ItemFilter();

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public StudFrPortClass getItem(int position) {
        return filteredList.get(position);
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

        StudFrPortClass aux = filteredList.get(position);

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
            holder.studentPhoto.setImageBitmap(FragmentConfig.getRoundedRectBitmap(bitmap,100));

        return rowView;
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    public class Holder
    {
        RecyclerView recyclerView;
        TextView studentName;
        ImageView studentPhoto;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<StudFrPortClass> list = originalList;

            int count = list.size();
            final ArrayList<StudFrPortClass> nlist = new ArrayList<StudFrPortClass>(count);

            String filterableString ;

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
            filteredList = (ArrayList<StudFrPortClass>) results.values;
            notifyDataSetChanged();
        }

    }
}
