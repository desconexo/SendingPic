package com.sendingpic.app.model;

import android.net.Uri;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.sendingpic.app.config.ConfiguracaoFirebase;
import com.sendingpic.app.helper.Base64Custom;
import com.sendingpic.app.helper.PrivacidadeTipoEnum;

public class Post {

    private String idPost;
    private String usuarioId;
    private String dataPublicacao;
    private String descricao;
    private String imageUrl;
    private Uri localImage;
    private PrivacidadeTipoEnum privacidadeTipo;

    public Post(){

    }

    public void salvar(){
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebase().child("posts").child(getUsuarioId());
        DatabaseReference theNewPostId = databaseReference.push();

        String postId = theNewPostId.getKey().toString();
        this.setIdPost(postId);

        databaseReference.child(getIdPost()).setValue(this);
    }

    @Exclude
    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    @Exclude
    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(String dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Exclude
    public Uri getLocalImage() {
        return localImage;
    }

    public void setLocalImage(Uri localImage) {
        this.localImage = localImage;
    }

    public PrivacidadeTipoEnum getPrivacidadeTipo() {
        return privacidadeTipo;
    }

    public void setPrivacidadeTipo(PrivacidadeTipoEnum privacidadeTipo) {
        this.privacidadeTipo = privacidadeTipo;
    }
}
