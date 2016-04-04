package com.ufcspa.unasus.appportfolio.WebClient;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by icaromsc on 31/03/2016.
 */
public class FirstLogin {

    //REQUEST
    private String email;
    private String passwd;
    private String idDevice;
    private String tpDevice;
    //RESPONSE




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(String idDevice) {
        this.idDevice = idDevice;
    }

    public String getTpDevice() {
        return tpDevice;
    }

    public void setTpDevice(String tpDevice) {
        this.tpDevice = tpDevice;
    }

    public JSONObject toJSON(){
        JSONObject jsonFinal = new JSONObject();
        JSONObject jsonInternal= new JSONObject();
        try {
            jsonInternal.put("email",getEmail());
            jsonInternal.put("passwd",getPasswd());
            jsonInternal.put("id_device",getIdDevice());
            jsonInternal.put("tp_device",getTpDevice());
            jsonFinal.put("firstLogin_request",jsonInternal);
            Log.d("JSON First login:",jsonFinal.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonFinal;
    }



}
