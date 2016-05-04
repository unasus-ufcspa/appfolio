package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ufcspa.unasus.appportfolio.R;

import java.util.ArrayList;

/**
 * Created by Arthur Zettler on 05/04/2016.
 */
public class FragmentVersions extends Frag {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.versions_dialog, null);

        final ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            list.add("");

//        final ListView versionList = (ListView) view.findViewById(R.id.version_list);
//        versionList.setAdapter(new VersionsAdapter(getActivity(), list));

        return view;
    }
}
