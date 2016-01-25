package com.ufcspa.unasus.appportfolio.Activities.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.ufcspa.unasus.appportfolio.Adapter.ReferenceAdapter;
import com.ufcspa.unasus.appportfolio.Model.Reference;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.util.ArrayList;

/**
 * Created by Desenvolvimento on 23/12/2015.
 */
public class FragRef extends Fragment {
    private Button btSave;
    private EditText edtRef;
    private ListView list;
    private ReferenceAdapter adapter;
    private Reference refSelected;
    private ArrayList<Reference> references;


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

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(salvar()){
                    //adapter.clearAdapter();
                    Reference r=new Reference();
                    r.setDsUrl(edtRef.getText().toString());
                    Toast.makeText(getActivity(),"Referências salvas com sucesso!",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(),"Erro ao salvar referências",Toast.LENGTH_SHORT).show();
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
        list=(ListView)getView().findViewById(R.id.frag_ref_listview);
    }

    private void gerarLista(){
        adapter= new ReferenceAdapter(getContext(),references);
        list.setAdapter(adapter);
        list.setClickable(true);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                refSelected = (Reference) adapter.getItem(position);
                registerForContextMenu(list);
                return true;
            }
        });
    }




    public boolean salvar(){
        boolean result = false;
        Singleton singleton = Singleton.getInstance();
        DataBaseAdapter data = DataBaseAdapter.getInstance(getActivity());
        Reference refer=new Reference();
        refer.setDsUrl(edtRef.getText().toString());
        refer.setIdActStudent(singleton.idActivityStudent);
        result=data.insertReference(refer);
        if(result==true){
            adapter.add(refer);
        }
        return result;
    }
    public void recuperar(){
     //get tx_reference from database
//        String tx="http://port.com";
//        edtRef.setText(tx);
        Singleton singleton = Singleton.getInstance();
        DataBaseAdapter data = DataBaseAdapter.getInstance(getActivity());
        references = (ArrayList)data.getReferences(singleton.idActivityStudent);
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
                Toast.makeText(getContext(),"Delete",Toast.LENGTH_SHORT).show();
                break;

            case 1:
                Toast.makeText(getContext(),"Refact",Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(getContext(),"go url",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(edtRef.getText().toString()));
                startActivity(i);
                break;
        }
        return super.onContextItemSelected(item);
    }






}
