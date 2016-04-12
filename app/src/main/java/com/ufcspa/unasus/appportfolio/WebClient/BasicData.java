package com.ufcspa.unasus.appportfolio.WebClient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.ufcspa.unasus.appportfolio.Model.basicData.*;
import com.ufcspa.unasus.appportfolio.Model.basicData.Class;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by icaromsc on 04/04/2016.
 */
public class BasicData {

    private LinkedList<Activity> activities;
    private LinkedList<ActivityStudent> activitiesStudent;
    private LinkedList<Class> classes;
    private LinkedList<ClassStudent> studentClasses;
    private LinkedList<ClassTutor> tutorClasses;
    private LinkedList<Portfolio> portfolios;
    private LinkedList<PortfolioClass> portfolioClasses;
    private LinkedList<PortfolioStudent> portfolioStudents;
    private LinkedList<User> users;
    private Context context;

    public BasicData(Context c) {
        activities=new LinkedList<Activity>();
        activitiesStudent= new LinkedList<ActivityStudent>();
        classes= new LinkedList<Class>();
        studentClasses= new LinkedList<ClassStudent>();
        tutorClasses= new LinkedList<ClassTutor>();
        portfolios= new LinkedList<Portfolio>();
        portfolioClasses= new LinkedList<PortfolioClass>();
        portfolioStudents= new LinkedList<PortfolioStudent>();
        users= new LinkedList<User>();
        this.context=c;
    }
    public void addActivity(Activity activity){
        activities.add(activity);
    }
    public void addActivityStudent(ActivityStudent a){
        activitiesStudent.add(a);
    }



    public void addClass(Class c){
        classes.add(c);
    }
    public void addClassStudent(ClassStudent c){
        studentClasses.add(c);
    }
    public void addClassTutor(ClassTutor c){
        tutorClasses.add(c);
    }
    public void addPortfolio(Portfolio p){
        portfolios.add(p);
    }
    public void addPortfolioClass(PortfolioClass pc){
        portfolioClasses.add(pc);
    }
    public void addPortfolioStudent(PortfolioStudent ps){
        portfolioStudents.add(ps);
    }
    public void addUsers(User u){
        users.add(u);
    }


    public LinkedList<Activity> getActivities() {
        return activities;
    }

    public LinkedList<ActivityStudent> getActivitiesStudent() {
        return activitiesStudent;
    }

    public LinkedList<Class> getClasses() {
        return classes;
    }

    public LinkedList<ClassStudent> getStudentClasses() {
        return studentClasses;
    }

    public LinkedList<ClassTutor> getTutorClasses() {
        return tutorClasses;
    }

    public LinkedList<Portfolio> getPortfolios() {
        return portfolios;
    }

    public LinkedList<PortfolioClass> getPortfolioClasses() {
        return portfolioClasses;
    }

    public LinkedList<PortfolioStudent> getPortfolioStudents() {
        return portfolioStudents;
    }

    public LinkedList<User> getUsers() {
        return users;
    }

    public synchronized void insertDataIntoSQLITE() {
        if(users.size()!=0 && activities.size() !=0){

        }

//        Thread t = new Thread(new Runnable() {
//            @SuppressLint("LongLogTag")
//            @Override
//            public void run() {
                //INSERT DATA IN DATABASE
                DataBaseAdapter data = DataBaseAdapter.getInstance(context);
                String log="BD Insert SQLite";
                if(users.size()!=0){
                    Log.d(log,"inserindo users");
                    data.insertTBUser(users);
                }
                Log.d(log, "inserindo classes");
                data.insertTBClass(classes);
                Log.d(log, "inserindo student claases");
                data.insertTBClassStudent(studentClasses);
                Log.d(log, "inserindo tutor classes");
                data.insertTBClassTutor(tutorClasses);
                Log.d(log, "inserindo portfolios");
                data.insertTBPortfolio(portfolios);
                Log.d(log, "inserindo portfolio claases");
                data.insertTBPortfolioClass(portfolioClasses);
                Log.d(log, "inserindo portfolio Students");
                data.insertTBPortfolioStudent(portfolioStudents);
                Log.d(log, "inserindo tb_atividades");
                data.insertTBActivity(activities);
                Log.d(log, "inserindo tb_activities student");
                data.insertTBActivityStudent(activitiesStudent);


//            }
//        });
//        t.start();
    }

    public static JSONObject toJSON(int idUser){
        JSONObject json = new JSONObject();
        JSONObject basic = new JSONObject();
        try {
            basic.put("id_user",idUser);
            json.put("basicData_request",basic);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


}