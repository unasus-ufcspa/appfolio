package com.ufcspa.unasus.appportfolio.WebClient;

/**
 * Created by icaromsc on 22/04/2016.
 * Classe resposável por armazenar temporariamente tuplas de ids de objetos recebidos via json
 */
public class HolderIDS {
    protected int id;
    protected int idSrv;


    public HolderIDS(int id, int idSrv) {
        this.id = id;
        this.idSrv = idSrv;
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


    public void clear(){
        this.id=-1;
        this.idSrv=-1;
    }
}
