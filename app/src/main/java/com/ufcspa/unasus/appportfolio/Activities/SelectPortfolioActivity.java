package com.ufcspa.unasus.appportfolio.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ufcspa.unasus.appportfolio.Model.PortfolioClass;
import com.ufcspa.unasus.appportfolio.Model.SelectPortfolioAdapter;
import com.ufcspa.unasus.appportfolio.Model.SingletonUser;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.util.List;

public class SelectPortfolioActivity extends AppCompatActivity {
    private ListView listview;
    private List<PortfolioClass> portfolios;
    private DataBaseAdapter data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_portfolio);
        listview=(ListView)findViewById(R.id.selectPortfolioListView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    public void init(){
        SingletonUser single =SingletonUser.getInstance();
        data=new DataBaseAdapter(this);
        try{
            portfolios=data.listarPortfolio(1,single.user.getUserType(),single.user.getIdUser());
        }catch (Exception e){
            Log.e("BANCO","falha em pegar lista:"+e.getMessage());
        }
        SelectPortfolioAdapter adapter = new SelectPortfolioAdapter(getApplicationContext(),portfolios);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"clicou em:"+portfolios.get(position).getStudentName(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
