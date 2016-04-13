package com.ufcspa.unasus.appportfolio.WebClient;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentSelectPortfolio;
import com.ufcspa.unasus.appportfolio.Model.basicData.ActivityStudent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Arthur Zettler on 13/04/2016.
 */
public class SyncDataClient extends HttpClient {
    private String method = "sendSync";
    private Context context;
    private SyncData syncData;

    public SyncDataClient(Context context) {
        super(context);
        this.context = context;
    }

    public void postJson(JSONObject json) {
        Log.d(tag, "URL: " + URL + method);
        Log.d(tag, json.toString());
        JsonObjectRequest jsObjReq = new JsonObjectRequest(Request.Method.POST, URL + method, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(tag, "Retornou do request");
                JSONObject rsp = response;
                String jsonStringName = "sendSync_response";
                try {
                    Log.d(tag, "JSON RESPONSE: " + response.toString().replaceAll("\\{", "\n{"));
                    if (response.has("erro")) {
                        FragmentSelectPortfolio.isSyncSincronizationNotSucessful = true;
                        Log.e(tag, "Sync falhou");
                        Log.d(tag, "JSon response receiving:" + response.toString());
                    } else if (response.has("sendSync_response")) {
                        Log.d(tag, "JSON POST existe sync Data response");
                        // INSTANCE BASIC DATA COMPLEX OBJECT
                        syncData = new SyncData(context);
                        JSONObject resp = new JSONObject();
                        try {
                            resp = response.getJSONObject("sendSync_response");
                            Log.d(tag, "JSon response::" + resp.toString());

                            if (resp.has("portfolioStudent")) {
                            }
                            if (resp.has("portfolio_class")) {
                            }
                            if (resp.has("portfolio")) {
                            }
                            if (resp.has("class")) {
                            }

                            if (resp.has("activityStudent")) {
                                // GET PORTFOLIO STUDENT
                                Log.d(tag, "recebendo activityStudent via json");
                                JSONObject objPs = resp.getJSONObject("activityStudent");
                                JSONArray tb = objPs.getJSONArray("tb_activity_student");
                                // POPULATE PORTFOLIO STUDENT
                                for (int i = 0; i < tb.length(); i++) {
                                    JSONObject temp = tb.getJSONObject(i);
                                    ActivityStudent activityStudent = new ActivityStudent();

                                    // GET DATA FROM JSON
                                    if (temp.has("id_activity_student")) {
                                        int id_activity_student = temp.getInt("id_activity_student");
                                        activityStudent.setIdActivityStudent(id_activity_student);
                                    }
                                    if (temp.has("dt_fisrt_sync")) {
                                        String dt_fisrt_sync = temp.getString("dt_fisrt_sync");
                                        activityStudent.setDt_first_sync(dt_fisrt_sync);
                                    }

                                    if (temp.has("id_portfolio_student")) {
                                        int id_portfolio_student = temp.getInt("id_portfolio_student");
                                        activityStudent.setIdPortfolioStudent(id_portfolio_student);
                                    }
                                    if (temp.has("id_activity")) {
                                        int id_activity = temp.getInt("id_activity");
                                        activityStudent.setIdActivity(id_activity);
                                    }

                                    if (temp.has("dt_conclusion")) {
                                        String dt_conclusion = temp.getString("dt_conclusion");
                                        activityStudent.setDt_conclusion(dt_conclusion);
                                    }
                                    //POPULATE OBJECT WITH DATA
                                    //ADD TO LINKEDLIST
                                    //basicData.addActivityStudent(activityStudent);
                                }
                            }

                            if (resp.has("actvity")) {
                            }

                            if (resp.has("user")) {
                            }

                            //EXECUTE INSERTS IN SQLITE
                            syncData.insertDataIntoSQLITE();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            FragmentSelectPortfolio.isSyncSincronizationNotSucessful = true;
                        } catch (Exception v) {
                            v.printStackTrace();
                            FragmentSelectPortfolio.isSyncSincronizationNotSucessful = true;
                        }
                    }
                } finally {
                    Log.d(tag, "Fim  da request");
                    FragmentSelectPortfolio.isSyncSucessful = true;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(tag, "Erro  na request");
                volleyError.printStackTrace();
                Log.e(tag, "erro=" + volleyError.getMessage());
                FragmentSelectPortfolio.isSyncSincronizationNotSucessful = true;
            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsObjReq);
    }

}
