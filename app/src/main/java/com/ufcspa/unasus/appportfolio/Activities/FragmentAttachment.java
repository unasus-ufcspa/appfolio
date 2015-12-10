package com.ufcspa.unasus.appportfolio.Activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.ufcspa.unasus.appportfolio.Adapter.FragmentAttachmentAdapter;
import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by desenvolvimento on 10/12/2015.
 */
public class FragmentAttachment extends Frag {
    private RecyclerView recViewPhotos;
    private RecyclerView recViewVideos;
    private RecyclerView recViewOthers;

    private List<Attachment> attachmentsPhotos;
    private List<Attachment> attachmentsVideos;
    private List<Attachment> attachmentsOthers;

    public FragmentAttachment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attachment, null);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
//        attachmentsPhotos = new ArrayList<>();
//        attachmentsVideos = new ArrayList<>();
//        attachmentsOthers = new ArrayList<>();
//
//        source = new DataBaseAdapter(getActivity());
//
//        singleton = Singleton.getInstance();
//        singleton.idActivityStudent = source.getActivityStudentID(singleton.activity.getIdAtivity(), singleton.portfolioClass.getIdPortfolioStudent());
//
//        recViewPhotos = (RecyclerView) getView().findViewById(R.id.recview_photos);
//        recViewVideos = (RecyclerView) getView().findViewById(R.id.recview_videos);
//        recViewOthers = (RecyclerView) getView().findViewById(R.id.recview_others);
//
//        LinearLayoutManager layoutManagerPhotos = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//        LinearLayoutManager layoutManagerVideos = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//        LinearLayoutManager layoutManagerOthers = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//
//        recViewPhotos.setLayoutManager(layoutManagerPhotos);
//        recViewVideos.setLayoutManager(layoutManagerVideos);
//        recViewOthers.setLayoutManager(layoutManagerOthers);

//        attachmentsPhotos = source.getAttachmentsFromActivityStudent(singleton.idActivityStudent, "I");
        //attachmentsVideos = source.getAttachmentsFromActivityStudent(singleton.idActivityStudent, "V");
        //attachmentsOthers = source.getAttachmentsFromActivityStudent(singleton.idActivityStudent, "O");

//        FragmentAttachmentAdapter adapterPhotos = new FragmentAttachmentAdapter(attachmentsPhotos);
        //FragmentAttachmentAdapter adapterVideos = new FragmentAttachmentAdapter(attachmentsVideos);
        //FragmentAttachmentAdapter adapterOthers = new FragmentAttachmentAdapter(attachmentsOthers);

//        recViewPhotos.setAdapter(adapterPhotos);
        //recViewVideos.setAdapter(adapterVideos);
        //recViewOthers.setAdapter(adapterOthers);
    }


}
