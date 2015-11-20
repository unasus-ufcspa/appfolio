package com.ufcspa.unasus.appportfolio.Activities;

import android.content.Intent;
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
import com.ufcspa.unasus.appportfolio.Adapter.SelectPortfolioAdapter;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.R;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import java.util.List;

public class SelectPortfolioActivity extends AppCompatActivity {
    private ListView listview;
    private List<PortfolioClass> portfolios;
    private DataBaseAdapter data;
    private Singleton single;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_portfolio);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        listview=(ListView)findViewById(R.id.selectPortfolioListView);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    public void init(){
        single = Singleton.getInstance();
        data=new DataBaseAdapter(this);
        try{
            portfolios=data.listarPortfolio(single.team.getIdClass(),single.user.getUserType(),single.user.getIdUser());
        }catch (Exception e){
            Log.e("BANCO","falha em pegar lista:"+e.getMessage());
        }
        if(portfolios.size() != 0 && portfolios != null) {
            SelectPortfolioAdapter adapter = new SelectPortfolioAdapter(getApplicationContext(), portfolios);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getApplicationContext(), "clicou em:" + portfolios.get(position).getStudentName(), Toast.LENGTH_SHORT).show();
                    single.portfolioClass = portfolios.get(position);
                    Log.d("BANCO", "ID do Portfolio " + single.portfolioClass.getIdPortfolioStudent());
                    startActivity(new Intent(getApplicationContext(),SelectActivitiesActivity.class));
                    //finish();
                }
            });
        }else
            Toast.makeText(getApplicationContext(),"Não há portfolios!",Toast.LENGTH_SHORT).show();
    }

}