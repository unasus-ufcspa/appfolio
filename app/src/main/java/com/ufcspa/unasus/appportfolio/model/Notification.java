package com.ufcspa.unasus.appportfolio.model;

/**
 * Created by Arthur Zettler on 19/04/2016.
 */
public class Notification {
    private int id_notification;
    private int id_notice;
    private int id_author;
    private int id_destination;
    private int id_activity_student;
    private String nm_table;
    private int co_id_table_srv;
    private String dt_notice;
    private String dt_read;

    public Notification() {
    }

    public int getId_notification() {
        return id_notification;
    }

    public void setId_notification(int id_notification) {
        this.id_notification = id_notification;
    }

    public int getId_notice() {
        return id_notice;
    }

    public void setId_notice(int id_notice) {
        this.id_notice = id_notice;
    }

    public int getId_author() {
        return id_author;
    }

    public void setId_author(int id_author) {
        this.id_author = id_author;
    }

    public int getId_destination() {
        return id_destination;
    }

    public void setId_destination(int id_destination) {
        this.id_destination = id_destination;
    }

    public int getId_activity_student() {
        return id_activity_student;
    }

    public void setId_activity_student(int id_activity_student) {
        this.id_activity_student = id_activity_student;
    }

    public String getNm_table() {
        return nm_table;
    }

    public void setNm_table(String nm_table) {
        this.nm_table = nm_table;
    }

    public int getCo_id_table_srv() {
        return co_id_table_srv;
    }

    public void setCo_id_table_srv(int co_id_table_srv) {
        this.co_id_table_srv = co_id_table_srv;
    }

    public String getDt_notice() {
        return dt_notice;
    }

    public void setDt_notice(String dt_notice) {
        this.dt_notice = dt_notice;
    }

    public String getDt_read() {
        return dt_read;
    }

    public void setDt_read(String dt_read) {
        this.dt_read = dt_read;
    }
}
