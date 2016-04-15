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
import com.ufcspa.unasus.appportfolio.Model.Singleton;
import com.ufcspa.unasus.appportfolio.Model.Sync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Arthur Zettler on 13/04/2016.
 */
public class SyncDataClient extends HttpClient {
    private String method = "sync";
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
                String jsonStringName = "sync_response";
                try {
                    Log.d(tag, "JSON RESPONSE: " + response.toString().replaceAll("\\{", "\n{"));
                    if (response.has("erro")) {
                        FragmentSelectPortfolio.isSyncSincronizationNotSucessful = true;
                        Log.e(tag, "Sync falhou");
                        Log.d(tag, "JSon response receiving:" + response.toString());
                    } else if (response.has("sync_response")) {
                        Log.d(tag, "JSON POST existe sync Data response");
                        // INSTANCE SyncData COMPLEX OBJECT
                        syncData = new SyncData(context);
                        try {
                            JSONObject resp = response.getJSONObject("sync_response");

                            if (resp.has("tb_sync")) {
                                String id_device = Singleton.getInstance().device.get_id_device();
                                JSONArray tb_sync_resp = resp.getJSONArray("tb_sync");
                                // POPULATE PORTFOLIO STUDENT
                                for (int i = 0; i < tb_sync_resp.length(); i++) {
                                    JSONObject temp = tb_sync_resp.getJSONObject(i);
                                    Sync sync = new Sync();

                                    sync.setId_device(id_device);
                                    sync.setTp_sync("R");

                                    if (temp.has("id_activity_student")) {
                                        int id_activity_student = temp.getInt("id_activity_student");
                                        sync.setId_activity_student(id_activity_student);
                                    }
                                    if (temp.has("nm_table")) {
                                        String nm_table = temp.getString("nm_table");
                                        sync.setNm_table(nm_table);
                                    }
                                    if (temp.has("co_id_table")) {
                                        int co_id_table = temp.getInt("co_id_table");
                                        sync.setCo_id_table(co_id_table);
                                    }
                                    if (temp.has("dt_sync")) {
                                        String dt_sync = temp.getString("dt_sync");
                                        sync.setDt_sync(dt_sync);
                                    }

                                    //ADD TO LINKEDLIST
                                    syncData.addSync(sync);
                                }

                                //EXECUTE INSERTS IN SQLITE
                                syncData.insertDataIntoSQLITE();
                            }
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
        }

        );
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsObjReq);
    }

}
