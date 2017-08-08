package com.ufcspa.unasus.appportfolio.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.ufcspa.unasus.appportfolio.Activities.Fragments.ConfigFragment;

/**
 * Created by Desenvolvimento on 13/11/2015.
 */
public class User {
    private Integer idUser;
    private String name;
    private String idCode;
    private Character userType;
    private String email;
    private String cellphone;
    private String photo;
    private String password;
    private Bitmap photoBitmap;

    public User(Integer idUser, String name, String idCode, String email, String cellphone) {
        this.idUser = idUser;
        this.name = name;
        this.idCode = idCode;
        this.email = email;
        this.cellphone = cellphone;
    }

    public User(Integer idUser, Character userType, String name) {
        this.idUser = idUser;
        this.userType = userType;
        this.name = name;
    }

    public User() {
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public Character getUserType() {
        return userType;
    }

    public void setUserType(Character userType) {
        this.userType = userType;
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

    public void setPhoto(String photo, Bitmap bitmap) {
        this.photo = photo;
        if (bitmap != null)
            this.photoBitmap = bitmap;
        else {
            if (photo != null) {
                byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
                this.photoBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                Bitmap picture = Bitmap.createScaledBitmap(photoBitmap, 180, 180, true);
                photoBitmap = ConfigFragment.getRoundedRectBitmap(picture, 100);

            }
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Bitmap getPhotoBitmap() {
        return photoBitmap;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", cellphone='" + cellphone + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
