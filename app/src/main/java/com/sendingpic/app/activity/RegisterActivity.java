package com.sendingpic.app.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jgabrielfreitas.core.BlurImageView;
import com.sendingpic.app.R;
import com.sendingpic.app.config.ConfiguracaoFirebase;
import com.sendingpic.app.helper.Base64Custom;
import com.sendingpic.app.model.Notificacao;
import com.sendingpic.app.model.Post;
import com.sendingpic.app.model.Usuario;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    BlurImageView blurImageView;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private StorageReference mStorage;

    private EditText edtApelido;
    private EditText edtEmail;
    private EditText edtSecretPass;
    private Button btnCancelRegister;
    private Button btnFinishRegister;
    private CircleImageView profileImage;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        blurImageView = findViewById(R.id.blurImage);
        blurImageView.setBlur(10);

        edtApelido = findViewById(R.id.edtApelido);
        edtEmail = findViewById(R.id.edtEmail);
        edtSecretPass = findViewById(R.id.edtSecretPass);
        btnCancelRegister = findViewById(R.id.btnCancelRegister);
        btnFinishRegister = findViewById(R.id.btnFinishRegister);
        profileImage = findViewById(R.id.profileImage);
        progressBar = findViewById(R.id.progressbar_register);

        mAuth = ConfiguracaoFirebase.getFirebaseAuth();
        mStorage = FirebaseStorage.getInstance().getReference();

        String userId = mAuth.getUid();

        mDatabase = ConfiguracaoFirebase.getFirebase().child("usuarios").child(userId);

        edtApelido.setText(mAuth.getCurrentUser().getDisplayName().toString());

        edtEmail.setEnabled(false);
        edtEmail.setText(mAuth.getCurrentUser().getEmail().toString());

        Picasso.with(this).load(mAuth.getCurrentUser().getPhotoUrl().toString()).into(profileImage);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                }
            }
        };

        btnCancelRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
            }
        });

        btnFinishRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtApelido.getText().toString().equals("") || edtApelido.getText().toString().equals(null)){
                    Toast.makeText(RegisterActivity.this, "O apelido não pode ser em braco!", Toast.LENGTH_SHORT).show();
                }else if(edtSecretPass.getText().toString().equals("") || edtSecretPass.getText().toString().equals(null)){
                    Toast.makeText(RegisterActivity.this, "O dígito secreto não pode ser em braco!", Toast.LENGTH_SHORT).show();
                }else if(edtSecretPass.getText().toString().length() != 4){
                    Toast.makeText(RegisterActivity.this, "O dígito secreto deve ter 4 caracteres!", Toast.LENGTH_SHORT).show();
                }else{
                    progressBar.setIndeterminate(true);



                    Usuario usuario = new Usuario();
                    usuario.setId(mAuth.getUid());
                    usuario.setEmail(edtEmail.getText().toString());
                    usuario.setUsuario(edtApelido.getText().toString());
                    usuario.setSecretPass(Base64Custom.codificar(edtSecretPass.getText().toString()));
                    usuario.setImageUrl(mAuth.getCurrentUser().getPhotoUrl().toString());
                    usuario.salvar();
                    startActivity(new Intent(RegisterActivity.this, StarterActivity.class));
                    finish();
                }

            }
        });
    }

    /*
    private void enviaFoto(final Usuario user){
        StorageReference storageReference = mStorage.child("profile_images").child(user.getId()).child(user.getLocalImageUri().getLastPathSegment());
        storageReference.putFile(user.getLocalImageUri()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                user.setImageUrl(downloadUrl.toString());
                user.salvar();
                startActivity(new Intent(RegisterActivity.this, StarterActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setIndeterminate(false);
                Toast.makeText(RegisterActivity.this, "Falha ao cadastrar.", Toast.LENGTH_LONG).show();
                mAuth.signOut();
            }
        });

    }
    */

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }
}
