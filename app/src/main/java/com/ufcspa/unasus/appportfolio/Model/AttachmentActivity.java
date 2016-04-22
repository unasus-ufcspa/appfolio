package com.ufcspa.unasus.appportfolio.Model;

/**
 * Created by icaromsc on 18/04/2016.
 */
public class AttachmentActivity {
    private int id_attach_activity;
    private int id_attachment;
    private int id_activity_student;
    private int id_srv;
    private String file;

    public AttachmentActivity(int id_activity_student, int id_attachment) {
        this.id_activity_student = id_activity_student;
        this.id_attachment = id_attachment;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getId_srv() {
        return id_srv;
    }

    public void setId_srv(int id_srv) {
        this.id_srv = id_srv;
    }

    public int getId_attach_activity() {
        return id_attach_activity;
    }

    public void setId_attach_activity(int id_attach_activity) {
        this.id_attach_activity = id_attach_activity;
    }

    public int getId_attachment() {
        return id_attachment;
    }

    public void setId_attachment(int id_attachment) {
        this.id_attachment = id_attachment;
    }

    public int getId_activity_student() {
        return id_activity_student;
    }

    public void setId_activity_student(int id_activity_student) {
        this.id_activity_student = id_activity_student;
    }
}
