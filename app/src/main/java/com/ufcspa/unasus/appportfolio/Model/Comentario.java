package com.ufcspa.unasus.appportfolio.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Desenvolvimento on 08/12/2015.
 */
public class Comentario {
    private int idComment;
    private int idActivityStudent;
    private int idAuthor;
    String txtComment;
    String txtReference;
    String typeComment;
    String dateComment;

    public void setIdAuthor(int idAuthor) {
        this.idAuthor = idAuthor;
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

    @Override
    public String toString() {
        return "Comentario{" +
                "txtComment='" + txtComment + '\'' +
                ", txtReference='" + txtReference + '\'' +
                ", typeComment='" + typeComment + '\'' +
                ", dateComment='" + dateComment + '\'' +
                ", idAuthor=" + idAuthor +
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return c;
    }




}
