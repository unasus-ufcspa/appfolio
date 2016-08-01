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
import com.ufcspa.unasus.appportfolio.Model.PolicyUser;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

/**
 * Created by Steffano on 29/07/2016.
 */
public class PolicyUserClient extends HttpClient{
    private String method = "policy";
    private Context context;
    private PolicyUser policyUser;


    public PolicyUserClient(Context context) {
        super(context);
        this.context=context;
    }

    public void postJson(JSONObject jsonSendFullData) {
        Log.d(tag, "URL: " + URL + method);
        System.out.println("json policyUser: " + jsonSendFullData.toString());
        JsonObjectRequest jsObjReq = new JsonObjectRequest(Request.Method.POST, URL + method, jsonSendFullData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(tag, "Retornou do request");
                try {
                    Log.d(tag, "JSON RESPONSE: " + response.toString().replaceAll("\\{", "\n{"));
                    JSONObject resp = response.getJSONObject("policyResponse");
                    Log.d(tag, "JSON POST existe policyResponse");
                    if (resp.has("success")) {
                        Log.d("json policyUser","sucesso");
                    } else {
                        Log.d("json policyUser","Falhou");
                    }
                } catch (Exception v) {
                    //MainActivity.isFullSyncNotSucessful = true;
                    v.printStackTrace();
                }finally {
                    Log.d(tag, "Fim  da request");
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

        jsObjReq.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsObjReq);
    }
}
