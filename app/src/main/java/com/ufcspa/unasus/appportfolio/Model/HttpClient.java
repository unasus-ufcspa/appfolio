package com.ufcspa.unasus.appportfolio.Model;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ufcspa.unasus.appportfolio.Activities.FragmentComments;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desenvolvimento on 18/12/2015.
 */
public class HttpClient {
    private Context context;
    private Comentario comentario=null;
    private static final String ip = "192.168.0.169";
    private static final String URL = "http://" + ip + "/portfolio/";
    private String tag="JSON";

    public HttpClient(Context context) {
        this.context = context;
    }

    public HttpClient(Context context, Comentario comentario) {
        this.context = context;
        this.comentario = comentario;
    }


    public void postJson(JSONObject jsonBody){
        JsonObjectRequest jsObjReq = new JsonObjectRequest(Request.Method.POST, URL + "volley.php", jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(tag,"Retornou do request");
                //try {
                    Log.d(tag,"JSON RESPONSE: "+ response.toString());
//                    if(response.getString("result").equals(0)) {
//                        Log.e(tag, "JSON POST erro");
//                    }else{
//                        Log.d(tag,"JSON POST foi");
//                        if(comentario!=null){
//                            comentario.setIdComment(response.getInt("result"));
//                        }
//                    }
//                } catch (JSONException e) {
//                    Log.e(tag,"JSON on response:"+e.getMessage());
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(tag,"Erro  na request");
                Log.e(tag,volleyError.getMessage());
            }
        });
        RequestQueue  queue= Volley.newRequestQueue(context);
        queue.add(jsObjReq);
    }



    public void getJsonList( final FragmentComments fComm){

        JsonArrayRequest jsonArtRequest = new JsonArrayRequest(URL + "list.php",new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List comments = new ArrayList<Comentario>();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject json = response.getJSONObject(i);
                        Comentario c = new Comentario();
                        c.setIdComment(json.getInt("id_comment"));
                        c.setDateComment(json.getString("tx_comment"));
                        c.setTxtComment(json.getString(""));
                        DataBaseAdapter dao = new DataBaseAdapter(context);
                        if (dao.getCommentById(c.getIdComment()) != null) {
                            dao.updateComment(c);
                        } else {
                            dao.insertComment(c);
                        }
                    }
                    fComm.addItems();
                } catch (JSONException e) {
                    Log.e("erro:", e.getMessage());
                }
            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("ERRO","Erro ao conectar com servidor:"+volleyError.getMessage());
            }
        });
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonArtRequest);
    }



    public void getMessage(final FragmentComments fComm){
        JsonObjectRequest jsObjReq = new JsonObjectRequest(Request.Method.GET, URL + "volley.php", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
                Log.d(tag,"Retornou do request");
                Comentario c = new Comentario();
                try {
                    c.setIdComment(json.getInt("id_comment"));
                    c.setDateComment(json.getString("tx_comment"));
                    c.setTxtComment(json.getString(""));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(tag,"erro ao pegar na request:"+e.getMessage());
                }
                DataBaseAdapter dao = new DataBaseAdapter(context);
                if (dao.getCommentById(c.getIdComment()) != null) {
                    dao.updateComment(c);
                } else {
                    dao.insertComment(c);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(tag,"Erro na request");
                Log.e(tag,volleyError.getMessage());
            }
        });
        RequestQueue  queue= Volley.newRequestQueue(context);
        queue.add(jsObjReq);
    }
}
