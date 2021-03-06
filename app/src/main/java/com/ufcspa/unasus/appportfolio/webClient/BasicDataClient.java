package com.ufcspa.unasus.appportfolio.webClient;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ufcspa.unasus.appportfolio.activities.LoginActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

/**
 * Created by icaromsc on 31/03/2016.
 */
public class BasicDataClient extends HttpClient {
    private String method="basicData";
    private Context context;
    private BasicData basicData;
    private LoginActivity loginActivity;
    private String jsonStringName="basicData_response";


    public BasicDataClient(Context context) {
        super(context);
        this.context= context;
    }

    public void postJson(JSONObject jsonFirstRequest){
        Log.d(tag, "URL: " + URL + method);

        JsonObjectRequest jsObjReq = new JsonObjectRequest(Request.Method.POST, URL+method, jsonFirstRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(tag,"Retornou do request");
                JSONObject rsp = response;
                getResponse(response);
                if(response.has("basicDataVisitante_response")){
                    jsonStringName="basicDataVisitante_response";
                    getResponse(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LoginActivity.isBasicDataSyncNotSucessful = true;
                Log.e(tag, "Erro  na request");
                volleyError.printStackTrace();
                //Log.wtf(tag,"Network response:"+volleyError.networkResponse.statusCode);
                Log.e(tag, "erro=" + volleyError.getMessage());
              //  loginActivity.showProgress(false);
            }
        });

        jsObjReq.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(30),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsObjReq);
    }

    private void getResponse(JSONObject response){
        try {
            Log.d(tag, "JSON RESPONSE: " + response.toString().replaceAll("\\{","\n{"));
            System.out.print(response.toString().replaceAll("\\{","\n{"));
            if (response.has("error")) {
                Log.e(tag, "sincronizacao de dados iniciais falhou");
                LoginActivity.isBasicDataSyncNotSucessful = true;
                Log.d(tag, "JSon response receiving:" + response.toString());
            }else if(response.has(jsonStringName)){
                Log.d(tag,"JSON POST existe basic Data response");
                // INSTANCE BASIC DATA COMPLEX OBJECT
                basicData= new BasicData(context);

                //Log.d(tag, "user encontrado!\n cadastrando no banco...");
                JSONObject resp= new JSONObject();
                try {
                    resp=response.getJSONObject(jsonStringName);
                    Log.d(tag, "JSon response::" + resp.toString());


                    if(resp.has("portfolioStudent")) {
                        // GET PORTFOLIO STUDENT
                        JSONObject objPs = resp.getJSONObject("portfolioStudent");
                        JSONArray tb_port_stu = objPs.getJSONArray("tb_portfolio_student");
                        // POPULATE PORTFOLIO STUDENT
                        for (int i = 0; i < tb_port_stu.length(); i++) {
                            JSONObject temp = tb_port_stu.getJSONObject(i);
                            PortfolioStudent ps = new PortfolioStudent();

                            // GET DATA FROM JSON
                            int idPortfolioStudent=temp.getInt("id_portifolio_student");
                            int idStudent=temp.getInt("id_student");
//                                    int idTutor=temp.getInt("id_tutor");
                            int idPortfolioClass = temp.getInt("id_portfolio_class");
                            String dtFirstSync=temp.getString("dt_first_sync");
                            String nuPortVersion=temp.getString("nu_portfolio_version");

                            //POPULATE OBJECT WITH DATA
                            ps.setId_portfolio_student(idPortfolioStudent);
                            ps.setId_student(idStudent);
//                                    ps.setId_tutor(idTutor);
                            ps.setDt_first_sync(dtFirstSync);
                            ps.setNu_portfolio_version(nuPortVersion);
                            ps.setId_portfolio_class(idPortfolioClass);


                            //ADD TO LINKEDLIST

                            basicData.addPortfolioStudent(ps);

                        }
                    }

                    if (resp.has("tutorPortfolio")) {
                        JSONObject tutorPortfolio = resp.getJSONObject("tutorPortfolio");
                        JSONArray tb_tutor_portfolio = tutorPortfolio.getJSONArray("tb_tutor_portfolio");
                        for (int i=0;i<tb_tutor_portfolio.length();i++){
                            JSONObject temp = tb_tutor_portfolio.getJSONObject(i);
                            TutorPortfolio tutor = new TutorPortfolio();

                            tutor.setId_tutor_portfolio(temp.getInt("id_tutor_portfolio"));
                            tutor.setId_tutor(temp.getInt("id_tutor"));
                            tutor.setId_portfolio_student(temp.getInt("id_portfolio_student"));

                            basicData.addTutorPortfolio(tutor);
                        }
                    }

                    if(resp.has("portfolio_class")) {
                        // GET PORTFOLIO STUDENT
                        JSONObject objPs = resp.getJSONObject("portfolio_class");
                        JSONArray tb_port_class = objPs.getJSONArray("tb_portfolio_class");
                        // POPULATE PORTFOLIO STUDENT
                        for (int i = 0; i < tb_port_class.length(); i++) {
                            JSONObject temp = tb_port_class.getJSONObject(i);
                            PortfolioClass pc = new PortfolioClass();

                            // GET DATA FROM JSON
                            int idPortfolioClass=temp.getInt("id_portfolio_class");
                            int idClass=temp.getInt("id_class");
                            int idPortfolio=temp.getInt("id_portfolio");


                            //POPULATE OBJECT WITH DATA
                            pc.setId_portfolio_class(idPortfolioClass);
                            pc.setId_class(idClass);
                            pc.setId_portfolio(idPortfolio);


                            //ADD TO LINKEDLIST

                            basicData.addPortfolioClass(pc);

                        }
                    }

                    if(resp.has("portfolio")) {
                        // GET PORTFOLIO STUDENT
                        JSONObject objPs = resp.getJSONObject("portfolio");
                        JSONArray tb_port = objPs.getJSONArray("tb_portfolio");
                        // POPULATE PORTFOLIO STUDENT
                        for (int i = 0; i < tb_port.length(); i++) {
                            JSONObject temp = tb_port.getJSONObject(i);
                            Portfolio p = new Portfolio();

                            // GET DATA FROM JSON
                            int idPortfolio=temp.getInt("id_portfolio");
                            String ds_title=temp.getString("ds_title");
                            String ds_description=temp.getString("ds_description");
                            String nuPortfolio_version=temp.getString("nu_portfolio_version");

                            //POPULATE OBJECT WITH DATA
                            p.setId_Portfolio(idPortfolio);
                            p.setDs_Title(ds_title);
                            p.setDs_Description(ds_description);
                            p.setNu_portfolio_version(nuPortfolio_version);


                            //ADD TO LINKEDLIST

                            basicData.addPortfolio(p);

                        }
                    }
                    if(resp.has("class")) {
                        // GET PORTFOLIO STUDENT
                        JSONObject objPs = resp.getJSONObject("class");
                        JSONArray tb = objPs.getJSONArray("tb_class");
                        // POPULATE PORTFOLIO STUDENT
                        for (int i = 0; i < tb.length(); i++) {
                            JSONObject temp = tb.getJSONObject(i);
                            Class c = new Class();

                            // GET DATA FROM JSON
                            int idClass = temp.getInt("id_class");
                            int idProposer = temp.getInt("id_proposer");
                            String ds_code = temp.getString("ds_code");
                            String ds_description = temp.getString("ds_description");

                            //POPULATE OBJECT WITH DATA
                            c.setId_class(idClass);
                            c.setId_proposer(idProposer);
                            c.setDs_code(ds_code);
                            c.setDs_description(ds_description);

                            //ADD TO LINKEDLIST
                            basicData.addClass(c);
                        }
                        if (objPs.has("classStudent")) {
                            objPs = objPs.getJSONObject("classStudent");
                            tb = objPs.getJSONArray("tb_class_student");
                            for (int i = 0; i < tb.length(); i++) {
                                JSONObject temp = tb.getJSONObject(i);
                                ClassStudent cs = new ClassStudent();

                                // GET DATA FROM JSON
                                int idClassStudent = temp.getInt("id_class_student");
                                int idClass = temp.getInt("id_class");
                                int idStudent = temp.getInt("id_student");

                                //POPULATE OBJECT WITH DATA
                                cs.setId_Class(idClass);
                                cs.setId_Student(idStudent);
                                cs.setId_Class_Student(idClassStudent);


                                //ADD TO LINKEDLIST
                                basicData.addClassStudent(cs);
                            }
                        }
                        if (objPs.has("classTutor")) {
                            objPs = objPs.getJSONObject("classTutor");
                            tb = objPs.getJSONArray("tb_class_tutor");
                            for (int i = 0; i < tb.length(); i++) {
                                JSONObject temp = tb.getJSONObject(i);
                                ClassTutor ct = new ClassTutor();

                                // GET DATA FROM JSON
                                int idClassTutor = temp.getInt("id_class_tutor");
                                int idClass = temp.getInt("id_class");
                                int idTutor = temp.getInt("id_tutor");

                                //POPULATE OBJECT WITH DATA
                                ct.setId_Class(idClass);
                                ct.setId_Tutor(idTutor);
                                ct.setId_Class_Tutor(idClassTutor);


                                //ADD TO LINKEDLIST
                                basicData.addClassTutor(ct);
                            }
                        }
                    }

                    if(resp.has("activityStudent")) {
                        // GET PORTFOLIO STUDENT
                        Log.d(tag, "recebendo activityStudent via json");
                        JSONObject objPs = resp.getJSONObject("activityStudent");
                        JSONArray tb = objPs.getJSONArray("tb_activity_student");
                        // POPULATE PORTFOLIO STUDENT
                        for (int i = 0; i < tb.length(); i++) {
                            JSONObject temp = tb.getJSONObject(i);
                            ActivityStudent activityStudent = new ActivityStudent();

                            // GET DATA FROM JSON
                            if(temp.has("id_activity_student")){
                                int id_activity_student = temp.getInt("id_activity_student");
                                activityStudent.setIdActivityStudent(id_activity_student);
                            }
                            if(temp.has("dt_fisrt_sync")){
                                String dt_fisrt_sync = temp.getString("dt_fisrt_sync");
                                activityStudent.setDt_first_sync(dt_fisrt_sync);
                            }

                            if(temp.has("id_portfolio_student")){
                                int id_portfolio_student = temp.getInt("id_portfolio_student");
                                activityStudent.setIdPortfolioStudent(id_portfolio_student);
                            }
                            if(temp.has("id_activity")){
                                int id_activity = temp.getInt("id_activity");
                                activityStudent.setIdActivity(id_activity);
                            }

                            if(temp.has("dt_conclusion")){
                                String dt_conclusion = temp.getString("dt_conclusion");
                                activityStudent.setDt_conclusion(dt_conclusion);
                            }
                            //POPULATE OBJECT WITH DATA
                            //ADD TO LINKEDLIST
                            basicData.addActivityStudent(activityStudent);
                        }
                    }

                    if(resp.has("actvity")) {
                        // GET PORTFOLIO STUDENT
                        JSONObject objPs = resp.getJSONObject("actvity");
                        JSONArray tb = objPs.getJSONArray("tb_activity");
                        // POPULATE PORTFOLIO STUDENT
                        for (int i = 0; i < tb.length(); i++) {
                            JSONObject temp = tb.getJSONObject(i);
                            Activity act = new Activity();

                            // GET DATA FROM JSON
                            int idActivity = temp.getInt("id_activity");
                            int id_portfolio = temp.getInt("id_portfolio");
                            int nu_order = temp.getInt("nu_order");
                            String dsTitle = temp.getString("ds_title");

                            if(temp.has("ds_description")){
                                String ds_description = temp.getString("ds_description");
                                act.setDs_description(ds_description);
                            }


                            //POPULATE OBJECT WITH DATA
                            act.setId_activity(idActivity);
                            act.setId_portfolio(id_portfolio);
                            act.setNu_order(nu_order);
                            act.setDs_title(dsTitle);


                            //ADD TO LINKEDLIST
                            basicData.addActivity(act);
                        }
                    }

                    if (resp.has("users")) {
                        // GET OBJECT
                        Log.d(tag, "recebendo user via json");
                        JSONObject objPs = resp.getJSONObject("users");
                        JSONArray tb = objPs.getJSONArray("tb_user");
                        // POPULATE OBJECT
                        for (int i = 0; i < tb.length(); i++) {
                            JSONObject temp = tb.getJSONObject(i);
                            User user = new User();

                            // GET DATA FROM JSON
                            int id_user = temp.getInt("id_user");
                            String nm_user = temp.getString("nm_user");
                            String nu_identification = temp.getString("nu_identification");
                            String nu_cellphone = temp.getString("nu_cellphone");
                            String ds_email = temp.getString("ds_email");


                            //POPULATE OBJECT WITH DATA
                            user.setIdUser(id_user);
                            user.setNm_user(nm_user);
                            user.setNu_identification(nu_identification);
                            user.setEmail(ds_email);
                            user.setCellphone(nu_cellphone);

                            //ADD TO LINKEDLIST
                            basicData.addUsers(user);
                        }
                    }
                    if (resp.has("policy")) {
                        // GET OBJECT
                        Log.d(tag, "recebendo policy via json");
                        JSONObject objPs = resp.getJSONObject("policy");
                        if (objPs.has("tb_policy")) {
                            JSONArray tb = objPs.getJSONArray("tb_policy");
                            if (tb.length()>0) {
                                // POPULATE OBJECT
                                for (int i = 0; i < tb.length(); i++) {
                                    JSONObject temp = tb.getJSONObject(i);
                                    Policy policy = new Policy();
                                    PolicyUser policyUser = new PolicyUser();

                                    // GET DATA FROM JSON
                                    int id_policyUser = temp.getInt("idPolicyUser");
                                    int id_user = temp.getInt("idUser");
                                    int id_policy = temp.getInt("idPolicy");
                                    String tx_policy = temp.getString("txPolicy");


                                    //POPULATE OBJECT WITH DATA
                                    policy.setIdPolicy(id_policy);
                                    policy.setTxPolicy(tx_policy);
                                    policyUser.setIdPolicyUser(id_policyUser);
                                    policyUser.setIdPolicy(id_policy);
                                    policyUser.setIdUser(id_user);
                                    policyUser.setFlAccept(null);

                                    //ADD TO LINKEDLIST
                                    basicData.addPolicy(policy);
                                    basicData.addPolicyUsers(policyUser);
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoginActivity.isBasicDataSyncNotSucessful = true;
                } catch (Exception v){
                    v.printStackTrace();
                    LoginActivity.isBasicDataSyncNotSucessful = true;
                }

                Log.d(tag, "Fim  da request");
                //EXECUTE INSERTS IN SQLITE
                basicData.insertDataIntoSQLITE();
                LoginActivity.isBasicDataSucessful = true;
            }
        } finally {

        }
    }

}
