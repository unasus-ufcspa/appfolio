package com.ufcspa.unasus.appportfolio.Model;

/**
 * Created by icaromsc on 28/03/2016.
 */
public class Device {
    private String _id_device;
    private int _id_user;
    private int _tp_device;

    public Device(){}

    public Device(String _id_device, int _id_user, int _tp_device) {
        this._id_device = _id_device;
        this._id_user = _id_user;
        this._tp_device = _tp_device;
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

    @Override
    public String toString() {
        return "Device{" +
                "_id_device='" + _id_device + '\'' +
                ", _id_user=" + _id_user +
                ", _tp_device=" + _tp_device +
                '}';
    }




}
