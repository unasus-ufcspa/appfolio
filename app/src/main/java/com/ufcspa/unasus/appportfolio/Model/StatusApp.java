package com.ufcspa.unasus.appportfolio.Model;

/**
 * Created by icaromsc on 12/04/2016.
 */
public class StatusApp {
    private int id;
    private boolean first_sync;
    private boolean basic_data_sync;

    public StatusApp(boolean first_sync, boolean basic_data_sync) {
        this.first_sync = first_sync;
        this.basic_data_sync = basic_data_sync;
    }
    public StatusApp(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFirst_sync() {
        return first_sync;
    }

    public void setFirst_sync(boolean first_sync) {
        this.first_sync = first_sync;
    }

    public boolean isBasic_data_sync() {
        return basic_data_sync;
    }

    public void setBasic_data_sync(boolean basic_data_sync) {
        this.basic_data_sync = basic_data_sync;
    }
}
