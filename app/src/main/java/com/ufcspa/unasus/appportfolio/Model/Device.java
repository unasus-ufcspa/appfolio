package com.ufcspa.unasus.appportfolio.Model;

/**
 * Created by icaromsc on 28/03/2016.
 */
public class Device {

    private String _id_device;
    private int _id_user;
    private int _tp_device;
    private String fl_first_login;
    private String fl_basic_data;
    private String fl_first_sync;

    public Device(){
        this._id_user=-1;
        //
    }



    public Device(String _id_device, int _id_user, int _tp_device, String fl_first_login, String fl_basic_data, String fl_first_sync) {
        this._id_device = _id_device;
        this._id_user = _id_user;
        this._tp_device = _tp_device;
        this.fl_first_login = fl_first_login;
        this.fl_basic_data = fl_basic_data;
        this.fl_first_sync = fl_first_sync;
    }


    public String get_id_device() {
        return _id_device;
    }

    public void set_id_device(String _id_device) {
        this._id_device = _id_device;
    }

    public int get_id_user() {
        return _id_user;
    }

    public void set_id_user(int _id_user) {
        this._id_user = _id_user;
    }

    public int get_tp_device() {
        return _tp_device;
    }

    public void set_tp_device(int _tp_device) {
        this._tp_device = _tp_device;
    }

    public String getFl_first_login() {
        return fl_first_login;
    }

    public String getFl_basic_data() {
        return fl_basic_data;
    }

    public String getFl_first_sync() {
        return fl_first_sync;
    }

    public void setFl_first_sync(String fl_first_sync) {
        this.fl_first_sync = fl_first_sync;
    }

    @Override
    public String toString() {
        return "Device{" +
                "_id_device='" + _id_device + '\'' +
                ", _id_user=" + _id_user +
                ", _tp_device=" + _tp_device +
                '}';
    }




}
