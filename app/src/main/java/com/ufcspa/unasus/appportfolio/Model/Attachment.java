package com.ufcspa.unasus.appportfolio.Model;

/**
 * Created by Zago on 09/12/2015.
 */
public class Attachment {
    private int id_attachment;
    private String ds_local_path;
    private String ds_server_path;
    private String ds_type;
    private String nameFile;
    private int id_attachment_srv;
    public static final String TYPE_VIDEO="V";
    public static final String TYPE_IMAGE="I";
    public static final String TYPE_TEXT="T";


    public Attachment(int id_attachment, String ds_local_path, String ds_server_path, String ds_type, String nameFile, int id_attachment_srv) {
        this.id_attachment = id_attachment;
        this.ds_local_path = ds_local_path;
        this.ds_server_path = ds_server_path;
        this.ds_type = ds_type;
        this.nameFile = nameFile;
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

    public String getLocalPath() {
        return ds_local_path;
    }

    public void setLocalPath(String ds_local_path) {
        this.ds_local_path = ds_local_path;
    }

    public String getServerPath() {
        return ds_server_path;
    }

    public void setServerPath(String ds_server_path) {
        this.ds_server_path = ds_server_path;
    }

    public String getType() {
        return ds_type;
    }

    public void setType(String ds_type) {
        this.ds_type = ds_type;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public int getidAttachmentSrv() {
        return id_attachment_srv;
    }

    public void setIdAttachmentSrv(int id_attachment_srv) {
        this.id_attachment_srv = id_attachment_srv;
    }
}
