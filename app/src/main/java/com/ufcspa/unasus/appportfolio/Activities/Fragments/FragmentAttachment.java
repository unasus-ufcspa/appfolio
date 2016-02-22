package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.ufcspa.unasus.appportfolio.Adapter.FragmentAttachmentAdapter;
import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.util.ArrayList;

/**
 * Created by desenvolvimento on 10/12/2015.
 */
public class FragmentAttachment extends Frag {

    private GridView attachmentGrid;
    private FragmentAttachmentAdapter listAdapter;
    private ArrayList<Attachment> attachments;

    private boolean isRTEditor;

    public FragmentAttachment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attachment, null);

        if (getArguments() != null)
            isRTEditor = getArguments().getBoolean("RTEditor", false);
        else
            isRTEditor = false;

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init()
    {
        source = DataBaseAdapter.getInstance(getActivity());

        singleton = Singleton.getInstance();

        attachmentGrid = (GridView) getView().findViewById(R.id.attachment_gridview);

        attachments = source.getAttachmentsFromActivityStudent(singleton.idActivityStudent);
        createPlusButton();

        listAdapter = new FragmentAttachmentAdapter(this, attachments);
        attachmentGrid.setAdapter(listAdapter);
    }

    public void imageClicked(int position) {

    }

    public void videoClicked(int position) {

    }

    public void textClicked(int position) {

    }

    public void plusClicked(int position) {
        addAttachmentToComments();
        attachments = source.getAttachmentsFromActivityStudent(singleton.idActivityStudent);
        createPlusButton();
    }

    private void createPlusButton() {
        if (!isRTEditor)
            attachments.add(new Attachment(-1, -1, -1, "", "", ""));
    }
}
