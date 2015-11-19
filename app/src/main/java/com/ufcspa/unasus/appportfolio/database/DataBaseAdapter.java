package com.ufcspa.unasus.appportfolio.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ufcspa.unasus.appportfolio.Model.Activity;
import com.ufcspa.unasus.appportfolio.Model.PortfolioClass;
import com.ufcspa.unasus.appportfolio.Model.Team;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.ufcspa.unasus.appportfolio.Model.User;

/**
 * Created by UNASUS on 11/11/2015.
 */
public class DataBaseAdapter {
    private SQLiteDatabase db;
    private DataBase helper;

    private String tag = "BANCO DE DADOS";

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

    public List<PortfolioClass> listarPortfolio(int idClass, char userType,int idUser){
        String query = "SELECT * FROM tb_portfolio";

        String queryNova="select \n" +
                "\t a.id_portfolio_student as id_portfolio_student,\n" +
                "\t c.ds_code as code,\n" +
                "\t d.nm_user as nome_aluno,\n" +
                "\t p.ds_title as portfolio_title\n" +
                "from\n" +
                " tb_portfolio_student a\n" +
                "\tjoin tb_portfolio_class b on b.id_portfolio_class = a.id_portfolio_class\n" +
                "\tjoin tb_class c on c.id_class = b.id_class \n" +
                "\tjoin tb_user d on d.id_user = a.id_student\n" +
                "\tjoin tb_user e on e.id_user = a.id_tutor\n" +
                "\tjoin tb_portfolio p on p.id_portfolio = b.id_portfolio\n" +
                "where 1=1 \n";
        if(userType=='S')
            queryNova+="AND a.id_student="+idUser;
        else if(userType=='T')
            queryNova+="AND a.id_tutor="+idUser;

        queryNova+=" AND "+"b.id_class="+idClass+";";

        ArrayList<PortfolioClass> portfolios = new ArrayList<PortfolioClass>();
        Cursor c = db.rawQuery(queryNova,null);
        if(c.moveToFirst()){
            do{
                portfolios.add(cursorToPortfolio(c));
            }while(c.moveToNext());
        }
        return portfolios;
    }

    private PortfolioClass cursorToPortfolio(Cursor c){
        PortfolioClass pc =new PortfolioClass(Integer.parseInt(c.getString(0)),c.getString(1),c.getString(2),c.getString(3));
        Log.d(tag,"portfolio populado:"+pc.toString());
        return pc;
    }

    public char verifyUserType(int idUser)
    {
        String query = "select \n" +
                "\tcase \n" +
                "\t\twhen sum(perfil) = 1 then 'T'\n" +
                "\t\twhen sum(perfil) = 2 then 'S'\n" +
                "\t\twhen sum(perfil) = 3 then 'U'\n" +
                "\tend as perfil\n" +
                "from (\n" +
                "\tselect 1 as perfil, count(id_portfolio_student) as nr from tb_portfolio_student where id_tutor = "+ idUser +"\n" +
                "\tunion select 2, count(id_portfolio_student) from tb_portfolio_student where id_student = "+ idUser +")\n" +
                "where nr > 0";
        char userType = 'S';

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst())
        {
            userType = cursor.getString(0).charAt(0);
        }

        return userType;
    }
    //TODO Parece que as turmas vem repetidas quando o usuário se loga como Tutor. Verificar o SELECT.
    public List<Team> getClasses(int idUser, char userType)
    {
        String query = "select \n" +
                "\tf.id_class,\n" +
                "\tf.id_proposer,\n" +
                "    f.ds_code,\n" +
                "\tf.ds_description,\n" +
                "\tf.st_status,\n" +
                "\tf.dt_start,\n" +
                "\tf.dt_finish\n" +
                "from \n" +
                "\ttb_portfolio_student a\n" +
                "\tjoin tb_user b on b.id_user = a.id_tutor\n" +
                "    join tb_user c on c.id_user = a.id_student\n" +
                "    join tb_portfolio_class d on d.id_portfolio_class = a.id_portfolio_class\n" +
                "    join tb_portfolio e on e.id_portfolio = d.id_portfolio\n" +
                "    join tb_class f on f.id_class = d.id_class\n" +
                "where 1=1 ";

        if(userType=='S')
            query+="AND a.id_student="+idUser;
        else if(userType=='T')
            query+="AND a.id_tutor="+idUser;

        List<Team> array_team = new ArrayList<>();

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) { do { array_team.add(cursorToTeam(cursor)); } while (cursor.moveToNext());}

        return array_team;
    }

    private Team cursorToTeam(Cursor cursor)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Team team = null;
        try {
            String dateBegin = cursor.getString(5);
            String dateEnd = cursor.getString(6);

            if(dateBegin == null)
                dateBegin = "1900-01-01";

            if(dateEnd == null)
                dateEnd = "1900-01-01";

            team = new Team(cursor.getInt(0),cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4).charAt(0), dateFormat.parse(dateBegin),dateFormat.parse(dateEnd));
        } catch (ParseException e) {
            Log.d(tag,"Data em formato errado! (cursorToTeam())");
        }

        return team;
    }

    public ArrayList<Activity> getActivities(int userId, int portfolioStudentId, char userType)
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
                "\tand ps.id_portfolio_student = "+ portfolioStudentId;
        if(userType=='S')
            query += "\nand s.id_user = "+ userId + ";";
        else if(userType=='T')
            query += "\nand ps.id_tutor=" + userId + ";";

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
