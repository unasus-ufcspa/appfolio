package com.ufcspa.unasus.appportfolio.WebClient;

/**
 * Created by icaromsc on 22/04/2016.
 * Classe resposável por armazenar temporariamente tuplas de ids de objetos recebidos via json
 */
public class HolderIDS {
    protected int id;
    protected int idSrv;
    protected String date;
    protected int idcvSrv;

    public int getIdcvSrv() {
        return idcvSrv;
    }

    public void setIdcvSrv(int idcvSrv) {
        this.idcvSrv = idcvSrv;
    }

    public HolderIDS(int id, int idSrv) {
        this.id = id;
        this.idSrv = idSrv;
        this.date = null;
    }

    public HolderIDS(int id, int idSrv, String date) {
        this.id = id;
        this.idSrv = idSrv;
        this.date = date;
    }

    public HolderIDS() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdSrv() {
        return idSrv;
    }

    public void setIdSrv(int idSrv) {
        this.idSrv = idSrv;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void clear(){
        this.id=-1;
        this.idSrv=-1;
    }


    @Override
    public String toString() {
        return "HolderIDS{" +
                "id=" + id +
                ", idSrv=" + idSrv +
                ", date='" + date + '\'' +
                '}';
    }
}
