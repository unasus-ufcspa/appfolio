package com.ufcspa.unasus.appportfolio.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.ufcspa.unasus.appportfolio.Adapter.SelectPortfolioClassAdapter;
import com.ufcspa.unasus.appportfolio.Model.PortfolioClass;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.util.List;

/**
 * Essa classe controla é responsável por armazenar qualquer
 * informações realacionadas as turmas do aluno logado. Sendo
 * possível selecionar a turma desejada.
 */
public class SelectClassActivity extends AppActivity implements AdapterView.OnItemClickListener
{
    private GridView grid_classes;
    private DataBaseAdapter source;
    private Singleton singleton;
    private List<PortfolioClass> portclasses;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);
        getSupportActionBar().hide();

        init();
        createDrawer(savedInstanceState);
    }

    private void init() {
        singleton = Singleton.getInstance();

        source = new DataBaseAdapter(getApplicationContext());
        try {
            //classes = source.getClasses(singleton.user.getIdUser(), singleton.user.getUserType());
            portclasses=source.selectListClassAndUserType(singleton.user.getIdUser());
            Log.d("lista","tam portlis:"+portclasses.size());

            //source.close();
        }catch (Exception e){

            Log.wtf("ERRO",e.getMessage());
        }

//        ArrayList<String>codTurmas = new ArrayList<String>();
//        for(int i=0;i<classes.size();i++){
//            if(!codTurmas.contains(classes.get(i).getCode()))
//                codTurmas.add(classes.get(i).getCode());
//        }
//        Log.d("Class:","cod Turmas:"+codTurmas.toString());
//        LinkedHashMap<String,Team> itens = new LinkedHashMap<>(classes.size());
//        for(int i=0;i<classes.size();i++){
//            itens.put(classes.get(i).getCode(),classes.get(i));
//        }
//        Log.d("Class","itens hashmap"+itens.toString());



        //SelectClassAdapter gridAdapter = new SelectClassAdapter(this, classes);
        SelectPortfolioClassAdapter gridAdapter= new SelectPortfolioClassAdapter(this,portclasses);
        grid_classes = (GridView) findViewById(R.id.grid_classes);
        grid_classes.setAdapter(gridAdapter);
        grid_classes.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        singleton.portfolioClass = portclasses.get(position);
        Log.d("BANCO", "ID do portfolioClass " + singleton.portfolioClass.getIdPortClass());
        Toast.makeText(getApplicationContext(),"clicou em:"+portclasses.get(position).getClassCode(),Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,SelectActivitiesActivity.class));
        //finish();
    }
}
