package com.ufcspa.unasus.appportfolio.WebClient;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ufcspa.unasus.appportfolio.Activities.LoginActivity;
import com.ufcspa.unasus.appportfolio.Activities.LoginActivity2;
import com.ufcspa.unasus.appportfolio.Model.Device;
import com.ufcspa.unasus.appportfolio.Model.Singleton;
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
        this.context=context;
    }



    public void postJson(JSONObject jsonFirstRequest){
        Log.d(tag, "URL: " + URL + method);

        JsonObjectRequest jsObjReq = new JsonObjectRequest(Request.Method.POST, URL+method, jsonFirstRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(tag,"Retornou do request");
                JSONObject rsp = response;

                try {
                    Log.d(tag, "JSON RESPONSE: " + response.toString());
                    if( response.has("firstLogin_response")) {
                        if(response.getJSONObject("firstLogin_response").has("erro")){
                            Log.e(tag, "JSON usuario ou senha invalidos");
                            LoginActivity.isLoginSucessful=false;
                            Log.d(tag, "JSon response receiving:" + response.getJSONObject("firstLogin_response").get("erro"));
                        }else {
                            if(response.getJSONObject("firstLogin_response").has("tb_user")){
                                Log.d(tag,"JSON POST foi");
                                Log.d(tag, "user encontrado!\n cadastrando no banco...");
                                JSONObject resp= new JSONObject();
                                try {
                                    resp=response.getJSONObject("firstLogin_response");
                                    Log.wtf(tag,"JSon response::"+resp.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (Exception v){
                                    v.printStackTrace();
                                }
                                //RECUPERANDO DADOS DO JSON RESPONSE
                                Integer idUser=resp.getJSONObject("tb_user").getInt("id_user");
                                String name=resp.getJSONObject("tb_user").getString("nm_user");
                                String idCode=resp.getJSONObject("tb_user").getString("nu_identification");
                                String email=resp.getJSONObject("tb_user").getString("ds_email");
                                String cellphone=resp.getJSONObject("tb_user").getString("nu_cellphone");




                                //INSERINDO USER NO DB SQLITE
                                user = new User(idUser,name,idCode,email,cellphone);
                                Log.d(tag, "user get by json:" + user.toString());
                                Log.d(tag, "user this user in sqlite...");


                                DataBaseAdapter.getInstance(context).insertUser(user);
                                DataBaseAdapter.getInstance(context).insertIntoTbDevice(new Device(Singleton.getInstance().device.get_id_device(),user.getIdUser(),Singleton.getInstance().device.get_tp_device()));

                                //Adicionando o usuario no singleton
                                Singleton.getInstance().user = user;
                                Log.d(tag, "user get by singleton:" + Singleton.getInstance().user.toString());

                                LoginActivity2.isLoginSucessful=true;
                            }
                        }


                    }else {
                        Log.e(tag,"não encontrou firstLogin_response no json");
                        LoginActivity2.isDataSyncNotSucessful = true;
                    }
                } catch (JSONException e) {
                    Log.e(tag,"JSON exception on response:"+e.getMessage());
                }finally {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(tag, "Erro  na request");
                volleyError.printStackTrace();
                //Log.wtf(tag,"Network response:"+volleyError.networkResponse.statusCode);
                Log.e(tag,"erro="+volleyError.getMessage());
                LoginActivity2.isDataSyncNotSucessful=true;
            }
        });
        RequestQueue queue= Volley.newRequestQueue(context);
        queue.add(jsObjReq);
    }

}
