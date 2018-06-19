package com.sendingpic.app.model;

import com.google.firebase.database.DatabaseReference;
import com.sendingpic.app.config.ConfiguracaoFirebase;

public class Notificacao {
    private Post post;
    private String userId;

    public Notificacao(){

    }

    public void salvar(){
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebase().child("notificacoes");
        DatabaseReference newNotification = databaseReference.push();
        databaseReference.child(post.getUsuarioId()).child(newNotification.getKey().toString()).setValue(this);
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
