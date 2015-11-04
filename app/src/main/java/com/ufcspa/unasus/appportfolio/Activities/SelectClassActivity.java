package com.ufcspa.unasus.appportfolio.Activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.ufcspa.unasus.appportfolio.DataBase.DatabaseHelper;
import com.ufcspa.unasus.appportfolio.Model.SelectClassGridViewAdapter;
import com.ufcspa.unasus.appportfolio.Model.Team;
import com.ufcspa.unasus.appportfolio.R;

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
public class SelectClassActivity extends AppCompatActivity
{
    private GridView grid_classes;
    private List<Team> classes;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_selection);

        fakeData();
        init();
    }

    private void init()
    {
        SelectClassGridViewAdapter gridAdapter = new SelectClassGridViewAdapter(this, classes);

        grid_classes = (GridView) findViewById(R.id.grid_classes);
        grid_classes.setAdapter(gridAdapter);
    }

    private void fakeData()
    {
        Date begin = null;
        Date finish = null;

        try
        {
            begin = new SimpleDateFormat("yyyy-MM-dd").parse("2015-11-15");
            finish = new SimpleDateFormat("yyyy-MM-dd").parse("2015-11-30");
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        Team c = new Team(0, "T 128", "Turma de Ciência da Computação da Faculdade de Informática da PUCRS", 'A', begin,finish);
        Team c2 = new Team(1, "T 320", "Turma de Informática Biomédica da UFCSPA", 'A', begin, finish);

        classes = new ArrayList();
        classes.add(c);
        classes.add(c2);
    }
}
