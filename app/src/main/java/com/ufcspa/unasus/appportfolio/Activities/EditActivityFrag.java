package com.ufcspa.unasus.appportfolio.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ufcspa.unasus.appportfolio.R;

/**
 * Created by Desenvolvimento on 25/11/2015.
 */
public class EditActivityFrag extends FragmentActivity {

    private FragmentManager fragmentManager = getFragmentManager();
    FragmentEditText fragmentEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity_wih_fragment);

        //if(savedInstanceState==null){
            fragmentEditText=new FragmentEditText();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(R.id.activity_edit_acitivity_left_layout,fragmentEditText);
            ft.commit();
        //}
        String[] itens={"Editar Texto","Anexos","Referências","Comentários"};
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,itens);
        ListView listView = (ListView)findViewById(R.id.edit_activity_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        fragmentEditText =(FragmentEditText) fragmentManager.findFragmentById(R.id.fragment);
                        break;
                    case 1:

                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

    }
}
