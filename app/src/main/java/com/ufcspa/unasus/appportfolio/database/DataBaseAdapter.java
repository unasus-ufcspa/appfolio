package com.ufcspa.unasus.appportfolio.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ufcspa.unasus.appportfolio.Model.User;

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

    private String tag = "BANCO";

    public DataBaseAdapter(Context context) {
        helper = new DataBase(context);
        db = helper.getDatabase();
    }

    public String getTestData() {
        //String sql ="SELECT name FROM sqlite_master WHERE type='table'";
        String sql = "SELECT Count(*) FROM tb_activity_student";

        Cursor mCur = db.rawQuery(sql, null);
        String tabelas = "";
        if (mCur.moveToFirst()) {
            do {
                tabelas += mCur.getString(0) + "\n";
            } while (mCur.moveToNext());
        }
        return tabelas;
    }

    public User verifyPass(String email, String pass) {
        int id=0;
        char type='0';
        String name="zebra";
        User user=new User(0,null,null);
        try {
            if (email != "" && pass != "") {

                String query = "select id_user , nm_user , tp_user from tb_user where ds_email ='"+email+"' "+"AND ds_password ='"+pass+"' ;";
                Cursor c = db.rawQuery(query, null);
                if (c.moveToFirst()) {
                    Log.d(tag, "password correto");
                    id=Integer.parseInt(c.getString(0));
                    name=c.getString(1);
                    type=c.getString(2).charAt(0);
                    user=new User(id,type,name);
                    Log.d(tag,"dados: id:"+id+" type:"+type+" name:"+name);
                }
            }
        }catch (Exception e){
            Log.d(tag,"erro query:"+e.getMessage());
        }finally {
            db.close();
            return user;
        }
    }

    public void criarUser() {
        ContentValues cv = new ContentValues();
        cv.put("nm_user", "Icaro");
        cv.put("nu_identification", "467");
        cv.put("tp_user", "S");
        cv.put("ds_email", "icaro@1234");
        cv.put("ds_password", "1234");
        db.insert("tb_user", null, cv);
        db.close();
        Log.d(tag, "inseriu no banco");
    }


    public String listarUsers() {
        String query = "SELECT * FROM tb_user";
        String r = "";
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            r += "id:" + c.getString(0) + "\n";
            r += "nome:" + c.getString(1) + "\n";
            r += "email:" + c.getString(4) + "\n";

        } else {
            r = "Não há users!";
        }
        c.close();
        db.close();
        Log.d(tag, "resultado string:" + r);
        return r;

    }

    public String listarTabelas() {
        String query = "SELECT name from sqlite_master where type='table'";
        String r = "";
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                r += c.getString(0) + "\n";
                //r += c.getString(1) + "\n";
            } while (c.moveToNext());
        } else {
            r = "não há tabelas";
        }

        return r;
    }







}
