package com.ufcspa.unasus.appportfolio.Activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

/**
 * Created by desenvolvimento on 10/12/2015.
 */
public class FragmentAttachment extends Frag {
    private RecyclerView recViewPhotos;
    private RecyclerView recViewVideos;
    private RecyclerView recViewOthers;

    private RecyclerView.Adapter mAdapter;

    public FragmentAttachment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attachment, null);

        init();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void init() {
        source = new DataBaseAdapter(getActivity());

        singleton = Singleton.getInstance();
        singleton.idActivityStudent = source.getActivityStudentID(singleton.activity.getIdAtivity(), singleton.portfolioClass.getIdPortfolioStudent());

        recViewPhotos = (RecyclerView) getView().findViewById(R.id.recview_photos);
        recViewVideos = (RecyclerView) getView().findViewById(R.id.recview_videos);
        recViewOthers = (RecyclerView) getView().findViewById(R.id.recview_others);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recViewPhotos.setLayoutManager(layoutManager);
        recViewVideos.setLayoutManager(layoutManager);
        recViewOthers.setLayoutManager(layoutManager);

        String[] teste = {"TESTE 1", "TESTE 2", "TESTE 3", "TESTE 4"};
//        mAdapter = new RecyclerView.Adapter(teste);
//        mAdapter = new SimpleAdapter(getActivity(),teste);
    }


}
