package com.ufcspa.unasus.appportfolio.Model;

import java.util.Date;

/**
 * Created by Convidado on 04/11/2015.
 */
public class Team
{
    private int id_class;
    private int id_proposer;
    private String ds_code;
    private String ds_description;
    private char st_status;
    private Date dt_start;
    private Date dt_finish;

    public Team(int id_class, int id_proposer, String ds_code, String ds_description, char st_status, Date dt_start, Date dt_finish) {
        this.id_class = id_class;
        this.id_proposer = id_proposer;
        this.ds_code = ds_code;
        this.ds_description = ds_description;
        this.st_status = st_status;
        this.dt_start = dt_start;
        this.dt_finish = dt_finish;
    }

    public int getIdClass() {
        return id_class;
    }

    public String getCode() {
        return ds_code;
    }

    public String getDescription() {
        return ds_description;
    }

    public char getStatus() {
        return st_status;
    }

    public Date getDateStart() {
        return dt_start;
    }

    public Date getDateFinish() { return dt_finish; }

    public int getIdProposer() {return id_proposer;}
}
