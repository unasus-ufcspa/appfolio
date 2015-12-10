package com.ufcspa.unasus.appportfolio.Model;

/**
 * Created by Desenvolvimento on 10/12/2015.
 */
public class ActivityStudent {
    private int idActivityStudent;
    private String txtActivity;
    private String dtLastAcess;

    public int getIdActivityStudent() {
        return idActivityStudent;
    }

    public void setIdActivityStudent(int idActivityStudent) {
        this.idActivityStudent = idActivityStudent;
    }

    public String getTxtActivity() {
        return txtActivity;
    }

    public void setTxtActivity(String txtActivity) {
        this.txtActivity = txtActivity;
    }

    public String getDtLastAcess() {
        return dtLastAcess;
    }

    public void setDtLastAcess(String dtLastAcess) {
        this.dtLastAcess = dtLastAcess;
    }
}
