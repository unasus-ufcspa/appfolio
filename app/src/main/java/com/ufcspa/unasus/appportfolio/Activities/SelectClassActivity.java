package com.ufcspa.unasus.appportfolio.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.ufcspa.unasus.appportfolio.Model.SelectClassAdapter;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.Model.Team;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.util.List;

/**
 * Essa classe controla é responsável por armazenar qualquer
 * informações realacionadas as turmas do aluno logado. Sendo
 * possível selecionar a turma desejada.
 */
public class SelectClassActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    private GridView grid_classes;
    private List<Team> classes;
    private DataBaseAdapter source;
    private Singleton singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);

        init();
    }

    private void init() {
        singleton = Singleton.getInstance();

        source = new DataBaseAdapter(getApplicationContext());
        classes = source.getClasses();

        SelectClassAdapter gridAdapter = new SelectClassAdapter(this, classes);

        grid_classes = (GridView) findViewById(R.id.grid_classes);
        grid_classes.setAdapter(gridAdapter);

        grid_classes.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        singleton.team = classes.get(position);
        Log.d("BANCO", "ID da Turma " + singleton.team.getIdClass());
        Toast.makeText(getApplicationContext(),"clicou em:"+classes.get(position).getCode(),Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,SelectPortfolioActivity.class));
        finish();
    }
}
