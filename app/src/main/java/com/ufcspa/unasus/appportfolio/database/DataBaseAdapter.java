package com.ufcspa.unasus.appportfolio.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by UNASUS on 11/11/2015.
 */
public class DataBaseAdapter {
    /*
        DataBaseAdapter source;
        source = new DataBaseAdapter(getApplicationContext());
        Log.d("BANCO", "TABELAS = " + source.getTestData());
     */
    private SQLiteDatabase db;
    private DataBase helper;

    public DataBaseAdapter(Context context)
    {
        helper = new DataBase(context);
        db = helper.getDatabase();
    }

    public String getTestData()
    {
        //String sql ="SELECT name FROM sqlite_master WHERE type='table'";
        String sql ="SELECT Count(*) FROM tb_activity_student";

        Cursor mCur = db.rawQuery(sql, null);
        String tabelas = "";
        if (mCur.moveToFirst())
        {
            do {
                tabelas += mCur.getString(0) + "\n";
            }while(mCur.moveToNext());
        }
        return tabelas;
    }

}
