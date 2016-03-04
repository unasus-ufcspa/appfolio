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
    private  String nameFile;

    public Attachment(int id_attachment, String ds_local_path, String ds_server_path, String ds_type, String nameFile) {
        this.id_attachment = id_attachment;
        this.ds_local_path = ds_local_path;
        this.ds_server_path = ds_server_path;
        this.ds_type = ds_type;
        this.nameFile = nameFile;
    }

    public Attachment() {

    }

    public int getId_attachment() {
        return id_attachment;
    }

    public void setId_attachment(int id_attachment) {
        this.id_attachment = id_attachment;
    }

    public int getId_comment() {
        return id_comment;
    }

    public void setId_comment(int id_comment) {
        this.id_comment = id_comment;
    }

    public int getId_activity_student() {
        return id_activity_student;
    }

    public void setId_activity_student(int id_activity_student) {
        this.id_activity_student = id_activity_student;
    }

    public String getDs_local_path() {
        return ds_local_path;
    }

    public void setDs_local_path(String ds_local_path) {
        this.ds_local_path = ds_local_path;
    }

    public String getDs_server_path() {
        return ds_server_path;
    }

    public void setDs_server_path(String ds_server_path) {
        this.ds_server_path = ds_server_path;
    }

    public String getDs_type() {
        return ds_type;
    }

    public void setDs_type(String ds_type) {
        this.ds_type = ds_type;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

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
