package com.ufcspa.unasus.appportfolio.webClient;

import android.content.Context;
import android.util.Log;

import com.ufcspa.unasus.appportfolio.model.Policy;
import com.ufcspa.unasus.appportfolio.model.PolicyUser;
import com.ufcspa.unasus.appportfolio.model.TutorPortfolio;
import com.ufcspa.unasus.appportfolio.model.basicData.Activity;
import com.ufcspa.unasus.appportfolio.model.basicData.ActivityStudent;
import com.ufcspa.unasus.appportfolio.model.basicData.Class;
import com.ufcspa.unasus.appportfolio.model.basicData.ClassStudent;
import com.ufcspa.unasus.appportfolio.model.basicData.ClassTutor;
import com.ufcspa.unasus.appportfolio.model.basicData.Portfolio;
import com.ufcspa.unasus.appportfolio.model.basicData.PortfolioClass;
import com.ufcspa.unasus.appportfolio.model.basicData.PortfolioStudent;
import com.ufcspa.unasus.appportfolio.model.basicData.User;
import com.ufcspa.unasus.appportfolio.database.DataBase;

import org.json.JSONException;
import org.json.JSONObject;

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
    private LinkedList<Policy> policies;
    private LinkedList<PolicyUser> policyUsers;
    private LinkedList<TutorPortfolio> tutorPortfolios;
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
        policies= new LinkedList<Policy>();
        policyUsers= new LinkedList<PolicyUser>();
        tutorPortfolios = new LinkedList<TutorPortfolio>();
        this.context=c;
    }

    public static JSONObject toJSON(int idUser, String idDevice) {
        JSONObject json = new JSONObject();
        JSONObject basic = new JSONObject();
        try {
            basic.put("id_user", idUser);
            basic.put("ds_hash", idDevice);
            json.put("basicData_request", basic);
            Log.d("basicData_request: ", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
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

    public void addTutorPortfolio(TutorPortfolio tp){
        tutorPortfolios.add(tp);
    }

    public void addUsers(User u){
        users.add(u);
    }
    public void addPolicy(Policy p){
        policies.add(p);
    }
    public void addPolicyUsers(PolicyUser pu){
        policyUsers.add(pu);
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

    public LinkedList<PolicyUser> getPolicyUsers() {
        return policyUsers;
    }

    public void setPolicyUsers(LinkedList<PolicyUser> policyUsers) {
        this.policyUsers = policyUsers;
    }

    public LinkedList<Policy> getPolicies() {
        return policies;
    }

    public void setPolicies(LinkedList<Policy> policies) {
        this.policies = policies;
    }

    public synchronized void insertDataIntoSQLITE() {
//        Thread t = new Thread(new Runnable() {
//            @SuppressLint("LongLogTag")
//            @Override
//            public void run() {
                //INSERT DATA IN DATABASE
                DataBase data = DataBase.getInstance(context);
                String log="BD Insert SQLite";
//                if(users.size()!=0){
        Log.d(log, "inserindo users");
        data.insertTBUser(users);
//                }
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
                Log.d(log, "inserindo tb_policy");
                data.insertTBPolicy(policies);
                Log.d(log, "inserindo tb_policyUser");
                data.insertTBPolicyUser(policyUsers);
                Log.d(log, "inserindo tb_tutor_portfolio");
                data.inserTbTutorPortfolio(tutorPortfolios);

//            }
//        });
//        t.start();
    }


}
