package com.ufcspa.unasus.appportfolio.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.mikepenz.crossfader.Crossfader;
import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragRef;
import com.ufcspa.unasus.appportfolio.R;


public class MainActivity extends AppCompatActivity {//implements View.OnTouchListener {

    private Crossfader crossFader;
    private View fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View bigDrawer = inflater.inflate(R.layout.big_drawer, null);
        View drawer = inflater.inflate(R.layout.mini_drawer, null);

        fragmentContainer = findViewById(R.id.fragment_container);

        crossFader = new Crossfader()
                .withContent(fragmentContainer)
                .withFirst(bigDrawer, 450)
                .withSecond(drawer, 150)
                .withSavedInstance(savedInstanceState)
                .build();

        FragRef fr = new FragRef();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fr).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = crossFader.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
}
