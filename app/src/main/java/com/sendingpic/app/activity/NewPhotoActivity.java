package com.sendingpic.app.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jgabrielfreitas.core.BlurImageView;
import com.sendingpic.app.R;
import com.sendingpic.app.config.ConfiguracaoFirebase;
import com.sendingpic.app.helper.PrivacidadeTipoEnum;
import com.sendingpic.app.model.Notificacao;
import com.sendingpic.app.model.Post;

import java.io.IOException;

public class NewPhotoActivity extends AppCompatActivity {

    private BlurImageView blurImageView;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;

    private EditText newPostLegenda;
    private ImageButton newPostFoto;
    private ProgressBar progressBar;

    private PopupMenu popupMenu;
    private MenuView.ItemView privacidade;
    private View privacidadeView;

    private Toolbar toolbar;

    private Boolean salvandoFoto = false;

    private Post newPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_photo);

        blurImageView = findViewById(R.id.blurImage);
        blurImageView.setBlur(10);

        newPost = new Post();
        newPost.setPrivacidadeTipo(PrivacidadeTipoEnum.PUBLICO);

        mAuth = ConfiguracaoFirebase.getFirebaseAuth();
        String userId = mAuth.getUid();
        mStorage = FirebaseStorage.getInstance().getReference();

        newPostLegenda = findViewById(R.id.new_post_legenda);
        newPostFoto = findViewById(R.id.new_post_foto);
        progressBar = findViewById(R.id.progressBar);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        newPostFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compartilharFoto();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        privacidade = findViewById(R.id.item_privacidade);
        privacidadeView = findViewById(R.id.item_privacidade);

        popupMenu = new PopupMenu(NewPhotoActivity.this, privacidadeView);
        popupMenu.inflate(R.menu.menu_privacy_popup);



        switch (item.getItemId()) {
            case R.id.item_cancelar:
                if(!salvandoFoto){
                    popupMenu.dismiss();
                    finish();
                }
                return true;
            case R.id.item_privacidade:
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (salvandoFoto){
                            return false;
                        }
                        privacidade.setIcon(menuItem.getIcon());
                        if(menuItem.getItemId() == R.id.item_privacidade_publico){
                            newPost.setPrivacidadeTipo(PrivacidadeTipoEnum.PUBLICO);
                        }else if(menuItem.getItemId() == R.id.item_privacidade_assinantes){
                            newPost.setPrivacidadeTipo(PrivacidadeTipoEnum.ASSINANTES);
                        }else if(menuItem.getItemId() == R.id.item_privacidade_privado){
                            newPost.setPrivacidadeTipo(PrivacidadeTipoEnum.PRIVADO);
                        }
                        return true;
                    }
                });
                return true;
            case R.id.item_enviar:
                if(!salvandoFoto) enviarPost();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void enviarPost() {
        salvandoFoto = true;

        newPostLegenda.setEnabled(false);
        newPostFoto.setEnabled(false);

        if(newPost.getLocalImage() == null){

            Toast.makeText(NewPhotoActivity.this, "Você deve selecionar uma imagem.", Toast.LENGTH_LONG).show();

            salvandoFoto = false;
            newPostLegenda.setEnabled(true);
            newPostFoto.setEnabled(true);
        }else {
            progressBar.setIndeterminate(true);

            newPost.setUsuarioId(mAuth.getUid());
            newPost.setDescricao(newPostLegenda.getText().toString());
            newPost.setDataPublicacao("17/06/2018");

            enviaFoto(newPost);
        }
    }


    private void compartilharFoto() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    private void enviaFoto(final Post post){
        StorageReference storageReference = mStorage.child("images").child(post.getUsuarioId()).child(post.getLocalImage().getLastPathSegment());
        storageReference.putFile(post.getLocalImage()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                newPost.setImageUrl(downloadUrl.toString());
                newPost.salvar();
                Notificacao notificacao = new Notificacao();
                notificacao.setPost(newPost);
                notificacao.setUserId(newPost.getUsuarioId());
                notificacao.salvar();
                Toast.makeText(NewPhotoActivity.this, "Publicado na sua linha do tempo.", Toast.LENGTH_LONG).show();
                popupMenu.dismiss();
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null){
            Uri localImageSelected = data.getData();

            newPost.setLocalImage(localImageSelected);

            try {
                Bitmap image = MediaStore.Images.Media.getBitmap(getContentResolver(), localImageSelected);
                Drawable drawable = new BitmapDrawable(getResources(), image);

                newPostFoto.setBackground(drawable);

                int valor = getResources().getDisplayMetrics().widthPixels;

                int x = image.getWidth(); //x
                int y = image.getHeight(); //y

                /*
                * x - valor
                * y - z
                * */

                int resultadoAltura = (y*valor)/x;

                //Toast.makeText(NewPhotoActivity.this, x + ", " + y, Toast.LENGTH_LONG).show();

                newPostFoto.getLayoutParams().width = valor;
                newPostFoto.getLayoutParams().height = (y*valor)/x;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
