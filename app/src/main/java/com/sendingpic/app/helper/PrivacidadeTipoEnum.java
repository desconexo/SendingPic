package com.sendingpic.app.helper;

public enum PrivacidadeTipoEnum {
    PUBLICO("Publico"),
    ASSINANTES("Assinantes"),
    PRIVADO("Privado");

    private String descricao;

    PrivacidadeTipoEnum (String descricao){
        this.descricao = descricao;
    }

    public String getDescricao(){
        return descricao;
    }
}
