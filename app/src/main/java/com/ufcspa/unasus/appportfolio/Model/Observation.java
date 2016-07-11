package com.ufcspa.unasus.appportfolio.Model;

/**
 * Created by Icaro on 03/06/2016.
 */
public class Observation {
    private int id_comment_version;
    private int id_version_activity;
    private String tx_reference;
    private int nu_comment_activity;
    private int nu_initial_position;
    private int nu_size;
    private int id_comment_version_srv;

    public int getId_comment_version_srv() {
        return id_comment_version_srv;
    }

    public void setId_comment_version_srv(int id_comment_version_srv) {
        this.id_comment_version_srv = id_comment_version_srv;
    }

    public int getId_comment_version() {
        return id_comment_version;
    }

    public void setId_comment_version(int id_comment_version) {
        this.id_comment_version = id_comment_version;
    }

    public int getId_version_activity() {
        return id_version_activity;
    }

    public void setId_version_activity(int id_version_activity) {
        this.id_version_activity = id_version_activity;
    }

    public String getTx_reference() {
        return tx_reference;
    }

    public void setTx_reference(String tx_reference) {
        this.tx_reference = tx_reference;
    }

    public int getNu_comment_activity() {
        return nu_comment_activity;
    }

    public void setNu_comment_activity(int nu_comment_activity) {
        this.nu_comment_activity = nu_comment_activity;
    }

    public int getNu_initial_position() {
        return nu_initial_position;
    }

    public void setNu_initial_position(int nu_initial_position) {
        this.nu_initial_position = nu_initial_position;
    }

    public int getNu_size() {
        return nu_size;
    }

    public void setNu_size(int nu_size) {
        this.nu_size = nu_size;
    }

    @Override
    public String toString() {
        return "Observation{" +
                "id_comment_version=" + id_comment_version +
                ", id_version_activity=" + id_version_activity +
                ", tx_reference='" + tx_reference + '\'' +
                ", nu_comment_activity=" + nu_comment_activity +
                ", nu_initial_position=" + nu_initial_position +
                ", nu_size=" + nu_size +
                ", id_comment_version_srv=" + id_comment_version_srv +
                '}';
    }
}
