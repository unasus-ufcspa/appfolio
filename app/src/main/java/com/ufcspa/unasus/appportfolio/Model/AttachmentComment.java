package com.ufcspa.unasus.appportfolio.Model;

/**
 * Created by icaromsc on 18/04/2016.
 */
public class AttachmentComment {
    private int id_attach_comment;
    private int id_attachment;
    private int id_comment;
    private int id_srv;

    public int getId_srv() {
        return id_srv;
    }

    public void setId_srv(int id_srv) {
        this.id_srv = id_srv;
    }

    public int getId_attach_comment() {
        return id_attach_comment;
    }

    public void setId_attach_comment(int id_attach_comment) {
        this.id_attach_comment = id_attach_comment;
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
}
