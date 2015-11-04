package com.ufcspa.unasus.appportfolio.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ufcspa.unasus.appportfolio.DataBase.DatabaseHelper;
import com.ufcspa.unasus.appportfolio.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper dbh = new DatabaseHelper(this);
        dbh.getDatabase();
    }
}
