package com.ufcspa.unasus.appportfolio.Model;

/**
 * Created by Steffano on 26/07/2016.
 */
public class Policy {
    private int idPolicy;
    private String txPolicy;

    public Policy() {
    }

    public Policy(int idPolicy, String txPolicy) {
        this.idPolicy = idPolicy;
        this.txPolicy = txPolicy;
    }

    public int getIdPolicy() {
        return idPolicy;
    }

    public void setIdPolicy(int idPolicy) {
        this.idPolicy = idPolicy;
    }

    public String getTxPolicy() {
        return txPolicy;
    }

    public void setTxPolicy(String txPolicy) {
        this.txPolicy = txPolicy;
    }
    @Override
    public String toString() {
        return "Policy{" +
                "idPolicy='" + idPolicy + '\'' +
                "txPolicy='" + txPolicy + '\'' +
                '}';
    }
}
