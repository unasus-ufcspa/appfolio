package com.ufcspa.unasus.appportfolio.model.basicData;

/**
 * Created by icaromsc on 04/04/2016.
 */
public class ActivityStudent {
    private int idActivityStudent;
    private int idPortfolioStudent;
    private int idActivity;
    private String dt_conclusion;
    private String dt_first_sync;

    public int getIdActivityStudent() {
        return idActivityStudent;
    }

    public void setIdActivityStudent(int idActivityStudent) {
        this.idActivityStudent = idActivityStudent;
    }

    public int getIdPortfolioStudent() {
        return idPortfolioStudent;
    }

    public void setIdPortfolioStudent(int idPortfolioStudent) {
        this.idPortfolioStudent = idPortfolioStudent;
    }

    public int getIdActivity() {
        return idActivity;
    }

    public void setIdActivity(int idActivity) {
        this.idActivity = idActivity;
    }

    public String getDt_conclusion() {
        return dt_conclusion;
    }

    public void setDt_conclusion(String dt_conclusion) {
        this.dt_conclusion = dt_conclusion;
    }

    public String getDt_first_sync() {
        return dt_first_sync;
    }

    public void setDt_first_sync(String dt_first_sync) {
        this.dt_first_sync = dt_first_sync;
    }
}
