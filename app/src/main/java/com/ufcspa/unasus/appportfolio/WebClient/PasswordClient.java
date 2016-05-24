package com.ufcspa.unasus.appportfolio.WebClient;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ufcspa.unasus.appportfolio.Activities.Fragments.FragmentConfig;
import com.ufcspa.unasus.appportfolio.Model.Singleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

/**
 * Created by Arthur Zettler on 24/05/2016.
 */
public class PasswordClient extends HttpClient {
    private Context context;
    private String method = "password";
    private Singleton singleton;
    private FragmentActivity activity;

    public PasswordClient(Context context, FragmentActivity activity) {
        super(context);
        this.context = context;
        this.singleton = Singleton.getInstance();
        this.activity = activity;
    }

    // TODO Formarto do JSON de envio e de recebimento
    public void postJson(String old_pass, String new_pass) {
        JSONObject logoutJson = null;
        try {
            logoutJson = new JSONObject().put("password_request", new JSONObject().put("old_pass", old_pass).put("new_pass", new_pass));
        } catch (JSONException e) {
//            FragmentConfig.couldNotLogout();
            return;
        }

        JsonObjectRequest jsObjReq = new JsonObjectRequest(Request.Method.POST, URL + method, logoutJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(tag, "JSON RESPONSE: " + response.toString());

                    if (response.has("password"))
                        if (response.getJSONObject("password").has("success")) {
//                            FragmentConfig.logout(activity);
                            return;
                        }

                } catch (JSONException e) {
                    FragmentConfig.couldNotLogout();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(tag, "Erro  na request");
                volleyError.printStackTrace();
                FragmentConfig.couldNotLogout();
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