package com.ufcspa.unasus.appportfolio.Model;

/**
 * Created by UNASUS on 10/11/2015.
 */
public class Activity implements Comparable<Activity>
{
    private int id_activity;
    private int id_portfolio;
    private int nu_order;
    private String ds_title;
    private String ds_description;

    public Activity(int id_activity, int id_portfolio, int nu_order, String ds_title, String ds_description)
    {
        this.id_activity = id_activity;
        this.id_portfolio = id_portfolio;
        this.nu_order = nu_order;
        this.ds_title = ds_title;
        this.ds_description = ds_description;
    }

    public Activity(int id_activity, String ds_title, String ds_description) {
        this.id_activity = id_activity;
        this.ds_title = ds_title;
        this.ds_description = ds_description;
    }

    public int getIdAtivity() {
        return id_activity;
    }

    public int getIdPortfolio() {
        return id_portfolio;
    }

    public int getNuOrder() {
        return nu_order;
    }

    public String getTitle() {
        return ds_title;
    }

    public String getDescription() {
        return ds_description;
    }

    @Override
    public int compareTo(Activity another)
    {
        if(this.nu_order < another.nu_order)
            return -1;
        if(this.nu_order > another.nu_order)
            return 1;
        return 0;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id_activity=" + id_activity +
                ", ds_title='" + ds_title + '\'' +
                ", ds_description='" + ds_description + '\'' +
                '}';
    }
}
