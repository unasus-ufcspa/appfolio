package com.ufcspa.unasus.appportfolio.Model;

/**
 * Created by icaromsc on 05/05/2016.
 */
public class CommentVersion {
    private int id_comment_version;
    private int id_version_activity;
    private int id_comment;
    private char fl_active;

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

    public int getId_comment() {
        return id_comment;
    }

    public void setId_comment(int id_comment) {
        this.id_comment = id_comment;
    }

    public char getFl_active() {
        return fl_active;
    }

    public void setFl_active(char fl_active) {
        this.fl_active = fl_active;
    }
}
