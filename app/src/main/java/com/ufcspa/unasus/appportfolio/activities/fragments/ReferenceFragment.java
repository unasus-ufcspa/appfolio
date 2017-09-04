package com.ufcspa.unasus.appportfolio.activities.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.ufcspa.unasus.appportfolio.activities.MainActivity;
import com.ufcspa.unasus.appportfolio.adapter.AnnotationAdapter;
import com.ufcspa.unasus.appportfolio.database.DataBase;
import com.ufcspa.unasus.appportfolio.model.Annotation;
import com.ufcspa.unasus.appportfolio.model.Reference;
import com.ufcspa.unasus.appportfolio.model.Singleton;
import com.ufcspa.unasus.appportfolio.model.Sync;
import com.ufcspa.unasus.appportfolio.R;

import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 23/12/2015.
 */
public class ReferenceFragment extends HelperFragment {
    private ImageButton mBtSave;
    private EditText mEdtRef;
    private ListView mListView;
//    private ReferenceAdapter mAnnotationAdapter;
    private AnnotationAdapter mAnnotationAdapter;
    private Reference mRefSelected;
    private Annotation mAnnotation;
    private ArrayList<Reference> mReferenceList;
    private ArrayList<Annotation> mAnnotationList;


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
        gerarLista();


        mBtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (salvar()) {
                    //mAnnotationAdapter.clearAdapter();
//                    Reference r=new Reference();
//                    r.setDsUrl(mEdtRef.getText().toString());
                    recuperar();
                    gerarLista();

                    if (isOnline()) {
                        MainActivity main = ((MainActivity) getActivity());
                        if (main != null) {
                            try {
                                main.sendFullData();
                            } finally {
                                Log.d("mAnnotation", "saved successfully");
                            }
                        }
                    }

                    Toast.makeText(getActivity(), "Anotações salvas com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Erro ao salvar anotações", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initComp() {
        mBtSave = (ImageButton) getView().findViewById(R.id.frag_ref_btSave);
        mEdtRef = (EditText) getView().findViewById(R.id.frag_ref_edt);
        mListView = (ListView) getView().findViewById(R.id.frag_ref_listview);
    }

    private void gerarLista() {
//        mAnnotationAdapter= new ReferenceAdapter(getContext(),mReferenceList);
        mAnnotationAdapter = new AnnotationAdapter(getContext(), mAnnotationList);
        mListView.setAdapter(mAnnotationAdapter);
        //para referências apenas
        /*mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAnnotation = mAnnotationAdapter.getItem(position);
                registerForContextMenu(mListView);
            }
        });*/

    }

    public boolean salvar() {
        int result = -1;
        Singleton singleton = Singleton.getInstance();
        DataBase data = DataBase.getInstance(getActivity());
//        Reference refer=new Reference();
        Annotation ann = new Annotation();
        ann.setDsAnnotation(mEdtRef.getText().toString());
        ann.setIdUser(singleton.user.getIdUser());
        result = data.insertAnnotation(ann);

        if (result > 0) {
            mAnnotationAdapter.add(ann);

            Sync sync = new Sync();
            sync.setNm_table("tb_annotation");
            sync.setCo_id_table(result);
            sync.setId_device(singleton.device.get_id_device());
            data.insertIntoTBSync(sync);

            return true;
        } else
            return false;
    }
    public void recuperar() {
     //get tx_reference from database
//        String tx="http://port.com";
//        mEdtRef.setText(tx);
        Singleton singleton = Singleton.getInstance();
        DataBase data = DataBase.getInstance(getActivity());
//        mReferenceList = (ArrayList)data.getReferences(singleton.idActivityStudent);
        mAnnotationList = (ArrayList) data.getAnnotations();
        //mAnnotationAdapter.refresh(mReferenceList);
        for (Annotation annotation: mAnnotationList) {
            Log.d("ReferenceFragment", "ref:" + annotation.toString());
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 0, 0, "Deletar");
        menu.add(0, 1, 0, "Alterar");
        menu.add(0, 2, 0, "Acessar url");
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //verify option
            case 0:
                Toast.makeText(getContext(), "Delete", Toast.LENGTH_SHORT).show();
//                source.deleteAnnotation(mAnnotation.getIdAnnotation());
                break;

            case 1:
                Toast.makeText(getContext(), "Refact", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(getContext(), "go url", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(mEdtRef.getText().toString()));
                startActivity(i);
                break;
        }
        return super.onContextItemSelected(item);
    }






}
