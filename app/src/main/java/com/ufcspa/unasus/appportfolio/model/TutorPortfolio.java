package com.ufcspa.unasus.appportfolio.model;

/**
 * Created by icaromsc on 04/04/2016.
 */
public class TutorPortfolio {
    private int id_tutor_portfolio;
    private int id_portfolio_student;
    private int id_tutor;

    public TutorPortfolio() {
    }

    public TutorPortfolio(int id_tutor_portfolio, int id_portfolio_student, int id_tutor) {
        this.id_tutor_portfolio = id_tutor_portfolio;
        this.id_portfolio_student = id_portfolio_student;
        this.id_tutor = id_tutor;
    }

    public int getId_tutor_portfolio() {
        return id_tutor_portfolio;
    }

    public void setId_tutor_portfolio(int id_tutor_portfolio) {
        this.id_tutor_portfolio = id_tutor_portfolio;
    }

    public int getId_portfolio_student() {
        return id_portfolio_student;
    }

    public void setId_portfolio_student(int id_portfolio_student) {
        this.id_portfolio_student = id_portfolio_student;
    }
    public int getId_tutor() {
        return id_tutor;
    }

    public void setId_tutor(int id_tutor) {
        this.id_tutor = id_tutor;
    }
    }