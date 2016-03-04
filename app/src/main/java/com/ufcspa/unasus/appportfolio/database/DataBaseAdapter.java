package com.ufcspa.unasus.appportfolio.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ufcspa.unasus.appportfolio.Model.Activity;
import com.ufcspa.unasus.appportfolio.Model.ActivityStudent;
import com.ufcspa.unasus.appportfolio.Model.Attachment;
import com.ufcspa.unasus.appportfolio.Model.Comentario;
import com.ufcspa.unasus.appportfolio.Model.PortfolioClass;
import com.ufcspa.unasus.appportfolio.Model.Reference;
import com.ufcspa.unasus.appportfolio.Model.StudFrPortClass;
import com.ufcspa.unasus.appportfolio.Model.Team;
import com.ufcspa.unasus.appportfolio.Model.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by UNASUS on 11/11/2015.
 */
public class DataBaseAdapter {
    private static DataBaseAdapter mInstance = null;
    private SQLiteDatabase db;
    private DataBase helper;
    private String tag = "BANCO DE DADOS ";

    private DataBaseAdapter(Context ctx) {
        helper = new DataBase(ctx);
        db = helper.getDatabase();
    }

    public static DataBaseAdapter getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DataBaseAdapter(ctx);
        }
        return mInstance;
    }

    public User verifyPass(String email, String pass) {
        int id = 0;
        char type = '0';
        String name = "zebra";
        User user = new User(0, null, null);
        try {
            if (email != "" && pass != "") {

                String query = "select id_user , nm_user , tp_user from tb_user where ds_email ='" + email + "' " + "AND ds_password ='" + pass + "' ;";
                Cursor c = db.rawQuery(query, null);
                if (c.moveToFirst()) {
                    Log.d(tag, "password correto");
                    id = Integer.parseInt(c.getString(0));
                    name = c.getString(1);
                    type = c.getString(2).charAt(0);
                    user = new User(id, type, name);
                    Log.d(tag, "dados: id:" + id + " type:" + type + " name:" + name);
                }
            }
        } catch (Exception e) {
            Log.d(tag, "erro query:" + e.getMessage());
        } finally {
//            db.close();
            return user;
        }
    }


    public boolean insertReference(Reference ref){
        ContentValues cv = new ContentValues();
        //cv.put("id_reference",ref.getIdRef());
        cv.put("ds_url",ref.getDsUrl());
        cv.put("id_activity_student",ref.getIdActStudent());
        try {
            db.insert("tb_reference", null, cv);
//            db.close();
            Log.d(tag, "inseriu referencia no banco");
            return true;
        }catch (Exception e){
            Log.e(tag, "erro ao inserir ref:" + e.getMessage());
            return false;
        }
    }

    public void deleteReference(int idReference) {
        try {
            db.delete("tb_reference", "id_reference=?", new String[]{"" + idReference});
            Log.d(tag, "removeu ref do banco");
        }catch (Exception e){
            Log.e(tag, "erro ao delete ref:" + e.getMessage());
        }
    }

    public List getReferences(int idActivity){
        List refs= new ArrayList<Reference>();
        String sql="SELECT * FROM tb_reference WHERE id_activity_student ="+idActivity+";";
        Cursor c = db.rawQuery(sql, null);
        Reference r;
        if (c.moveToFirst()) {
            do {
                try {
                   r= new Reference(c.getInt(0),c.getString(1),0);
                    refs.add(r);
                    Log.d(tag,"reference:"+r.toString());
                } catch (Exception v) {
                    Log.e(tag, "erro ao pegar dados do banco:" + v.getMessage());
                }
                //add references
            } while (c.moveToNext());
            c.close();
//            db.close();
        } else {
            Log.d(tag, "não retornoun nada");
        }
        return refs;
    }

    public int insertComment(Comentario c) {
        ContentValues cv = new ContentValues();
        cv.put("id_activity_student", c.getIdActivityStudent());
        cv.put("id_author", c.getIdAuthor());
        cv.put("tx_comment", c.getTxtComment());
        cv.put("tx_reference", c.getTxtReference());
        cv.put("tp_comment", c.getTypeComment());
        cv.put("dt_comment", c.getDateComment());
        db.insert("tb_comment", null, cv);
        try {
//            db.close();
            Log.d(tag, "inseriu comentario no banco");
        } catch (Exception e) {
            Log.e(tag, "erro ao inserir:" + e.getMessage());
        }
        Cursor cursor = db.rawQuery("select seq from sqlite_sequence where name='tb_comment'",null);
        int lastID=0;
        if(cursor.moveToFirst()){
            lastID=cursor.getInt(0);
            Log.d(tag, "last id_comment id table:"+lastID);
        }
        return lastID;
    }

    public void insertSpecificComment(Comentario c,int idNote) {
        ContentValues cv = new ContentValues();
        cv.put("id_activity_student", c.getIdActivityStudent());
        cv.put("id_author", c.getIdAuthor());
        cv.put("tx_comment", c.getTxtComment());
        cv.put("tx_reference", c.getTxtReference());
        cv.put("tp_comment", c.getTypeComment());
        cv.put("dt_comment", c.getDateComment());
        cv.put("id_note", idNote);
        db.insert("tb_comment", null, cv);
        try {
//            db.close();
            Log.d(tag+" insertSpecificComment", "inseriu comentario no banco");
        } catch (Exception e) {
            Log.e(tag+ " insertSpecificComment", "erro ao inserir:" + e.getMessage());
        }

    }

    public Comentario getCommentById(int id){
        String sql = "select * from tb_comment WHERE id_comment ="+id+ ";";
        Cursor c = db.rawQuery(sql,null);
            if(c!=null && c.getCount()>0) {
                c.moveToFirst();
                Comentario comm = new Comentario();
                comm.setIdComment(c.getInt(0));
                comm.setIdActivityStudent(c.getInt(1));
                comm.setIdAuthor(c.getInt(2));
                comm.setTxtComment(c.getString(3));
                comm.setTxtReference(c.getString(4));
                comm.setTypeComment(c.getString(5));
                comm.setDateComment(c.getString(6));
                c.close();
                return  comm;
            }else{
                return null;
            }
    }
    public void updateComment(Comentario c){
        ContentValues cv = new ContentValues();
        cv.put("id_activity_student", c.getIdActivityStudent());
        cv.put("id_author", c.getIdAuthor());
        cv.put("tx_comment",c.getTxtComment());
        cv.put("tx_reference",c.getTxtReference());
        cv.put("dt_comment", c.getDateComment());
        try{
            db.update("tb_comment", cv, "id_comment=?", new String[]{"" + c.getIdComment()});
        }catch (Exception e ){
            Log.d(tag, e.getMessage());
        }
    }


    public List<Comentario> listComments(int idActStu,String typeComment,int idNote) {
        ArrayList<Comentario> comentarios = new ArrayList<Comentario>();
        //String sql = "select * from tb_comment WHERE id_activity_student =" + idActStu;
        String sql= "SELECT\n" +
                "\tc.id_comment,\n" +
                "\tc.id_activity_student,\n" +
                "\tc.id_author,\n" +
                "\tc.tx_comment,\n" +
                "\tc.dt_comment,\n" +
                "\tac.id_attachment \n" +
                "\tFROM tb_comment c \n" +
                "\t\tLEFT JOIN  tb_attach_comment ac on ac.id_comment = c.id_comment\n" +
                "\tWHERE 1=1 AND c.id_activity_student = "+idActStu;

        StringBuilder stBuild = new StringBuilder(sql);
        if(typeComment.equalsIgnoreCase("C")||typeComment.equalsIgnoreCase("O")||typeComment.equalsIgnoreCase("P")){
            //sql+=" AND tp_comment='"+typeComment+"' ";
            stBuild.append(" AND tp_comment='"+typeComment+"' ");
            if(typeComment.equalsIgnoreCase("O")){
                //sql+=" AND id_note="+idNote;
                stBuild.append(" AND id_note="+idNote);
            }
        }
        //sql+=" ORDER BY dt_comment ASC;";
        stBuild.append(" ORDER BY dt_comment ASC;");
        sql=stBuild.toString();
        Log.e(tag, "sql listComments:" + sql);
        Cursor c = db.rawQuery(sql, null);
        Comentario cmm;
        if (c.moveToFirst()) {
            do {
                try {
                    cmm = new Comentario();
                    cmm.setIdComment(c.getInt(0));
                    cmm.setIdActivityStudent(c.getInt(1));
                    cmm.setIdAuthor(c.getInt(2));
                    cmm.setTxtComment(c.getString(3));
                    cmm.setDateComment(c.getString(4));
                    cmm.setIdAttach(c.getInt(5));
                    comentarios.add(cmm);
                } catch (Exception v) {
                    Log.e(tag, "erro ao pegar dados do banco:" + v.getMessage());
                }
                //add comment
            } while (c.moveToNext());
            c.close();
//            db.close();
        } else {
            Log.d(tag, "não retornoun nada");
        }
        Log.d(tag, "listou comentarios no banco n:" + comentarios.size());
        return comentarios;
    }
    

    public List<Integer> listSpecificComments(int idActStu) {
        ArrayList<Integer> comentarios = new ArrayList<Integer>();
        String sql = "SELECT DISTINCT id_note from tb_comment WHERE id_activity_student =" + idActStu+" AND tp_comment='O'";
        Log.e(tag, "sql listComments:" + sql);
        Cursor c = db.rawQuery(sql, null);
       Integer id;
        if (c.moveToFirst()) {
            do {
                try {
                    id=c.getInt(0);
                    Log.e(tag, "listSpecificComments idnow:" + id);
                    comentarios.add(id);
                } catch (Exception v) {
                    Log.e(tag, "erro ao pegar dados do banco:" + v.getMessage());
                }
                //add comment
            } while (c.moveToNext());
            c.close();
//            db.close();
        } else {
            Log.d(tag+" listSpecific comments", "não retornou nada");
        }
        Log.d(tag, "listou notas no banco n:" + comentarios.size());
        return comentarios;
    }

    public int insertAttachment(Attachment attach){
        ContentValues cv = new ContentValues();
        cv.put("ds_local_path", attach.getDs_local_path());
        cv.put("ds_server_path",attach.getDs_server_path());
        cv.put("tp_attachment",attach.getType());
        cv.put("nm_file", attach.getNameFile());

        try {
            db.insert("tb_attachment",null,cv);
            Log.d(tag, "conseguiu salvar id anexo no comentario do bd");
        }catch (Exception e){
            Log.e(tag, "erro ao salvar na tabela tb_attach_comment:" + e.getMessage());
        }
        Cursor cursor = db.rawQuery("select seq from sqlite_sequence where name='tb_attachment'",null);
        int lastID=0;
        if(cursor.moveToFirst()){
            lastID=cursor.getInt(0);
            Log.d(tag, "last id_attachment in table:"+lastID);
        }
        return lastID;
    }

    public Attachment getAttachmentByID(int id_attachment){
        Attachment attch= new Attachment();
        String sql="SELECT * FROM tb_attachment WHERE id_attachment ="+id_attachment;
        Cursor c = null;
            try{
               c= db.rawQuery(sql,null);
                if(c.moveToFirst()){
                    attch.setId_attachment(c.getInt(0));
                    attch.setDs_local_path(c.getString(1));
                    attch.setDs_server_path(c.getString(2));
                    attch.setDs_type(c.getString(3));
                    attch.setNameFile(c.getString(4));
                }else{
                    Log.e(tag, "conseguiu salvar id anexo no comentario do bd");
                }
            }catch (Exception e){
                Log.wtf(tag, "erro ao tentar buscar anexo no banco:" + e.getMessage());
            }
        return attch;
    }




    public void insertAttachComment(int idComment,int idAttach){
        ContentValues cv = new ContentValues();
        cv.put("id_attachment",idAttach);
        cv.put("id_comment",idComment);
        try {
            db.insert("tb_attach_comment",null,cv);
            Log.d(tag, "conseguiu salvar id anexo no comentario do bd");
        }catch (Exception e){
            Log.e(tag, "erro ao salvar na tabela tb_attach_comment:" + e.getMessage());
        }
    }

    public void updateActivityStudent(ActivityStudent activityStudent) {
        ContentValues cv = new ContentValues();
        cv.put("tx_activity", activityStudent.getTxtActivity());
        cv.put("dt_last_access", activityStudent.getDtLastAcess());
        try {
            db.update("tb_activity_student", cv, "id_activity_student=?", new String[]{String.valueOf(activityStudent.getIdActivityStudent())});
            Log.d(tag, "conseguiu salvar alteração da atividade");
        } catch (Exception e) {
            Log.e(tag, "erro ao atualizar acStudent:" + e.getMessage());
        }
    }

    public ActivityStudent listActivityStudent(int idActivityStudent) {
        ActivityStudent acStudent = new ActivityStudent();
        String query = "select tx_activity from tb_activity_student WHERE id_activity_student = " + idActivityStudent + ";";
        Cursor c = null;
        Log.d(tag, "query lista act Stu:" + query);
        try {
            c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                acStudent.setIdActivityStudent(idActivityStudent);
                acStudent.setTxtActivity(c.getString(0));
                Log.d(tag, "há texto da atividade no banco:"+acStudent.getTxtActivity());
            } else {
                Log.d(tag, "não há texto da atividade no banco");
                acStudent.setIdActivityStudent(idActivityStudent);
                acStudent.setTxtActivity("");
            }
            c.close();
        } catch (Exception e) {
            Log.e(tag, "erro ao listar atividade do estudante:" + e.getMessage());
        }


