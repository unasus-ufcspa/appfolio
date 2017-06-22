package com.ufcspa.unasus.appportfolio.Model;

public class OneComment {
    public boolean orientation; // true to left, false to right
    public String comment;
    public String hour;
    public String date;
    public boolean atach;
    public String  path;
    public int idAttach;
    public int idAuthor;


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
    public OneComment(boolean orientation, String comment,String h,String dt,boolean attach) {
        super();
        this.orientation = orientation;
        this.comment = comment;
        this.hour=h;
        this.date=dt;
        this.atach=attach;
    }

    public OneComment(boolean orientation, String comment, String hour, String date, int idAttach, int idAuthor) {
        this.orientation = orientation;
        this.comment = comment;
        this.hour = hour;
        this.date = date;
        this.idAttach = idAttach;
        this.idAuthor = idAuthor;
    }

    @Override
    public String toString() {
        return "OneComment{" +
                "comment='" + comment + '\'' +
                ", hour='" + hour + '\'' +
                ", date='" + date + '\'' +
                ", atach='" + atach + '\'' +
                ", author'" + idAuthor +
                '}';
    }
}