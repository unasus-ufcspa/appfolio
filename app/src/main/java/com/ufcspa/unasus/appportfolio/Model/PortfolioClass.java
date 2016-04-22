package com.ufcspa.unasus.appportfolio.Model;

/**
 * Created by Desenvolvimento on 17/11/2015.
 */
public class PortfolioClass implements Comparable<PortfolioClass> {
    private int idPortClass;
    private int idPortfolioStudent;
    private String classCode;
    private String studentName;
    private String portfolioTitle;
    private String perfil;

    public PortfolioClass() {
    }

    public PortfolioClass(String classCode, int idPortClass, String perfil, String portfolioTitle) {
        this.classCode = classCode;
        this.idPortClass = idPortClass;
        this.perfil = perfil;
        this.portfolioTitle = portfolioTitle;
    }

    public PortfolioClass(int idPortfolioStudent, String classCode, String studentName, String portfolioTitle) {
        this.idPortfolioStudent = idPortfolioStudent;
        this.classCode = classCode;
        this.studentName = studentName;
        this.portfolioTitle = portfolioTitle;
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

    public String getPortfolioTitle() {
        return portfolioTitle;
    }

    public void setPortfolioTitle(String portfolioTitle) {
        this.portfolioTitle = portfolioTitle;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public int getIdPortClass() {
        return idPortClass;
    }

    public void setIdPortClass(int idPortClass) {
        this.idPortClass = idPortClass;
    }

    @Override
    public String toString() {
        return "PortfolioClass{" +
                "idPortStudent=" + idPortfolioStudent +
                ", idPortClass=" + idPortClass +
                ", classCode='" + classCode + '\'' +
                ", portfolioTitle='" + portfolioTitle + '\'' +
                ", perfil='" + perfil + '\'' +
                '}';
    }

    @Override
    public int compareTo(PortfolioClass another) {
        int thisClassCode;
        int anotherClassCode;

        try {
            thisClassCode = Integer.valueOf(this.classCode.substring(1));
            anotherClassCode = Integer.valueOf(another.getClassCode().substring(1));
        } catch (Exception e) {
            return 0;
        }

        if (thisClassCode < anotherClassCode) return -1;
        if (thisClassCode > anotherClassCode) return 1;

        return 0;
    }
}
