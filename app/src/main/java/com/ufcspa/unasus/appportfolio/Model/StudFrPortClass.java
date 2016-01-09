package com.ufcspa.unasus.appportfolio.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Desenvolvimento on 07/01/2016.
 * objeto que representa um aluno para popular a tela de alunos e atividades
 */


public class StudFrPortClass {
    private String nameStudent;
    private List<Activity> listActivities;

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

    public StudFrPortClass() {
        listActivities= new ArrayList<Activity>();
    }


    public void add(Activity a){
        listActivities.add(a);
    }

    @Override
    public String toString() {
        return "StudFrPortClass{" +
                "nameStudent='" + nameStudent + '\'' +
                ", listActivities=" + listActivities +
                '}';
    }
}
