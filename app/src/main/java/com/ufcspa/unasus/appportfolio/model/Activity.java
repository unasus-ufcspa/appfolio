package com.ufcspa.unasus.appportfolio.model;

/**
 * Created by UNASUS on 10/11/2015.
 */
public class Activity implements Comparable<Activity>
{
    private int id_activity;
    private int id_activity_student;
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

    public Activity(int id_activity_student, int id_activity, String ds_title, String ds_description, int nu_order) {
        this.id_activity_student = id_activity_student;
        this.ds_title = ds_title;
        this.ds_description = ds_description;
        this.id_activity = id_activity;
        this.nu_order = nu_order;
    }

    public int getIdActivityStudent() {
        return id_activity_student;
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

    public void setId_portfolio(int id_portfolio) {
        this.id_portfolio = id_portfolio;
    }

    public void setNu_order(int nu_order) {
        this.nu_order = nu_order;
    }

    public void setDs_title(String ds_title) {
        this.ds_title = ds_title;
    }

    public void setDs_description(String ds_description) {
        this.ds_description = ds_description;
    }

    public int getIdActivity() {
        return id_activity;
    }

    public void setIdActivity(int id_activity) {
        this.id_activity = id_activity;
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
                "id_activity_student=" + id_activity_student +
                ", ds_title='" + ds_title + '\'' +
                ", ds_description='" + ds_description + '\'' +
                '}';
    }
}
