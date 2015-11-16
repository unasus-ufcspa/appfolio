package com.ufcspa.unasus.appportfolio.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ufcspa.unasus.appportfolio.Model.SelectClassGridViewAdapter;
import com.ufcspa.unasus.appportfolio.Model.Team;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);

        init();
    }

    private void init() {
        source = new DataBaseAdapter(getApplicationContext());
        classes = source.getClasses();

        SelectClassGridViewAdapter gridAdapter = new SelectClassGridViewAdapter(this, classes);

        grid_classes = (GridView) findViewById(R.id.grid_classes);
        grid_classes.setAdapter(gridAdapter);

        grid_classes.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        System.out.println("Teste" + position);
    }
}
