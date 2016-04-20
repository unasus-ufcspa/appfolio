package com.ufcspa.unasus.appportfolio.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentAttachmentDialog;
import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.R;

import java.util.List;

/**
 * Created by Arthur Zettler on 16/03/2016.
 */
public class FragmentAttachmentDialogAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private FragmentAttachmentDialog context;
    private List<Attachment> attachments;


    public FragmentAttachmentDialogAdapter(FragmentAttachmentDialog context, List<Attachment> attachment) {
        this.context = context;
        this.attachments = attachment;
        inflater = (LayoutInflater) context.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return attachments.size();
    }

    @Override
    public Object getItem(int position) {
        return attachments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.celladapter_fragment_attachment, null);

        final Holder components = new Holder();
        components.imgAttachment = (ImageView) rowView.findViewById(R.id.img_attachment);
        components.descAttachment = (TextView) rowView.findViewById(R.id.desc_attachment);
        components.imgDelete = (ImageView) rowView.findViewById(R.id.img_delete);
        components.imgDelete.setVisibility(View.GONE);

        final Attachment aux = attachments.get(position);

        switch (aux.getTpAttachment()) {
            case "I":
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.imageClicked(position);
                    }
                });
                components.imgAttachment.setImageResource(R.drawable.attachment_image);
                components.descAttachment.setText(aux.getNmFile());
                break;
            case "V":
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.videoClicked(position);
                    }
                });
                components.imgAttachment.setImageResource(R.drawable.attachment_video);
                components.descAttachment.setText(aux.getNmFile());
                break;
            case "T":
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.textClicked(position);
                    }
                });
                components.imgAttachment.setImageResource(R.drawable.attachment_file);
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