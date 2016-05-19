package com.ufcspa.unasus.appportfolio.Model;

/**
 * Created by icaromsc on 18/04/2016.
 */
public class VersionActivity implements Comparable<VersionActivity> {
    private int id_version_activity;
    private int id_activity_student;
    private String tx_activity;
    private String dt_last_access;
    private String dt_submission;
    private String dt_verification;
    private int id_version_activit_srv;

    public VersionActivity() {
        this.id_version_activit_srv = -1;
    }

    public VersionActivity(int id_activity_student, String tx_activity, String dt_last_access, String dt_submission, String dt_verification, int id_version_activit_srv) {
        this.id_activity_student = id_activity_student;
        this.tx_activity = tx_activity;
        this.dt_last_access = dt_last_access;
        this.dt_submission = dt_submission;
        this.dt_verification = dt_verification;
        this.id_version_activit_srv = id_version_activit_srv;
    }


    public int getId_version_activity() {
        return id_version_activity;
    }

    public void setId_version_activity(int id_version_activity) {
        this.id_version_activity = id_version_activity;
    }

    public int getId_activity_student() {
        return id_activity_student;
    }

    public void setId_activity_student(int id_activity_student) {
        this.id_activity_student = id_activity_student;
    }

    public String getTx_activity() {
        return tx_activity;
    }

    public void setTx_activity(String tx_activity) {
        this.tx_activity = tx_activity;
    }

    public String getDt_last_access() {
        return dt_last_access;
    }

    public void setDt_last_access(String dt_last_access) {
        this.dt_last_access = dt_last_access;
    }

    public String getDt_submission() {
        return dt_submission;
    }

    public void setDt_submission(String dt_submission) {
        this.dt_submission = dt_submission;
    }

    public String getDt_verification() {
        return dt_verification;
    }

    public void setDt_verification(String dt_verification) {
        this.dt_verification = dt_verification;
    }

    public int getId_version_activit_srv() {
        return id_version_activit_srv;
    }

    public void setId_version_activit_srv(int id_version_activit_srv) {
        this.id_version_activit_srv = id_version_activit_srv;
    }

    @Override
    public int compareTo(VersionActivity another) {
        if (this.id_version_activity < another.getId_version_activity())
            return 1;
        if (this.id_version_activity > another.getId_version_activity())
            return -1;
        return 0;
    }
}
