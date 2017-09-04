package com.ufcspa.unasus.appportfolio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.activities.fragments.AttachmentDialogFragment;
import com.ufcspa.unasus.appportfolio.model.Attachment;
import com.ufcspa.unasus.appportfolio.R;

import java.util.List;

/**
 * Created by Arthur Zettler on 16/03/2016.
 */
public class FragmentAttachmentDialogAdapter extends BaseAdapter {
    private static LayoutInflater sInflater = null;
    private AttachmentDialogFragment mAttachmentDialogFragment;
    private List<Attachment> mAttachmentList;


    public FragmentAttachmentDialogAdapter(AttachmentDialogFragment mAttachmentDialogFragment, List<Attachment> attachment) {
        this.mAttachmentDialogFragment = mAttachmentDialogFragment;
        this.mAttachmentList = attachment;
        sInflater = (LayoutInflater) mAttachmentDialogFragment.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mAttachmentList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAttachmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View rowView = sInflater.inflate(R.layout.celladapter_fragment_attachment, null);

        final Holder components = new Holder();
        components.imgAttachment = (ImageView) rowView.findViewById(R.id.img_attachment);
        components.descAttachment = (TextView) rowView.findViewById(R.id.desc_attachment);
        components.imgDelete = (ImageView) rowView.findViewById(R.id.img_delete);
        components.imgDelete.setVisibility(View.GONE);

        final Attachment aux = mAttachmentList.get(position);

        switch (aux.getTpAttachment()) {
            case "I":
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAttachmentDialogFragment.imageClicked(position);
                    }
                });
                components.imgAttachment.setImageResource(R.drawable.ic_attachment_image);
                components.descAttachment.setText(aux.getNmFile());
                break;
            case "V":
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAttachmentDialogFragment.videoClicked(position);
                    }
                });
                components.imgAttachment.setImageResource(R.drawable.ic_attachment_video);
                components.descAttachment.setText(aux.getNmFile());
                break;
            case "T":
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAttachmentDialogFragment.textClicked(position);
                    }
                });
                components.imgAttachment.setImageResource(R.drawable.ic_attachment_file);
                components.descAttachment.setText(aux.getNmFile());
                break;
            default:
                break;
        }

        return rowView;
    }


    private class Holder {
        ImageView imgAttachment;
        ImageView imgDelete;
        TextView descAttachment;
    }
}