package com.ufcspa.unasus.appportfolio.WebClient;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.LinkedList;

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

    public void postJson(JSONObject jsonSendFullData) {
        Log.d(tag, "URL: " + URL + method);
        JsonObjectRequest jsObjReq = new JsonObjectRequest(Request.Method.POST, URL + method, jsonSendFullData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(tag, "Retornou do request");
                try {
                    Log.d(tag, "JSON RESPONSE: " + response.toString().replaceAll("\\{", "\n{"));
                    if (response.has("erro")) {
                        Log.e(tag, "sincronizacao de dados full falhou");
                    } else if (response.has("fullDataDevSrv_response")) {
                        sendData.dadosResponse = new LinkedHashMap<>();

                        JSONObject resp = response.getJSONObject("fullDataDevSrv_response");
                        Log.d(tag, "JSON POST existe Send Full Data response");
                        if (resp.has("comment")) {
                            JSONObject comment = resp.getJSONObject("comment");
                            LinkedList<HolderIDS> holders = new LinkedList<>();
                            if (comment.has("tb_comment")) {
                                JSONArray tb_comment = comment.getJSONArray("tb_comment");
                                for (int i = 0; i < tb_comment.length(); i++) {
                                    HolderIDS holder = new HolderIDS();
                                    JSONObject temp = tb_comment.getJSONObject(i);
                                    int id_comment = temp.getInt("id_comment");
                                    int id_comment_srv = temp.getInt("id_comment_srv");
                                    holder.id = id_comment;
                                    holder.idSrv = id_comment_srv;

                                    holders.add(holder);
                                }
                                sendData.dadosResponse.put("tb_comment", holders);
                            }
                        }

                        //atualiza dados recebidos via json no sqlite
                        sendData.insertDataOnResponse();
                    }
                } catch (Exception v) {
                    //MainActivity.isFullSyncNotSucessful = true;
                    v.printStackTrace();
                }finally {
                    Log.d(tag, "Fim  da request");
                    //EXECUTE INSERTS IN SQLITE
                    //MainActivity.isFullDataSucessful = true;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //MainActivity.isFullSyncNotSucessful = true;
                Log.e(tag, "Erro  na request");
                Log.e(tag, "erro=" + volleyError.getMessage());
                volleyError.printStackTrace();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsObjReq);
    }
}
