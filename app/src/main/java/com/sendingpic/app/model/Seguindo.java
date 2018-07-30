package com.sendingpic.app.model;

import com.google.firebase.database.DatabaseReference;
import com.sendingpic.app.config.ConfiguracaoFirebase;

public class Seguindo {

    private String idConta;
    private String idContaSeguidor;
    private String data;

    public void salvar(){
        salvarSeguindo();
        salvarSeguidor();
    }

    public void salvarSeguidor(){
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebase().child("seguidor");
        databaseReference.child(getIdContaSeguidor()).child(getIdConta()).setValue(this);
    }

    public void salvarSeguindo(){
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebase().child("seguindo");
        databaseReference.child(getIdConta()).child(getIdContaSeguidor()).setValue(this);
    }

    public String getIdConta() {
        return idConta;
    }

    public void setIdConta(String idConta) {
        this.idConta = idConta;
    }

    public String getIdContaSeguidor() {
        return idContaSeguidor;
    }

    public void setIdContaSeguidor(String idContaSeguidor) {
        this.idContaSeguidor = idContaSeguidor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
