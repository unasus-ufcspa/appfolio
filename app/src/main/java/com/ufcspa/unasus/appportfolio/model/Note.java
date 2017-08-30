package com.ufcspa.unasus.appportfolio.model;

import java.io.Serializable;

/**
 * Created by icaromsc on 25/01/2016.
 */
public class Note implements Serializable, Comparable<Note> {
    private int btId;
    private String selectedText;
    private float btY;

    public Note(int btId, String selectedText, float btY) {
        this.btId = btId;
        this.selectedText = selectedText;
        this.btY = btY;
    }

    public int getBtId() {
        return btId;
    }

    public void setBtId(int btId) {
        this.btId = btId;
    }

    public String getSelectedText() {
        return selectedText;
    }

    public void setSelectedText(String selectedText) {
        this.selectedText = selectedText;
    }

    public float getBtY() {
        return btY;
    }

    public void setBtY(float btY) {
        this.btY = btY;
    }


    @Override
    public int compareTo(Note another) {
        if (this.btY < another.btY)
            return -1;
        if (this.btY > another.btY)
            return 1;
        return 0;
    }
}
