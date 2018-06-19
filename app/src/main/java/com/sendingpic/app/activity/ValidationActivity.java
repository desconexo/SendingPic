package com.sendingpic.app.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jgabrielfreitas.core.BlurImageView;
import com.sendingpic.app.R;
import com.sendingpic.app.config.ConfiguracaoFirebase;
import com.sendingpic.app.model.Usuario;

public class ValidationActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ValueEventListener mListener;

    BlurImageView blurImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);

        blurImageView = (BlurImageView) findViewById(R.id.blurImage);
        blurImageView.setBlur(10);

        mAuth = ConfiguracaoFirebase.getFirebaseAuth();

        mDatabase = ConfiguracaoFirebase.getFirebase().child("usuarios").child(mAuth.getUid());

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(ValidationActivity.this, MainActivity.class));
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = new Usuario();
                try {
                    usuario = dataSnapshot.getValue(Usuario.class);
                    try {
                        if (!usuario.getEmail().equals(null)) {
                            startActivity(new Intent(ValidationActivity.this, StarterActivity.class));
                            //DEU, MANDAR PRA PÁGINA QUE PEDE O CÓDIGO DE SEGURANÇA
                        }
                    }catch (NullPointerException e){
                        startActivity(new Intent(ValidationActivity.this, RegisterActivity.class));
                    }
                }catch (NullPointerException e){
                    startActivity(new Intent(ValidationActivity.this, RegisterActivity.class));
                }
                usuario = null;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addValueEventListener(valueEventListener);
        mListener = valueEventListener;
    }
}
