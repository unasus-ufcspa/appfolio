package com.ufcspa.unasus.appportfolio.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Desenvolvimento on 08/12/2015.
 */
public class Comentario {
    String txtComment;
    String txtReference;
    String typeComment;
    String dateComment;
    private String dateSend;
    private int idComment;
    private int id_comment_version;

    public int getId_comment_version_srv() {
        return id_comment_version_srv;
    }

    public void setId_comment_version_srv(int id_comment_version_srv) {
        this.id_comment_version_srv = id_comment_version_srv;
    }

    private int id_comment_version_srv;
    private int idActivityStudent;
    private int idAuthor;
    private int idNote;
    private int idAttach;
    private int idCommentSrv;

    public int getId_comment_version() {
        return id_comment_version;
    }

    public void setId_comment_version(int id_comment_version) {
        this.id_comment_version = id_comment_version;
    }

    public int getIdAttach() {
        return idAttach;
    }

    public void setIdAttach(int idAttach) {
        this.idAttach = idAttach;
    }

    public int getIdNote() {
        return idNote;
    }

    public void setIdNote(int idNote) {
        this.idNote = idNote;
    }

    public String getTxtReference() {
        return txtReference;
    }

    public void setTxtReference(String txtReference) {
        this.txtReference = txtReference;
    }

    public int getIdComment() {
        return idComment;
    }

    public void setIdComment(int idComment) {
        this.idComment = idComment;
    }

    public int getIdActivityStudent() {
        return idActivityStudent;
    }

    public void setIdActivityStudent(int idActivityStudent) {
        this.idActivityStudent = idActivityStudent;
    }

    public int getIdAuthor() {
        return idAuthor;
    }

    public void setIdAuthor(int idAuthor) {
        this.idAuthor = idAuthor;
    }

    public String getTxtComment() {
        return txtComment;
    }

    public void setTxtComment(String txtComment) {
        this.txtComment = txtComment;
    }

    public String getTypeComment() {
        return typeComment;
    }

    public void setTypeComment(String typeComment) {
        this.typeComment = typeComment;
    }

    public String getDateComment() {
        return dateComment;
    }

    public void setDateComment(String dateComment) {
        this.dateComment = dateComment;
    }

    public String getDateSend() {
        return dateSend;
    }

    public void setDateSend(String dateSend) {
        this.dateSend = dateSend;
    }

    public int getIdCommentSrv() {
        return idCommentSrv;
    }

    public void setIdCommentSrv(int idCommentSrv) {
        this.idCommentSrv = idCommentSrv;
    }

    @Override
    public String toString() {
        return "Comentario{" +
                "txtComment='" + txtComment + '\'' +
                ", dateComment='" + dateComment + '\'' +
                ", dateSend='" + dateSend + '\'' +
                ", idComment=" + idComment +
                ", id_comment_version=" + id_comment_version +
                ", idActivityStudent=" + idActivityStudent +
                ", idAuthor=" + idAuthor +
                ", idNote=" + idNote +
                ", idAttach=" + idAttach +
                ", idCommentSrv=" + idCommentSrv +
                '}';
    }

    public JSONObject toJSON(){
        JSONObject c= new JSONObject();// c is comment JSON OBJECT
        try {
            //c.put("id_comment",+getIdComment());
            c.put("id_activity_student",getIdActivityStudent());
            c.put("id_author",getIdAuthor());
            c.put("tx_comment",getTxtComment());
            c.put("dt_comment",getDateComment());
            c.put("tx_reference",getTxtReference());
            c.put("tp_comment",getTypeComment());
            c.put("nu_comment_activity",getIdNote());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return c;
    }




}
