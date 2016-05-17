package com.ufcspa.unasus.appportfolio.Activities.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ufcspa.unasus.appportfolio.Activities.MainActivity;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.WebClient.LogoutClient;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentConfig extends Frag implements View.OnClickListener {
    private static ProgressDialog dialog;
    private Button btn_profile;
    private Button btn_password;
    private Button btn_logout;

    public FragmentConfig() {
        // Required empty public constructor
    }

    public static void logout(FragmentActivity main) {
        ((MainActivity) main).logout();
    }

    public static void couldNotLogout() {
        dialog.dismiss();
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
        btn_logout = (Button) view.findViewById(R.id.btn_logout);

        btn_profile.setOnClickListener(this);
        btn_password.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_profile) {
            ((MainActivity) getActivity()).initChangeProfileFragment();
        }
        if (v.getId() == R.id.btn_password) {
            ((MainActivity) getActivity()).initChangePasswordFragment();
        }
        if (v.getId() == R.id.btn_logout) {
            if (isOnline()) {
                dialog = ProgressDialog.show(getContext(), "Saindo", "Por favor aguarde...", true);
                LogoutClient logoutClient = new LogoutClient(getContext(), getActivity());
                logoutClient.postJson();
            }
        }
    }
}
