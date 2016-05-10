package com.ufcspa.unasus.appportfolio.Activities.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ufcspa.unasus.appportfolio.Activities.MainActivity;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentConfig extends Frag implements View.OnClickListener {
    private Button btn_profile;
    private Button btn_password;

    public FragmentConfig() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_config, container, false);

        singleton = Singleton.getInstance();
        source = DataBaseAdapter.getInstance(getContext());

        init(view);

        return view;
    }

    private void init(View view) {
        btn_profile = (Button) view.findViewById(R.id.btn_profile);
        btn_password = (Button) view.findViewById(R.id.btn_password);

        btn_profile.setOnClickListener(this);
        btn_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_profile) {
            ((MainActivity) getActivity()).initChangeProfileFragment();
        }
        if (v.getId() == R.id.btn_password) {
            ((MainActivity) getActivity()).initChangePasswordFragment();
        }
    }
}