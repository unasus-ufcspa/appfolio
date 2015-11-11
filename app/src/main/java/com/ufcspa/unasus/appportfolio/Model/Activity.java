package com.ufcspa.unasus.appportfolio.Model;

/**
 * Created by UNASUS on 10/11/2015.
 */
public class Activity
{
    private String ds_title;
    private String ds_description;

    public Activity(String ds_title, String ds_description)
    {
        this.ds_title = ds_title;
        this.ds_description = ds_description;
    }

    public String getTitle()
    {
        return ds_title;
    }

    public String getDescription()
    {
        return ds_description;
    }
}
