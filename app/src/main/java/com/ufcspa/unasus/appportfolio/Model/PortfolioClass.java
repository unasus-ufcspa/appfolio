package com.ufcspa.unasus.appportfolio.Model;

/**
 * Created by Desenvolvimento on 17/11/2015.
 */
public class PortfolioClass {
    private int idPortfolioStudent;
    private String classCode;
    private String studentName;

    public PortfolioClass(int idPortfolioStudent, String classCode, String studentName) {
        this.idPortfolioStudent = idPortfolioStudent;
        this.classCode = classCode;
        this.studentName = studentName;
    }


    public int getIdPortfolioStudent() {
        return idPortfolioStudent;
    }

    public void setIdPortfolioStudent(int idPortfolioStudent) {
        this.idPortfolioStudent = idPortfolioStudent;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
