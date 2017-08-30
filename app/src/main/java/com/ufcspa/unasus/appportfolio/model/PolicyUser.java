package com.ufcspa.unasus.appportfolio.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Steffano on 26/07/2016.
 */
public class PolicyUser {
    private int idPolicyUser;
    private int idPolicy;
    private int idUser;
    private String flAccept;

    public PolicyUser() {
    }

    public PolicyUser(int idPolicyUser, int idPolicy, int idUser, String flAccept) {
        this.idPolicyUser = idPolicyUser;
        this.idPolicy = idPolicy;
        this.idUser = idUser;
        this.flAccept = flAccept;
    }

    public static JSONObject toJSON(int idPolicyUser, int idUser, String flAccept) {
        JSONObject json = new JSONObject();
        JSONObject policy = new JSONObject();
        try {
            policy.put("id_policy_user", idPolicyUser);
            policy.put("id_user", idUser);
            policy.put("fl_accept", flAccept);
            json.put("policyRequest", policy);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public int getIdPolicy() {
        return idPolicy;
    }

    public void setIdPolicy(int idPolicy) {
        this.idPolicy = idPolicy;
    }

    public int getIdPolicyUser() {
        return idPolicyUser;
    }

    public void setIdPolicyUser(int idPolicyUser) {
        this.idPolicyUser = idPolicyUser;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getFlAccept() {
        return flAccept;
    }

    public void setFlAccept(String flAccept) {
        this.flAccept = flAccept;
    }

    @Override
    public String toString() {
        return "Policy{" +
                "idPolicyUser='" + idPolicyUser + '\'' +
                "idPolicy='" + idPolicy + '\'' +
                "idUser='" + idUser + '\'' +
                "flAccept='" + flAccept + '\'' +
                '}';
    }
}
