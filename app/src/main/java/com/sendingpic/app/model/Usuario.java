package com.sendingpic.app.model;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.sendingpic.app.config.ConfiguracaoFirebase;

/**
 * Created by feeri on 11/03/2018.
 */

public class Usuario {
    private String id;
    private String usuario;
    private String email;
    private String secretPass;
    private String imageUrl;
    private Uri localImageUri;

    public Usuario(){

    }

    public void salvar(){
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebase();
        databaseReference.child("usuarios").child(getId()).setValue(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSecretPass() {
        return secretPass;
    }

    public void setSecretPass(String secretPass) {
        this.secretPass = secretPass;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Exclude
    public Uri getLocalImageUri() {
        return localImageUri;
    }

    public void setLocalImageUri(Uri localImageUri) {
        this.localImageUri = localImageUri;
    }
}
