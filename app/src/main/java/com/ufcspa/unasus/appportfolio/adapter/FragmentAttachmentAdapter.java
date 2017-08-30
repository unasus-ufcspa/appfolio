package com.ufcspa.unasus.appportfolio.adapter;

import android.content.Context;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ufcspa.unasus.appportfolio.activities.fragments.AttachmentFragment;
import com.ufcspa.unasus.appportfolio.activities.MainActivity;
import com.ufcspa.unasus.appportfolio.model.Attachment;
import com.ufcspa.unasus.appportfolio.model.Singleton;
import com.ufcspa.unasus.appportfolio.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by desenvolvimento on 10/12/2015.
 */
public class FragmentAttachmentAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private AttachmentFragment context;
    private List<Attachment> attachments;
    private boolean canDelete;
    private Set<Attachment> shouldDelete;


    public FragmentAttachmentAdapter(AttachmentFragment context, List<Attachment> attachment) {
        this.context = context;
        this.attachments = attachment;
        this.shouldDelete = new HashSet<>();
        inflater = (LayoutInflater) context.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        canDelete = false;
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

        if (!canDelete)
            components.imgDelete.setVisibility(View.GONE);

        final Attachment aux = attachments.get(position);

        switch (aux.getTpAttachment()) {
            case "I":
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (canDelete)
                            checkIfIsMarked(components, aux);
                        else
                            context.imageClicked(position);
                    }
                });
                components.imgAttachment.setImageResource(R.drawable.ic_attachment_image);
                components.descAttachment.setText(aux.getNmFile());
                break;
            case "V":
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (canDelete)
                            checkIfIsMarked(components, aux);
                        else
                            context.videoClicked(position);
                    }
                });
                components.imgAttachment.setImageResource(R.drawable.ic_attachment_video);
                components.descAttachment.setText(aux.getNmFile());
                break;
            case "T":
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (canDelete)
                            checkIfIsMarked(components, aux);
                        else
                            context.textClicked(position);
                    }
                });
                components.imgAttachment.setImageResource(R.drawable.ic_attachment_file);
                components.descAttachment.setText(aux.getNmFile());
                break;
            default:
                if (!canDelete) {
                    rowView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            context.plusClicked();
                        }
                    });
                    components.imgAttachment.setImageResource(R.drawable.ic_attachment_plus);
                    components.descAttachment.setText("Adicionar novo");
                    components.imgDelete.setVisibility(View.GONE);
                }
                break;
        }

        if (!canDelete) {
            rowView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    canDelete = true;
                    if (!Singleton.getInstance().guestUser) {
                        context.deleteOneMedia(attachments.size() - 1);
                    }
                    context.getActivity().startActionMode(new ActionBarCallBack());
                    return true;
                }
            });
        }

        return rowView;
    }

    private void checkIfIsMarked(Holder components, Attachment attachment) {
        if (components.imgDelete.getVisibility() == View.VISIBLE) {
            components.imgDelete.setVisibility(View.GONE);
            shouldDelete.remove(attachment);
        } else {
            components.imgDelete.setVisibility(View.VISIBLE);
            shouldDelete.add(attachment);
        }
    }

    public void refresh(List<Attachment> attachments) {
        this.attachments = attachments;
        notifyDataSetChanged();
    }

    private class Holder {
        ImageView imgAttachment;
        ImageView imgDelete;
        TextView descAttachment;
    }

    public class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            context.deleteMedia(shouldDelete);
            mode.finish();
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.attachment, menu);
            ((MainActivity) context.getActivity()).hideDrawer();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            canDelete = false;
            context.createPlusButton();
            notifyDataSetChanged();
            ((MainActivity) context.getActivity()).showDrawer();
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }
    }
}