package com.ufcspa.unasus.appportfolio.Model.basicData;

/**
 * Created by icaromsc on 04/04/2016.
 */
public class Portfolio {
    private int id_Portfolio;
    private String ds_Title;
    private String ds_Description;
    private String nu_portfolio_version;


    public int getId_Portfolio() {
        return id_Portfolio;
    }

    public void setId_Portfolio(int id_Portfolio) {
        this.id_Portfolio = id_Portfolio;
    }

    public String getDs_Title() {
        return ds_Title;
    }

    public void setDs_Title(String ds_Title) {
        this.ds_Title = ds_Title;
    }

    public String getDs_Description() {
        return ds_Description;
    }

    public void setDs_Description(String ds_Description) {
        this.ds_Description = ds_Description;
    }

    public String getNu_portfolio_version() {
        return nu_portfolio_version;
    }

    public void setNu_portfolio_version(String nu_portfolio_version) {
        this.nu_portfolio_version = nu_portfolio_version;
    }
}
