package com.ufcspa.unasus.appportfolio.Model;

/**
 * Created by Desenvolvimento on 17/11/2015.
 */
public class PortfolioClass {
    private int idPortfolio;
    private String classCode;
    private String studentName;

    public PortfolioClass(int idPortfolio, String classCode, String studentName) {
        this.idPortfolio = idPortfolio;
        this.classCode = classCode;
        this.studentName = studentName;
    }

    @Override
    public String toString() {
        return "PortfolioClass{" +
                "idPortfolio=" + idPortfolio +
                ", classCode='" + classCode + '\'' +
                ", studentName='" + studentName + '\'' +
                '}';
    }

    public int getIdPortfolio() {
        return idPortfolio;
    }

    public void setIdPortfolio(int idPortfolio) {
        this.idPortfolio = idPortfolio;
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
