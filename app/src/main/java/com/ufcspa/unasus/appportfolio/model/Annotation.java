package com.ufcspa.unasus.appportfolio.model;

/**
 * Created by Steffano on 08/05/2017.
 */

public class Annotation {
    private int idAnnotation;
    private int idUser;
    private String dsAnnotation;
    private int idAnnotationSrv;

    public Annotation() {
    }

    public Annotation(int idUser, String dsAnnotation, int idAnnotationSrv) {
        this.idUser = idUser;
        this.dsAnnotation = dsAnnotation;
        this.idAnnotationSrv = idAnnotationSrv;
    }

    public Annotation(int idAnnotation, int idUser, String dsAnnotation, int idAnnotationSrv) {
        this.idAnnotation = idAnnotation;
        this.idUser = idUser;
        this.dsAnnotation = dsAnnotation;
        this.idAnnotationSrv = idAnnotationSrv;
    }

    public int getIdAnnotation() {
        return idAnnotation;
    }

    public void setIdAnnotation(int idAnnotation) {
        this.idAnnotation = idAnnotation;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getDsAnnotation() {
        return dsAnnotation;
    }

    public void setDsAnnotation(String dsAnnotation) {
        this.dsAnnotation = dsAnnotation;
    }

    public int getIdAnnotationSrv() {
        return idAnnotationSrv;
    }

    public void setIdAnnotationSrv(int idAnnotationSrv) {
        this.idAnnotationSrv = idAnnotationSrv;
    }
}
