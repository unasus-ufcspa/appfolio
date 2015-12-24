package com.ufcspa.unasus.appportfolio.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by desenvolvimento on 10/12/2015.
 */
public class FragmentAttachmentAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList<Attachment> data = new ArrayList();

    public FragmentAttachmentAdapter(Context context, int layoutResourceId, ArrayList<Attachment> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.text = (TextView) row.findViewById(R.id.textView);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Attachment item = data.get(position);
//        Picasso.with(context).load(item.getLocalPath()).resize(100, 100).centerCrop().into(holder.image);
        //holder.image.setImageBitmap(decodeFile(item.getLocalPath()));
        holder.text.setText(item.getLocalPath());
        return row;
    }

    static class ViewHolder {
        TextView text;
    }

    private Bitmap decodeFile(String f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1 = new FileInputStream(f);
            BitmapFactory.decodeStream(stream1, null, o);
            stream1.close();

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < 100
                        || height_tmp / 2 < 100)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}/*RecyclerView.Adapter<FragmentAttachmentAdapter.ViewHolder> {

    private List<Attachment> items;

    public FragmentAttachmentAdapter(List<Attachment> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.celladapter_fragment_attachment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Attachment item = items.get(position);
        Bitmap bitmap = BitmapFactory.decodeFile(item.getLocalPath());
        holder.image.setImageBitmap(bitmap);
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.img_attachment);
        }
    }
}*/
