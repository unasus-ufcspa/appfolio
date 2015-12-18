package com.ufcspa.unasus.appportfolio.Model;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Desenvolvimento on 18/12/2015.
 */
public class HttpClient {
    private Context context;
    private Comentario comentario=null;
    private static final String URL="http://localhost:8080/portfolio/";
    private String tag="JSON";

    public HttpClient(Context context) {
        this.context = context;
    }

    public HttpClient(Context context, Comentario comentario) {
        this.context = context;
        this.comentario = comentario;
    }


    public void postJson(JSONObject jsonBody){
        final Response.Listener<JSONObject> response = null;
        JsonObjectRequest jsObjReq = new JsonObjectRequest(Request.Method.POST, URL + "volley.php", jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(tag,"Retornou do request");
                try {
                    if(response.getString("result").equals("0"))
                        Log.e(tag, "JSON POST erro");
                    else{
                        Log.d(tag,"JSON POST foi");
                        if(comentario!=null){
                            //comentario.setIdComment(response.getInt("result"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        RequestQueue  queue= Volley.newRequestQueue(context);
        queue.add(jsObjReq);
    }
}
