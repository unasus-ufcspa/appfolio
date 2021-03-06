package com.ufcspa.unasus.appportfolio.model;

/**
 * Created by Desenvolvimento on 23/12/2015.
 */
public class Reference {
    private int idRef;
    private String dsUrl;
    private int idActStudent;
    private int idRefSrv;

    public Reference(){}

    public Reference(int idRef, String ds_url, int idActStudent) {
        this.idRef = idRef;
        this.dsUrl = ds_url;
        this.idActStudent = idActStudent;
    }

    @Override
    public String toString() {
        return "Reference{" +
                "dsUrl='" + dsUrl + '\'' +
                ", idRef=" + idRef +
                ", idActStudent=" + idActStudent +
                '}';
    }

    public int getIdRef() {
        return idRef;
    }

    public void setIdRef(int idRef) {
        this.idRef = idRef;
    }

    public String getDsUrl() {
        return dsUrl;
    }

    public void setDsUrl(String ds_url) {
        this.dsUrl = ds_url;
    }

    public int getIdActStudent() {
        return idActStudent;
    }

    public void setIdActStudent(int idActStudent) {
        this.idActStudent = idActStudent;
    }

    public int getIdRefSrv() {
        return idRefSrv;
    }

    public void setIdRefSrv(int idRefSrv) {
        this.idRefSrv = idRefSrv;
    }
}
