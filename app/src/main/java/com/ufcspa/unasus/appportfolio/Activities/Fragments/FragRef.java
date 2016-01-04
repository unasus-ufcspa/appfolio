package com.ufcspa.unasus.appportfolio.Activities.Fragments;

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
import com.ufcspa.unasus.appportfolio.R;

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
                    adapter.clearAdapter();
                    Reference r=new Reference();
                    r.setDsUrl("http://yahoo.com.br");
                    adapter.add(r);
                    list.setAdapter(adapter);
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
        list=(ListView)getView().findViewById(R.id.frag_ref_listview);
        adapter= new ReferenceAdapter(getContext(),new ArrayList());
        Reference r =new Reference();
        r.setDsUrl("http://gmail.com");
        adapter.add(r);
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
        return true;
    }
    public void recuperar(){
     //get tx_reference from database
//        String tx="http://port.com";
//        edtRef.setText(tx);
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
                break;
        }
        return super.onContextItemSelected(item);
    }






}
