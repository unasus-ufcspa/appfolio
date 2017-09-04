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
    private static LayoutInflater sInflater = null;
    private AttachmentFragment mAttachmentFragment;
    private List<Attachment> mAttachmentList;
    private boolean mCanDelete;
    private Set<Attachment> mShouldDelete;


    public FragmentAttachmentAdapter(AttachmentFragment mAttachmentFragment, List<Attachment> attachment) {
        this.mAttachmentFragment = mAttachmentFragment;
        this.mAttachmentList = attachment;
        this.mShouldDelete = new HashSet<>();
        sInflater = (LayoutInflater) mAttachmentFragment.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCanDelete = false;
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

        if (!mCanDelete)
            components.imgDelete.setVisibility(View.GONE);

        final Attachment aux = mAttachmentList.get(position);

        switch (aux.getTpAttachment()) {
            case "I":
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCanDelete)
                            checkIfIsMarked(components, aux);
                        else
                            mAttachmentFragment.imageClicked(position);
                    }
                });
                components.imgAttachment.setImageResource(R.drawable.ic_attachment_image);
                components.descAttachment.setText(aux.getNmFile());
                break;
            case "V":
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCanDelete)
                            checkIfIsMarked(components, aux);
                        else
                            mAttachmentFragment.videoClicked(position);
                    }
                });
                components.imgAttachment.setImageResource(R.drawable.ic_attachment_video);
                components.descAttachment.setText(aux.getNmFile());
                break;
            case "T":
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCanDelete)
                            checkIfIsMarked(components, aux);
                        else
                            mAttachmentFragment.textClicked(position);
                    }
                });
                components.imgAttachment.setImageResource(R.drawable.ic_attachment_file);
                components.descAttachment.setText(aux.getNmFile());
                break;
            default:
                if (!mCanDelete) {
                    rowView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mAttachmentFragment.plusClicked();
                        }
                    });
                    components.imgAttachment.setImageResource(R.drawable.ic_attachment_plus);
                    components.descAttachment.setText("Adicionar novo");
                    components.imgDelete.setVisibility(View.GONE);
                }
                break;
        }

        if (!mCanDelete) {
            rowView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mCanDelete = true;
                    if (!Singleton.getInstance().guestUser) {
                        mAttachmentFragment.deleteOneMedia(mAttachmentList.size() - 1);
                    }
                    mAttachmentFragment.getActivity().startActionMode(new ActionBarCallBack());
                    return true;
                }
            });
        }

        return rowView;
    }

    private void checkIfIsMarked(Holder components, Attachment attachment) {
        if (components.imgDelete.getVisibility() == View.VISIBLE) {
            components.imgDelete.setVisibility(View.GONE);
            mShouldDelete.remove(attachment);
        } else {
            components.imgDelete.setVisibility(View.VISIBLE);
            mShouldDelete.add(attachment);
        }
    }

    public void refresh(List<Attachment> attachments) {
        this.mAttachmentList = attachments;
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
            mAttachmentFragment.deleteMedia(mShouldDelete);
            mode.finish();
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.attachment, menu);
            ((MainActivity) mAttachmentFragment.getActivity()).hideDrawer();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mCanDelete = false;
            mAttachmentFragment.createPlusButton();
            notifyDataSetChanged();
            ((MainActivity) mAttachmentFragment.getActivity()).showDrawer();
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }
    }
}