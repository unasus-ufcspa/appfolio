package com.ufcspa.unasus.appportfolio.Model;

/**
 * Created by Desenvolvimento on 16/11/2015.
 */
public class Singleton {
    private static Singleton ourInstance = new Singleton();

    public static Singleton getInstance() {
        return ourInstance;
    }

    private Singleton() {}

    public User user = new User(0,null,null);
    public Team team = new Team(0,0,null,null,'Z',null,null) ;
    public PortfolioClass portfolioClass = null;//new PortfolioClass(1,"212","artur","ARRAY");
    public Activity activity = new Activity(1,1,1,"Atividade final","legal");

    public int idActivityStudent = -1;
}
