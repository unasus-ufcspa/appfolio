package com.ufcspa.unasus.appportfolio.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.R;

import java.util.List;

/**
 * Created by desenvolvimento on 10/12/2015.
 */
public class FragmentAttachmentAdapter extends RecyclerView.Adapter<FragmentAttachmentAdapter.ViewHolder> {

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
}
