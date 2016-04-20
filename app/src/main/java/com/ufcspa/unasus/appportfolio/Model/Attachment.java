package com.ufcspa.unasus.appportfolio.Model;

/**
 * Created by Zago on 09/12/2015.
 */
public class Attachment {
    public static final String TYPE_VIDEO="V";
    public static final String TYPE_IMAGE="I";
    public static final String TYPE_TEXT="T";
    private int id_attachment;
    private String tp_attachment;
    private String nm_file;
    private String nm_system;
    private int id_attachment_srv;


    public Attachment(int id_attachment, String tp_attachment, String nm_file, String nm_system, int id_attachment_srv) {
        this.id_attachment = id_attachment;
        this.tp_attachment = tp_attachment;
        this.nm_file = nm_file;
        this.nm_system = nm_system;
        this.id_attachment_srv = id_attachment_srv;
    }

    public Attachment() {

    }

    public int getIdAttachment() {
        return id_attachment;
    }

    public void setIdAttachment(int id_attachment) {
        this.id_attachment = id_attachment;
    }

    public int getidAttachmentSrv() {
        return id_attachment_srv;
    }

    public void setIdAttachmentSrv(int id_attachment_srv) {
        this.id_attachment_srv = id_attachment_srv;
    }

    public String getTpAttachment() {
        return tp_attachment;
    }

    public void setTpAttachment(String tp_attachment) {
        this.tp_attachment = tp_attachment;
    }

    public String getNmFile() {
        return nm_file;
    }

    public void setNmFile(String nm_file) {
        this.nm_file = nm_file;
    }

    public String getNmSystem() {
        return nm_system;
    }

    public void setNmSystem(String nm_system) {
        this.nm_system = nm_system;
    }
}