//        db.close();
        return acStudent;
    }


    public void criarUser() {
        ContentValues cv = new ContentValues();
        cv.put("nm_user", "Icaro");
        cv.put("nu_identification", "467");
        cv.put("tp_user", "S");
        cv.put("ds_email", "icaro@1234");
        cv.put("ds_password", "1234");
        db.insert("tb_user", null, cv);
//        db.close();
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
//        db.close();
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

//        db.close();

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

//        db.close();
        return portfolios;
    }

    private PortfolioClass cursorToPortfolio(Cursor c){
        PortfolioClass pc = new PortfolioClass(Integer.parseInt(c.getString(0)), c.getString(1), c.getString(2), c.getString(3));
        Log.d(tag, "portfolio populado:" + pc.toString());
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

//        db.close();
        return userType;
    }

    public List<Team> getClasses(int idUser, char userType)
    {
        String query = "select distinct\n" +
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

//        db.close();
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

//        db.close();
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

//        db.close();
        return array_activity;
    }

    public ArrayList<StudFrPortClass> selectListActivitiesAndStudents(int idPortfolioClass,String perfil,int idUsuario){
        String query="select \n" +
                "tas.id_activity_student,\n" +
                "u.id_user,\n" +
                "a.ds_title,\n" +
                "a.ds_description,\n" +
                "u.nm_user as nm_student,\n" +
                "tas.id_portfolio_student\n" +
                "FROM\n" +
                "\ttb_activity_student as tas\n" +
                "\tjoin tb_activity a on tas.id_activity = a.id_activity\n" +
                "\tjoin tb_portfolio_student ps on ps.id_portfolio_student = tas.id_portfolio_student\n" +
                "\tjoin tb_user u on u.id_user = ps.id_student\n" +
                "WHERE \n" +
                "\tps.id_portfolio_class="+idPortfolioClass;
        if(perfil.equalsIgnoreCase("S"))
            query+=" AND u.id_user = "+idUsuario;
        ArrayList<StudFrPortClass> students = new ArrayList<>();
        Cursor c = db.rawQuery(query, null);
        if(c.moveToFirst()){
            HashMap<Integer,StudFrPortClass> hash = new LinkedHashMap<Integer,StudFrPortClass>();
            int lastid;
            int  idUser = 0;
            int cont=-1;
            do{
                lastid=idUser;
                idUser=c.getInt(1);

                String nameStudent=c.getString(4);
                Activity a = new Activity(c.getInt(0),c.getString(2),c.getString(3));
                a.setId_portfolio(c.getInt(5));

                if(lastid==idUser){
                    students.get(cont).setNameStudent(nameStudent);
                    students.get(cont).add(a);
                }else{
                    StudFrPortClass student = new StudFrPortClass();
                    student.setNameStudent(nameStudent);
                    student.add(a);
                    students.add(student);
                    cont++;
                }
//

            }while (c.moveToNext());

        }else{
            Log.d(tag,"Nao retornou nada na consulta");
        }
        return students;
    }

    public List<PortfolioClass> selectListClassAndUserType(int idUser){
        //retorna uma lista com as turmas que o usuário esta cadastro e seu papel nela(tutor ou aluno);
        // perfil S- Student T-tutor
        String query="select distinct \n" +
                "\tps.id_portfolio_class,\n" +
                "\tc.ds_code,\n" +
                "\tc.ds_description,\n" +
                "\tp.ds_title,\n" +
                "\tp.ds_description,\n" +
                "\tcase \n" +
                "\t\twhen id_student = "+idUser+" then 'S'\n" +
                "\t\twhen id_tutor = "+idUser+" then 'T' \n" +
                "\tend as perfil\n" +
                "from \n" +
                "\ttb_portfolio_student as ps \n" +
                "\tjoin tb_portfolio_class pc on pc.id_portfolio_class = ps.id_portfolio_class\n" +
                "\tjoin tb_class c on c.id_class = pc.id_class \n" +
                "\tjoin tb_portfolio p on p.id_portfolio = pc.id_portfolio\n" +
                "WHERE\n" +
                "\t( id_tutor = "+idUser+" OR id_student = "+idUser+" )";
        Cursor c = db.rawQuery(query, null);
        ArrayList lista = new ArrayList<PortfolioClass>();
            if(c.moveToFirst()){
                do{
                    int idPortClass=c.getInt(0);
                    String classCode=c.getString(1);
                    String portTitle=c.getString(3);
                    String perfil=c.getString(5);
                    PortfolioClass p = new PortfolioClass(classCode,idPortClass,perfil,portTitle);
                    lista.add(p);
                    Log.d(tag,"port class:" +p.toString());
                }while (c.moveToNext());
            }else{
                Log.d(tag,"Nao retornou nada na consulta");
            }

        return lista;
    }





    private Activity cursorToActivity(Cursor cursor)
    {
        Activity activity = new Activity(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4));
        return activity;
    }

    public void saveAttachmentActivityStudent(String path, String type, int idActivityStudent) {
        ContentValues values = new ContentValues();

        values.put("id_activity_student", idActivityStudent);
        values.put("ds_local_path", path);
        values.put("ds_type", type);

        db.insert("tb_attachment", null, values);
    }

    public int getActivityStudentID(int idActivity, int idPortfolioStudent)
    {
        String query = "SELECT  id_activity_student FROM tb_activity_student WHERE \n" +
                "\tid_portfolio_student = " + idPortfolioStudent + " and id_activity = " + idActivity + ";";
        Log.d(tag, "query getActStuID:" + query);
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            Log.d(tag,"idAcStudent:"+cursor.getInt(0));
            return cursor.getInt(0);
        }
        //db.close();
        cursor.close();
        return -1;
    }

    public ArrayList<Attachment> getAttachmentsFromActivityStudent(int idActivityStudent)
    {
        String query = "SELECT * FROM tb_attachment WHERE id_activity_student = " + idActivityStudent + ";";

        ArrayList<Attachment> array_attachment = new ArrayList<>();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        do{
            if (cursor.getCount() != 0)
                array_attachment.add(cursorToAttachment(cursor));
        } while(cursor.moveToNext());

        return array_attachment;
    }

    public ArrayList<Attachment> getAttachmentsFromActivityStudent(int idActivityStudent, String type) {
        String query = "SELECT * FROM tb_attachment WHERE id_activity_student = " + idActivityStudent + " and ds_type = '" + type + "';";

        ArrayList<Attachment> array_attachment = new ArrayList<>();

        Cursor cursor = db.rawQuery(query, null);

        do {
            if (cursor.getCount() != 0)
                array_attachment.add(cursorToAttachment(cursor));
        } while (cursor.moveToNext());

        return array_attachment;
    }

    public ArrayList<Attachment> getAttachmentsFromStudent() {
        String query = "SELECT * FROM tb_attachment;";

        ArrayList<Attachment> array_attachment = new ArrayList<>();

        Cursor cursor = db.rawQuery(query, null);

        do {
            if (cursor.getCount() != 0)
                array_attachment.add(cursorToAttachment(cursor));
        } while (cursor.moveToNext());

        return array_attachment;
    }

    private Attachment cursorToAttachment(Cursor cursor)
    {
        Attachment attachment = new Attachment(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        return attachment;
    }







}
