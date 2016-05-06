package com.ufcspa.unasus.appportfolio.Model.basicData;

/**
 * Created by icaromsc on 04/04/2016.
 */
public class User {
    private Integer idUser;
    private String nm_user;
    private String nu_identification;
    private String email;
    private String cellphone;
    private String photo;

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getNm_user() {
        return nm_user;
    }

    public void setNm_user(String nm_user) {
        this.nm_user = nm_user;
    }

    public String getNu_identification() {
        return nu_identification;
    }

    public void setNu_identification(String nu_identification) {
        this.nu_identification = nu_identification;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
