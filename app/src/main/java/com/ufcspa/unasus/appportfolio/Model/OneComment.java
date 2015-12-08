package com.ufcspa.unasus.appportfolio.Model;

public class OneComment {
    public boolean orientation; // true to left, false to right
    public String comment;

    public OneComment(boolean orientation, String comment) {
        super();
        this.orientation = orientation;
        this.comment = comment;
    }

}