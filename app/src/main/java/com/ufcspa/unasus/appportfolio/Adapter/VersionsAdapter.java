package com.ufcspa.unasus.appportfolio.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ufcspa.unasus.appportfolio.R;

import java.util.List;

/**
 * Created by Arthur Zettler on 31/03/2016.
 */
public class VersionsAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Context context;
    private List<String> list;
    private ImageLoader imageLoader;

    public VersionsAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.adapter_versions, null);

        String aux = list.get(position);

        holder.date = (TextView) rowView.findViewById(R.id.version_date);
        holder.time = (TextView) rowView.findViewById(R.id.version_time);
        holder.img_version = (ImageView) rowView.findViewById(R.id.version_image);

        return rowView;
    }

    public class Holder {
        TextView date;
        TextView time;
        ImageView img_version;
    }
}
