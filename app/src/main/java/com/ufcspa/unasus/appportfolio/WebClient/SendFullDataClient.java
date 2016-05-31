package com.ufcspa.unasus.appportfolio.WebClient;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ufcspa.unasus.appportfolio.Activities.MainActivity;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * Created by icaromsc on 22/04/2016.
 */
public class SendFullDataClient extends HttpClient{
    private String method = "fullDataDevSrv";
    private Context context;
    private SendData sendData;


    public SendFullDataClient(Context context,SendData send) {
        super(context);
        this.context=context;
        this.sendData=send;
    }

    public void postJsonAntigo(JSONObject jsonSendFullData) {
        Log.d(tag, "URL: " + URL + method);
        System.out.println("json send fulldata: " + jsonSendFullData.toString());
        JsonObjectRequest jsObjReq = new JsonObjectRequest(Request.Method.POST, URL + method, jsonSendFullData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(tag, "Retornou do request");
                try {
                    Log.d(tag, "JSON RESPONSE: " + response.toString().replaceAll("\\{", "\n{"));
                    if (response.has("error")) {
                        Log.e(tag, "sincronizacao de dados full falhou");
                        MainActivity.sendResponseNotReceived = false;
                    } else if (response.has("fullDataDevSrv_response")) {
                        sendData.dadosResponse = new LinkedHashMap<>();
                        LinkedList<HolderIDS> holdersComments = new LinkedList<>();

                        JSONObject resp = response.getJSONObject("fullDataDevSrv_response");
                        Log.d(tag, "JSON POST existe Send Full Data response");
                        if (resp.has("comment")) {
                            JSONObject comment = resp.getJSONObject("comment");

                            if (comment.has("tb_comment")) {
                                JSONArray tb_comment = comment.getJSONArray("tb_comment");
                                for (int i = 0; i < tb_comment.length(); i++) {
                                    HolderIDS holder = new HolderIDS();
                                    JSONObject temp = tb_comment.getJSONObject(i);
                                    int id_comment = temp.getInt("id_comment");
                                    int id_comment_srv = temp.getInt("id_comment_srv");
                                    String dt_comment_srv = temp.getString("dt_comment_srv");
                                    holder.id = id_comment;
                                    holder.idSrv = id_comment_srv;
                                    holder.date = dt_comment_srv;
                                    holdersComments.add(holder);
                                }
                                sendData.dadosResponse.put("tb_comment", holdersComments);
                            }
                        }
                        if (resp.has("version")) {
                            JSONObject version = resp.getJSONObject("version");
                            LinkedList<HolderIDS> holders = new LinkedList<>();
                            if (version.has("tb_version_activity")) {
                                if (version.get("tb_version_activity") instanceof JSONArray) {
                                    Log.d("json","tem tb_version_activity");
                                    JSONArray tb_version_activity = version.getJSONArray("tb_version_activity");
                                    for (int i = 0; i < tb_version_activity.length(); i++) {
                                        HolderIDS holder = new HolderIDS();
                                        JSONObject temp = tb_version_activity.getJSONObject(i);
                                        int id_version_activity = temp.getInt("id_version_activity");
                                        int id_version_activity_srv = temp.getInt("id_version_activity_srv");
                                        String dt_submission = temp.getString("dt_submission");
                                        holder.id = id_version_activity;
                                        holder.idSrv = id_version_activity_srv;
                                        holder.date = dt_submission;
                                        holders.add(holder);

                                        if (temp.has("tb_comment")) {
                                            Log.d("json","tem tb_comment dentro de version");
                                            JSONArray tb_comment = temp.getJSONArray("tb_comment");
                                            for (int j= 0; j < tb_comment.length(); j++) {
                                                Log.d("json","encontrou comment");
                                                HolderIDS holder2 = new HolderIDS();
                                                JSONObject temp2 = tb_comment.getJSONObject(j);
                                                int id_comment = temp2.getInt("id_comment");
                                                //int id_comment_srv = temp2.getInt("id_comment_srv");
                                                String dt_comment_srv = temp2.getString("dt_send");
                                                holder2.id = id_comment;
                                                //holder2.idSrv = id_comment_srv;
                                                holder2.date = dt_comment_srv;
                                                Log.d("json","holder2:"+holder2.toString());
                                                holdersComments.add(holder2);
                                            }
                                            sendData.dadosResponse.remove("tb_comment");
                                            sendData.dadosResponse.put("tb_comment", holdersComments);
                                            Log.d("json", "deu put em dados response na tb_comment:"+holdersComments);
                                        }else{
                                            Log.d("json","não tem tb_comment dentro de version");
                                        }
                                    }
                                    sendData.dadosResponse.put("tb_version_activity", holders);
                                }
                            }else{
                                Log.d("json","não entrou em version_activity json");
                            }
                        }else{
                            Log.d("json","não entrou em version");
                        }


                        //atualiza dados recebidos via json no sqlite
                        sendData.insertDataOnResponse();
                        MainActivity.shouldSend = false;
                    } else {
                        DataBaseAdapter.getInstance(context).deleteTBUserFromSync();
                    }
                    DataBaseAdapter.getInstance(context).deleteAllNotificationsFromSync();
                } catch (Exception v) {
                    //MainActivity.isFullSyncNotSucessful = true;
                    v.printStackTrace();
                }finally {
                    Log.d(tag, "Fim  da request");
                    //EXECUTE INSERTS IN SQLITE
                    //MainActivity.isFullDataSucessful = true;
                    MainActivity.sendResponseNotReceived = false;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //MainActivity.isFullSyncNotSucessful = true;
                Log.e(tag, "Erro  na request");
                Log.e(tag, "erro=" + volleyError.getMessage());
                volleyError.printStackTrace();
                MainActivity.sendResponseNotReceived = false;
            }
        });

