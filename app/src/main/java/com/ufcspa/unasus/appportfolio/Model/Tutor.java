package com.ufcspa.unasus.appportfolio.Model;

/**
 * Created by Desenvolvimento on 13/11/2015.
 */
public class Tutor extends User {
    public Tutor(Integer idUser, String name, String idCode, String email, String cellphone) {
        super(idUser, name, idCode, email, cellphone);
        setUserType('T');
    }


}
