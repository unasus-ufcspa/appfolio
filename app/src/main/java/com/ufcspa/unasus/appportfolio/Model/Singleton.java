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

    public User user= new User(0,null,null);
    public Team team= null;
    public PortfolioClass portfolioClass = null;
}
