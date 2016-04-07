package com.ufcspa.unasus.appportfolio.WebClient;

import android.content.Context;

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
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //INSERT DATA IN DATABASE
                DataBaseAdapter data = DataBaseAdapter.getInstance(context);

                data.insertTBUser(users);
                data.insertTBClass(classes);
                data.insertTBClassStudent(studentClasses);
                data.insertTBClassTutor(tutorClasses);
                data.insertTBPortfolio(portfolios);
                data.insertTBPortfolioClass(portfolioClasses);
                data.insertTBPortfolioStudent(portfolioStudents);
                data.insertTBActivity(activities);
                data.insertTBActivityStudent(activitiesStudent);


            }
        });
    }

    public static JSONObject toJSON(int idUser){
        JSONObject json = new JSONObject();
        JSONObject basic = new JSONObject();
        try {
            basic.put("id_user",idUser);
            json.put("getBasicData_request",basic);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


}