        jsObjReq.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsObjReq);
    }

    public void postJson(JSONObject jsonSendFullData) {
        Log.d(tag, "URL: " + URL + method);
        System.out.println("json send fulldata: " + jsonSendFullData.toString());
        JsonObjectRequest jsObjReq = new JsonObjectRequest(Request.Method.POST, URL + method, jsonSendFullData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(tag, "Retornou do request");
                try {
                    Log.d(tag, "JSON RESPONSE: " + response.toString().replaceAll("\\{", "\n{"));
                    if (response.has("error")) {
                        Log.e(tag, "sincronizacao de dados full falhou");
                        MainActivity.sendResponseNotReceived = false;
                    } else if (response.has("fullDataDevSrv_response")) {
                        sendData.dadosResponse = new LinkedHashMap<>();
                        LinkedList<HolderIDS> holdersComments = new LinkedList<>();

                        JSONObject resp = response.getJSONObject("fullDataDevSrv_response");
                        Log.d(tag, "JSON POST existe Send Full Data response");
                        if (resp.has("comment")) {
                            JSONObject comment = resp.getJSONObject("comment");

                            if (comment.has("tb_comment")) {
                                JSONArray tb_comment = comment.getJSONArray("tb_comment");
                                for (int i = 0; i < tb_comment.length(); i++) {
                                    HolderIDS holder = new HolderIDS();
                                    JSONObject temp = tb_comment.getJSONObject(i);
                                    int id_comment = temp.getInt("id_comment");
                                    int id_comment_srv = temp.getInt("id_comment_srv");
                                    String dt_comment_srv = temp.getString("dt_comment_srv");
                                    holder.id = id_comment;
                                    holder.idSrv = id_comment_srv;
                                    holder.date = dt_comment_srv;
                                    holdersComments.add(holder);
                                }
                                sendData.dadosResponse.put("tb_comment", holdersComments);
                            }
                        }
                        if (resp.has("version")) {
                            JSONObject version = resp.getJSONObject("version");
                            LinkedList<HolderIDS> holders = new LinkedList<>();
                            if (version.has("tb_version_activity")) {
                                if (version.get("tb_version_activity") instanceof JSONObject) {
                                    Log.d("json","tem tb_version_activity");
                                    JSONObject tb_version_activity = version.getJSONObject("tb_version_activity");
                                   // for (int i = 0; i < tb_version_activity.length(); i++) {
                                        HolderIDS holder = new HolderIDS();
                                        JSONObject temp = tb_version_activity;
////                                        int id_version_activity = temp.getInt("id_version_activity");
////                                        int id_version_activity_srv = temp.getInt("id_version_activity_srv");
////                                        String dt_submission = temp.getString("dt_submission");
//                                        holder.id = id_version_activity;
//                                        holder.idSrv = id_version_activity_srv;
//                                        holder.date = dt_submission;
//                                        holders.add(holder);

                                        if (temp.has("tb_comment")) {
                                            Log.d("json","tem tb_comment dentro de version");
                                            JSONObject tb_comment = temp.getJSONObject("tb_comment");
                                           // for (int j= 0; j < tb_comment.length(); j++) {
                                                Log.d("json","encontrou comment");
                                                HolderIDS holder2 = new HolderIDS();
                                                JSONObject temp2 = tb_comment;
                                                int id_comment = temp2.getInt("id_comment");
                                                //int id_comment_srv = temp2.getInt("id_comment_srv");
                                                String dt_comment_srv = temp2.getString("dt_send");
                                                holder2.id = id_comment;
                                                //holder2.idSrv = id_comment_srv;
                                                holder2.date = dt_comment_srv;
                                                Log.d("json","holder2:"+holder2.toString());
                                                holdersComments.add(holder2);
                                            //}//
                                            sendData.dadosResponse.remove("tb_comment");
                                            sendData.dadosResponse.put("tb_comment", holdersComments);
                                            Log.d("json", "deu put em dados response na tb_comment:"+holdersComments);
                                        }else{
                                            Log.d("json","não tem tb_comment dentro de version");
                                        }
                                    //}
                                    sendData.dadosResponse.put("tb_version_activity", holders);
                                }
                            }else{
                                Log.d("json","não entrou em version_activity json");
                            }
                        }else{
                            Log.d("json","não entrou em version");
                        }


                        //atualiza dados recebidos via json no sqlite
                        sendData.insertDataOnResponse();
                        MainActivity.shouldSend = false;
                    } else {
                        DataBaseAdapter.getInstance(context).deleteTBUserFromSync();
                    }
                    DataBaseAdapter.getInstance(context).deleteAllNotificationsFromSync();
                } catch (Exception v) {
                    //MainActivity.isFullSyncNotSucessful = true;
                    v.printStackTrace();
                }finally {
                    Log.d(tag, "Fim  da request");
                    //EXECUTE INSERTS IN SQLITE
                    //MainActivity.isFullDataSucessful = true;
                    MainActivity.sendResponseNotReceived = false;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //MainActivity.isFullSyncNotSucessful = true;
                Log.e(tag, "Erro  na request");
                Log.e(tag, "erro=" + volleyError.getMessage());
                volleyError.printStackTrace();
                MainActivity.sendResponseNotReceived = false;
            }
        });

        jsObjReq.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsObjReq);
    }



}
