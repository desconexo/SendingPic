package com.sendingpic.app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jgabrielfreitas.core.BlurImageView;
import com.sendingpic.app.R;
import com.sendingpic.app.adapter.PostAdapter;
import com.sendingpic.app.config.ConfiguracaoFirebase;
import com.sendingpic.app.model.Post;
import com.sendingpic.app.model.Seguindo;
import com.sendingpic.app.model.Usuario;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private BlurImageView blurImageView;

    private ArrayAdapter adapter;
    private ArrayList<Post> posts;

    private ListView listView;
    private CircleImageView image_profile;
    private TextView profile_username;
    private TextView tv_follow;
    private TextView tv_report;

    private DatabaseReference mDatabase;
    private DatabaseReference buscaPosts;
    private FirebaseAuth mAuth;
    private ValueEventListener eventListenerPosts;

    @Override
    public void onStart() {
        super.onStart();
        buscaPosts.addValueEventListener(eventListenerPosts);
    }

    @Override
    public void onStop() {
        super.onStop();
        buscaPosts.removeEventListener(eventListenerPosts);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        blurImageView = findViewById(R.id.blurImage);
        blurImageView.setBlur(10);

        Bundle extras = getIntent().getExtras();

        mDatabase = ConfiguracaoFirebase.getFirebase();
        mAuth = ConfiguracaoFirebase.getFirebaseAuth();

        image_profile = findViewById(R.id.image_profile_p);
        profile_username = findViewById(R.id.profile_username_p);
        tv_follow = findViewById(R.id.tv_follow_p);
        tv_report = findViewById(R.id.tv_report_p);
        listView = findViewById(R.id.lv_posts);

        DatabaseReference dadosUsuario = mDatabase.child("usuarios").child(extras.getString("id"));

        dadosUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    Picasso.with(ProfileActivity.this).load(usuario.getImageUrl()).into(image_profile);
                    profile_username.setText(usuario.getUsuario());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference verifyFollow = mDatabase.child("seguindo").child(extras.getString("id")).child(mAuth.getUid());
        verifyFollow.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    tv_follow.setText(getResources().getString(R.string.following));
                }else {
                    tv_follow.setText(getResources().getString(R.string.follow));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        posts = new ArrayList<>();

        adapter = new PostAdapter(ProfileActivity.this, posts);
        listView.setAdapter(adapter);

        buscaPosts = mDatabase.child("posts").child(extras.getString("id"));

        eventListenerPosts = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                posts.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()){
                    Post post = dados.getValue(Post.class);
                    posts.add(post);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

    }
}
