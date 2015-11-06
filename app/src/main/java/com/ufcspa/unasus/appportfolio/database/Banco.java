package com.ufcspa.unasus.appportfolio.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Desenvolvimento on 29/10/2015.
 */
public class Banco extends SQLiteOpenHelper {

    private static final String DBNAME="db_portfolio";
    private static final int VERSION=1;
    //private Context context;
    private String tag="BANCO";


    public Banco(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db=this.getWritableDatabase();
            db.execSQL(ClassTable.create());
            db.execSQL(ClassTable.insertValues());
            Log.d(tag, "criou bd");
        }catch (Exception e){
            Log.e(tag,e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean verifyPass(String email,String pass){
        SQLiteDatabase db =this.getReadableDatabase();
        String query="select count(*) from tb_user where ds_email = '"+email+"' AND ds_password = '"+pass+"';";
        Cursor c= db.rawQuery(query,null);
        if(c.moveToFirst()){
            Log.d(tag,"password correto");
            db.close();
            return true;
        }else{
            db.close();
            return false;
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

    }
}
