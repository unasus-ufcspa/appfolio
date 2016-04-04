package com.ufcspa.unasus.appportfolio.Model.basicData;

/**
 * Created by icaromsc on 04/04/2016.
 */
public class PortfolioStudent {
    private int id_portfolio_student;
    private int id_portfolio_class;
    private int id_student;
    private int id_tutor;
    private String dt_first_sync;
    private String nu_portfolio_version;

    public int getId_portfolio_student() {
        return id_portfolio_student;
    }

    public void setId_portfolio_student(int id_portfolio_student) {
        this.id_portfolio_student = id_portfolio_student;
    }

    public int getId_portfolio_class() {
        return id_portfolio_class;
    }

    public void setId_portfolio_class(int id_portfolio_class) {
        this.id_portfolio_class = id_portfolio_class;
    }

    public int getId_student() {
        return id_student;
    }

    public void setId_student(int id_student) {
        this.id_student = id_student;
    }

    public int getId_tutor() {
        return id_tutor;
    }

    public void setId_tutor(int id_tutor) {
        this.id_tutor = id_tutor;
    }

    public String getDt_first_sync() {
        return dt_first_sync;
    }

    public void setDt_first_sync(String dt_first_sync) {
        this.dt_first_sync = dt_first_sync;
    }

    public String getNu_portfolio_version() {
        return nu_portfolio_version;
    }

    public void setNu_portfolio_version(String nu_portfolio_version) {
        this.nu_portfolio_version = nu_portfolio_version;
    }



}
