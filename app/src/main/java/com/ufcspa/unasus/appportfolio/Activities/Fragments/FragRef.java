package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ufcspa.unasus.appportfolio.R;

/**
 * Created by Desenvolvimento on 23/12/2015.
 */
public class FragRef extends Fragment {
    private Button btSave;
    private EditText edtRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reference, null);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        initComp();

        recuperar();

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(salvar()){
                    Toast.makeText(getActivity(),"Referências salvas com sucesso!",Toast.LENGTH_SHORT).show();
                }
                
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initComp(){
        btSave=(Button)getView().findViewById(R.id.frag_ref_btSave);
        edtRef=(EditText)getView().findViewById(R.id.frag_ref_edt);
    }

    public boolean salvar(){
        return true;
    }
    public void recuperar(){
     //get tx_reference from database
        String tx="http://port.com";
        edtRef.setText(tx);
    }






}
