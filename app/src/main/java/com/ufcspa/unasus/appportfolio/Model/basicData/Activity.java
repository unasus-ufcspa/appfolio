package com.ufcspa.unasus.appportfolio.Model.basicData;

/**
 * Created by icaromsc on 04/04/2016.
 */
public class Activity{
    private int id_activity;
    private int id_portfolio;
    private int nu_order;
    private String ds_title;
    private String ds_description;

    public int getId_activity() {
        return id_activity;
    }

    public void setId_activity(int id_activity) {
        this.id_activity = id_activity;
    }

    public int getId_portfolio() {
        return id_portfolio;
    }

    public void setId_portfolio(int id_portfolio) {
        this.id_portfolio = id_portfolio;
    }

    public int getNu_order() {
        return nu_order;
    }

    public void setNu_order(int nu_order) {
        this.nu_order = nu_order;
    }

    public String getDs_title() {
        return ds_title;
    }

    public void setDs_title(String ds_title) {
        this.ds_title = ds_title;
    }

    public String getDs_description() {
        return ds_description;
    }

    public void setDs_description(String ds_description) {
        this.ds_description = ds_description;
    }
}
