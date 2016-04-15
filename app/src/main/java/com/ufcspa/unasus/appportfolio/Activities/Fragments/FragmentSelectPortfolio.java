package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.ufcspa.unasus.appportfolio.Adapter.SelectPortfolioClassAdapter;
import com.ufcspa.unasus.appportfolio.Model.PortfolioClass;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.WebClient.SyncData;
import com.ufcspa.unasus.appportfolio.WebClient.SyncDataClient;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.util.List;

/**
 * Created by Desenvolvimento on 12/01/2016.
 */
public class FragmentSelectPortfolio extends Frag {
    public static boolean isSyncSucessful;
    public static boolean isSyncSincronizationNotSucessful;
    private GridView grid_classes;
    private DataBaseAdapter source;
    private Singleton singleton;
    private List<PortfolioClass> portclasses;

    void FragmentSelectPortfolio() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_classes, null);

        isSyncSucessful = false;
        isSyncSincronizationNotSucessful = false;

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        singleton = Singleton.getInstance();
        source = DataBaseAdapter.getInstance(getActivity());

        try {
            portclasses = source.selectListClassAndUserType(singleton.user.getIdUser());
            Log.d("lista", "tam portlis:" + portclasses.size());
        } catch (Exception e) {
            Log.wtf("ERRO", e.getMessage());
        }

        SelectPortfolioClassAdapter gridAdapter = new SelectPortfolioClassAdapter(getActivity(), portclasses);
        grid_classes = (GridView) getView().findViewById(R.id.grid_classes);
        grid_classes.setAdapter(gridAdapter);
//        downloadTBSync();
    }

    private void downloadTBSync() {
        if (isOnline()) {
            final Thread myThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    getSync();

                    while (!isSyncSucessful)
                        if (isSyncSincronizationNotSucessful)
                            break;

                    if (!isSyncSincronizationNotSucessful) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // Atualizar as notificações e o layout
                            }
                        });
                    } else {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Erro interno. Por favor tente novamente", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
            myThread.start();
        } else
        {
            Toast.makeText(getContext(), "Sem conexão com a internet", Toast.LENGTH_LONG).show();
            // Atualizar a interface
        }
    }

    private void getSync() {
        SyncDataClient client = new SyncDataClient(getContext());
        client.postJson(SyncData.toJSON(singleton.device.get_id_device()));
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
