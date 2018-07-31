package com.sendingpic.app.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sendingpic.app.R;
import com.sendingpic.app.config.ConfiguracaoFirebase;
import com.sendingpic.app.model.Usuario;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    private CircleImageView image_profile;
    private TextView profile_username;
    private TextView tv_follow;
    private TextView tv_report;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    public PerfilFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        mDatabase = ConfiguracaoFirebase.getFirebase();
        mAuth = ConfiguracaoFirebase.getFirebaseAuth();

        image_profile = view.findViewById(R.id.image_profile);
        profile_username = view.findViewById(R.id.profile_username);
        tv_follow = view.findViewById(R.id.tv_follow);
        tv_report = view.findViewById(R.id.tv_report);

        DatabaseReference dadosUsuario = mDatabase.child("usuarios").child(mAuth.getUid());

        dadosUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    Picasso.with(getActivity()).load(usuario.getImageUrl()).into(image_profile);
                    profile_username.setText(usuario.getUsuario());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference seguidoresUsuario = mDatabase.child("seguindo").child(mAuth.getUid());
        seguidoresUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int seguidores = (int) dataSnapshot.getChildrenCount();
                String seguidor = "SEGUIDOR";

                switch (seguidores){
                    case 0:
                        seguidor = "SEGUIDOR";
                        break;
                    case 1:
                        seguidor = "SEGUIDOR";
                        break;
                    default:
                        seguidor = "SEGUIDORES";
                }

                tv_follow.setText(seguidores + " " + seguidor);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

}
