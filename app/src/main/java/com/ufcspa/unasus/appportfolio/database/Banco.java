package com.ufcspa.unasus.appportfolio.database;

import android.content.ContentValues;
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
        String query= "CREATE TABLE tb_user(\n" +
                "id_user INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "nm_user TEXT NOT NULL,\n" +
                "nu_identification TEXT NOT NULL,\n" +
                "tp_user TEXT,\n" +
                "ds_email TEXT NOT NULL,\n" +
                "ds_password TEXT,\n" +
                "nu_cellphone TEXT\n" +
                ");";

        //criarUser(this);
            //db=this.getWritableDatabase();

        try{
            String criar="INSERT INTO tb_user VALUES(NULL,'ICARUS','666','S','icaromscastro@gmail.com','senha123','97854632')";
            db.execSQL(query);
            Log.d(tag, "criou bd");
            db.execSQL(criar);
            Log.d(tag, "inseriu no banco");
            //this.getWritableDatabase();

            // criar usuario inicial
//            ContentValues cv= new ContentValues();
//            cv.put("nm_user","Icaro");
//            cv.put("nu_identification","467");
//            cv.put("tp_user","S");
//            cv.put("ds_email","icaro@1234");
//            cv.put("ds_password", "1234");
//            db.insert("tb_user", null, cv);
//            db.close();



        }catch (Exception e){
            Log.e(tag," catch on create:"+e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean verifyPass(String email,String pass){

        if(email!="" && pass!="")
        {
            SQLiteDatabase db =this.getReadableDatabase();
            String query="select id_user from tb_user where ds_email = '"+email+"' AND ds_password = '"+pass+"';";
            Cursor c= db.rawQuery(query,null);
            if(c.moveToFirst()){
                Log.d(tag,"password correto");
                db.close();
                return true;
            } else{
                db.close();
                return false;
            }
        }else{
            return false;
        }
        }

@Override
public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        }

public void criarUser(){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put("nm_user","Icaro");
        cv.put("nu_identification","467");
        cv.put("tp_user","S");
        cv.put("ds_email","icaro@1234");
        cv.put("ds_password", "1234");
        db.insert("tb_user", null, cv);
        db.close();
        Log.d(tag, "inseriu no banco");
        }


    public String listarUsers(){
        SQLiteDatabase db=this.getWritableDatabase();
        String query = "SELECT * FROM tb_user";
        String r="";
        Log.d(tag, "nome d abase:" + this.getDatabaseName());
        Cursor c = db.rawQuery(query,null);
        if(c.moveToFirst()){
            r+="id:"+c.getString(0)+"\n";
            r+="nome:"+c.getString(1)+"\n";
            r+="email:"+c.getString(4)+"\n";

        }else{
            r="Não há users!";
        }
        c.close();
        db.close();
        Log.d(tag,"resultado string:"+r);
        return r;

    }
    public String listarTabelas(){
        String query="SELECT name from sqlite_master where type='table'";
        SQLiteDatabase db=this.getWritableDatabase();
        String r="";
        Cursor c = db.rawQuery(query,null);
        if(c.moveToFirst()){
                do {
                    r += c.getString(0) + "\n";
                    //r += c.getString(1) + "\n";
                }while(c.moveToNext());
        }else{
                r="não há tabelas";
            }

        return r;
    }



        }
