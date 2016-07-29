package com.ufcspa.unasus.appportfolio.Model;

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

    public PolicyUser(int idPolicyUser, String flAccept, int idPolicy, int idUser) {
        this.idPolicyUser = idPolicyUser;
        this.flAccept = flAccept;
        this.idPolicy = idPolicy;
        this.idUser = idUser;
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
