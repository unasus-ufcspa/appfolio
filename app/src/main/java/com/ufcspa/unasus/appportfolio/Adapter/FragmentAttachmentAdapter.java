package com.ufcspa.unasus.appportfolio.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentAttachment;
import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.R;

import java.util.List;

/**
 * Created by desenvolvimento on 10/12/2015.
 */
public class FragmentAttachmentAdapter extends BaseAdapter {
    private FragmentAttachment context;
    private List<Attachment> attachments;
    private static LayoutInflater inflater = null;


    public FragmentAttachmentAdapter(FragmentAttachment context, List<Attachment> attachment) {
        this.context = context;
        this.attachments = attachment;
        inflater = (LayoutInflater) context.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class Holder {
        ImageView imgAttachment;
        TextView descAttachment;
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

        Holder components = new Holder();
        components.imgAttachment = (ImageView) rowView.findViewById(R.id.img_attachment);
        components.descAttachment = (TextView) rowView.findViewById(R.id.desc_attachment);

        Attachment aux = attachments.get(position);

        switch (aux.getType()) {
            case "I":
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.imageClicked(position);
                    }
                });
                components.imgAttachment.setImageResource(R.drawable.attachment_image);
                components.descAttachment.setText("Imagem");
                break;
            case "V":
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.videoClicked(position);
                    }
                });
                components.imgAttachment.setImageResource(R.drawable.attachment_video);
                components.descAttachment.setText("Video");
                break;
            case "T":
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.textClicked(position);
                    }
                });
                components.imgAttachment.setImageResource(R.drawable.attachment_file);
                components.descAttachment.setText("Text");
                break;
            default:
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.plusClicked(position);
                    }
                });
                components.imgAttachment.setImageResource(R.drawable.attachment_plus);
                components.descAttachment.setText("Adicionar novo");
                break;
        }

        return rowView;
    }
}