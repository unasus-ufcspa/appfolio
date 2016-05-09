package com.ufcspa.unasus.appportfolio.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desenvolvimento on 07/01/2016.
 * objeto que representa um aluno para popular a tela de alunos e atividades
 */


public class StudFrPortClass {
    private String nameStudent;
    private Bitmap photo;
    private String photo64;
    private String cellphone;
    private List<Activity> listActivities;

    public StudFrPortClass() {
        listActivities = new ArrayList<Activity>();
    }

    public String getNameStudent() {
        return nameStudent;
    }

    public void setNameStudent(String nameStudent) {
        this.nameStudent = nameStudent;
    }

    public List<Activity> getListActivities() {
        return listActivities;
    }

    public void setListActivities(List<Activity> listActivities) {
        this.listActivities = listActivities;
    }

    public String getPhoto64() {
        return photo64;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        if (photo != null) {
            this.photo64 = photo;
            byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
            this.photo = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
    }

    public void add(Activity a){
        listActivities.add(a);
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    @Override
    public String toString() {
        return "StudFrPortClass{" +
                "nameStudent='" + nameStudent + '\'' +
                ", listActivities=" + listActivities +
                '}';
    }
}
