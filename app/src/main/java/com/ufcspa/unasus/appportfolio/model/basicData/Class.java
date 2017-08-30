package com.ufcspa.unasus.appportfolio.model.basicData;

import java.util.Date;

/**
 * Created by icaromsc on 04/04/2016.
 */
public class Class {
    private int id_class;
    private int id_proposer;
    private String ds_code;
    private String ds_description;
    private char st_status;
    private Date dt_start;
    private Date dt_finish;

    public int getId_class() {
        return id_class;
    }

    public void setId_class(int id_class) {
        this.id_class = id_class;
    }

    public int getId_proposer() {
        return id_proposer;
    }

    public void setId_proposer(int id_proposer) {
        this.id_proposer = id_proposer;
    }

    public String getDs_code() {
        return ds_code;
    }

    public void setDs_code(String ds_code) {
        this.ds_code = ds_code;
    }

    public String getDs_description() {
        return ds_description;
    }

    public void setDs_description(String ds_description) {
        this.ds_description = ds_description;
    }

    public char getSt_status() {
        return st_status;
    }

    public void setSt_status(char st_status) {
        this.st_status = st_status;
    }

    public Date getDt_start() {
        return dt_start;
    }

    public void setDt_start(Date dt_start) {
        this.dt_start = dt_start;
    }

    public Date getDt_finish() {
        return dt_finish;
    }

    public void setDt_finish(Date dt_finish) {
        this.dt_finish = dt_finish;
    }
}
