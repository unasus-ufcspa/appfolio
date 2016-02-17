package com.ufcspa.unasus.appportfolio.Model;

/**
 * Created by icaromsc on 17/02/2016.
 */
public class SpecificComment {
    public boolean orientation; // true to left, false to right
    public String comment;
    public String reference;


    public SpecificComment(boolean orientation, String comment,String reference) {
        super();
        this.orientation = orientation;
        this.comment = comment;
    }

}