package com.ufcspa.unasus.appportfolio.Model.WebClient;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ufcspa.unasus.appportfolio.Activities.LoginActivity;
import com.ufcspa.unasus.appportfolio.Model.User;
import com.ufcspa.unasus.appportfolio.database.DataBaseAdapter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by icaromsc on 29/03/2016.
 */
public class FistLoginClient extends HttpClient {
    private Context context;
    private String method="firstLogin";
    private User user;

    public FistLoginClient(Context context) {
        super(context);
    }

    public void postJson(JSONObject jsonFirstRequest){
        Log.d(tag, "URL: " + URL);

        JsonObjectRequest jsObjReq = new JsonObjectRequest(Request.Method.POST, URL+method, jsonFirstRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(tag,"Retornou do request");
                try {
                    Log.d(tag,"JSON RESPONSE: "+ response.toString());
                    if( response.getString("erro").length()>2) {
                        Log.e(tag, "JSON usuario ou senha invalidos");
                        LoginActivity.isLoginSucessful=false;
                    }else if(response.getJSONObject("tb_user").getInt("id_user")!=0){
                        Log.d(tag,"JSON POST foi");
                        Log.d(tag,"user encontrado!\n cadastrando no banco...");

                        //RECUPERANDO DADOS DO JSON RESPONSE
                        Integer idUser=response.getJSONObject("tb_user").getInt("id_user");
                        String name=response.getJSONObject("tb_user").getString("nm_user");
                        String idCode=response.getJSONObject("tb_user").getString("nu_identification");
                        String email=response.getJSONObject("tb_user").getString("ds_email");
                        String cellphone=response.getJSONObject("tb_user").getString("nu_cellphone");;


                        user = new User(idUser,name,idCode,email,cellphone);
                        DataBaseAdapter.getInstance(context).insertUser(user);
                        LoginActivity.isLoginSucessful=true;
                    }
                } catch (JSONException e) {
                    Log.e(tag,"JSON exception on response:"+e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(tag,"Erro  na request");
                Log.e(tag,"erro="+volleyError.getMessage());
            }
        });
        RequestQueue queue= Volley.newRequestQueue(context);
        queue.add(jsObjReq);
    }

}
