package com.ufcspa.unasus.appportfolio.Model;

public class OneComment {
    public boolean orientation; // true to left, false to right
    public String comment;
    public String hour;
    public String date;


    public OneComment(boolean orientation, String comment) {
        super();
        this.orientation = orientation;
        this.comment = comment;
    }
    public OneComment(boolean orientation, String comment,String h) {
        super();
        this.orientation = orientation;
        this.comment = comment;
        this.hour=h;
    }

    public OneComment(boolean orientation, String comment,String h,String dt) {
        super();
        this.orientation = orientation;
        this.comment = comment;
        this.hour=h;
        this.date=dt;
    }

}