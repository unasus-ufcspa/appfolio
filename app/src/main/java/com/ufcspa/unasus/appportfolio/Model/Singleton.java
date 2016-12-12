package com.ufcspa.unasus.appportfolio.Model;

/**
 * Created by Desenvolvimento on 16/11/2015.
 */
public class Singleton {
    private static Singleton ourInstance = new Singleton();
    public User user = new User(0,null,null);
    public Team team = new Team(0,0,null,null,'Z',null,null) ;
    public PortfolioClass portfolioClass = null;//new PortfolioClass(1,"212","artur","ARRAY");
    public Activity activity = new Activity(1,1,1,"Atividade final","legal");
    public Device device = new Device();
    public String selectedText = "null";
    public String activityText = "";
    public Note note = new Note(0,"null",0);
    public int idActivityStudent = -1;
    public int idVersionActivity = -1;
    public int idCurrentVersionActivity = -1;
    public int lastIdAttach = -1;
    public int idStudent = -1;
    public boolean firsttime = false;
    public boolean isFullscreen = false;
    public boolean wasFullscreen = false;
    public boolean isFirstSpecificComment = false;
    public boolean firstSync = false;
    public boolean tutorial = false;

    public Observation actualObservation= new Observation();

    public String erro = "Erro interno. Por favor tente novamente";


    private Singleton() {
    }

    public static Singleton getInstance() {
        if (ourInstance == null) ourInstance = new Singleton();
        return ourInstance;
    }

    public static Singleton init(){
        ourInstance = new Singleton();
        return ourInstance;
    }
}
