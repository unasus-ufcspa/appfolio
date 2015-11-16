package com.ufcspa.unasus.appportfolio.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ufcspa.unasus.appportfolio.Model.Activity;
import com.ufcspa.unasus.appportfolio.Model.Team;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public List<Team> getClasses()
    {
        String query = "SELECT * FROM tb_class";
        List<Team> array_team = new ArrayList<>();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        do{ array_team.add(cursorToTeam(cursor)); } while(cursor.moveToNext());

        return array_team;
    }

    private Team cursorToTeam(Cursor cursor)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Team team = null;
       try {
            String dateBegin = cursor.getString(4);
            String dateEnd = cursor.getString(5);

            if(dateBegin == null)
                dateBegin = "1900-01-01";

            if(dateEnd == null)
                dateEnd = "1900-01-01";

            team = new Team(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3).charAt(0), dateFormat.parse(dateBegin),dateFormat.parse(dateEnd));
        } catch (ParseException e) {
            Log.d(tag,"Data em formato errado! (cursorToTeam())");
        }

        return team;
    }

    public ArrayList<Activity> getActivities(int userId, int portfolioId)
    {
        String query = "select \n" +
                        "\ta.id_activity,\n" +
                        "\ta.id_portfolio,\n" +
                        "\ta.nu_order,\n" +
                        "\ta.ds_title,\n" +
                        "\ta.ds_description\n" +
                        "from \n" +
                        "\ttb_activity_student ac\n" +
                        "    join tb_activity a on a.id_activity = ac.id_activity\n" +
                        "    join tb_portfolio p on p.id_portfolio = a.id_portfolio\n" +
                        "    join tb_portfolio_student ps on ps.id_portfolio_student = ac.id_portfolio_student    \n" +
                        "    join tb_portfolio_class pc on pc.id_portfolio_class = ps.id_portfolio_class\n" +
                        "    join tb_class c on c.id_class = pc.id_class\n" +
                        "    join tb_user s on s.id_user = ps.id_student\n" +
                        "where 1 = 1\n" +
                            "\tand s.id_user = "+ userId + "\n" +
                            "\tand p.id_portfolio = "+ portfolioId +";";

        ArrayList<Activity> array_activity = new ArrayList<>();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        do{ array_activity.add(cursorToActivity(cursor)); } while(cursor.moveToNext());

        return array_activity;
    }

    private Activity cursorToActivity(Cursor cursor)
    {
        Activity activity = new Activity(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4));
        return activity;
    }
}
