package com.ufcspa.unasus.appportfolio.Model;

/**
 * Created by Zago on 09/12/2015.
 */
public class Attachment {
    private int id_attachment;
    private int id_comment;
    private int id_activity_student;
    private String ds_local_path;
    private String ds_server_path;
    private String ds_type;

    public Attachment(int id_attachment, int id_comment, int id_activity_student, String ds_local_path, String ds_server_path, String ds_type) {
        this.id_attachment = id_attachment;
        this.id_comment = id_comment;
        this.id_activity_student = id_activity_student;
        this.ds_local_path = ds_local_path;
        this.ds_server_path = ds_server_path;
        this.ds_type = ds_type;
    }

    public int getIdAttachment() {
        return id_attachment;
    }

    public int getIdComment() {
        return id_comment;
    }

    public int getIdActivityStudent() {
        return id_activity_student;
    }

    public String getLocalPath() {
        return ds_local_path;
    }

    public String getServerPath() {
        return ds_server_path;
    }

    public String getType() {
        return ds_type;
    }
}
