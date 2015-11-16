package com.ufcspa.unasus.appportfolio.Model;

/**
 * Created by Desenvolvimento on 16/11/2015.
 */
public class SingletonUser {
    private static SingletonUser ourInstance = new SingletonUser();

    public static SingletonUser getInstance() {
        return ourInstance;
    }

    public User user= new User(0,null,null);
    private SingletonUser() {
    }


}
