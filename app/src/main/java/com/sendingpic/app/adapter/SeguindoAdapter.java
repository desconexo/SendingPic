package com.sendingpic.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sendingpic.app.R;
import com.sendingpic.app.config.ConfiguracaoFirebase;
import com.sendingpic.app.model.Notificacao;
import com.sendingpic.app.model.Seguindo;
import com.sendingpic.app.model.Usuario;

import java.util.ArrayList;

public class SeguindoAdapter extends ArrayAdapter<Seguindo> {

    private ArrayList<Seguindo> seguidores;
    private ArrayList<Usuario> usuarios;
    private ArrayAdapter adapter;
    private Context context;
    private TextView tv_no_followers;
    private TextView username;

    private DatabaseReference mDatabase;
    private ValueEventListener valueEventListener;

    public SeguindoAdapter(Context context, ArrayList<Seguindo> objects, TextView tv){
        super(context, 0, objects);
        this.seguidores = objects;
        this.context = context;
        this.tv_no_followers = tv;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if (seguidores != null){
            int visible = 100;
            tv_no_followers.setVisibility(visible);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.perfil_seguido, parent, false);
            username = view.findViewById(R.id.username_profile);

            Seguindo seguindo = seguidores.get(position);

            mDatabase = ConfiguracaoFirebase.getFirebase();
            DatabaseReference pesquisa = mDatabase.child("usuarios").child(seguindo.getIdConta());

            pesquisa.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        Usuario usuarioRecebido = dataSnapshot.getValue(Usuario.class);
                        username.setText(usuarioRecebido.getUsuario());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else{
            int visible = 0;
            tv_no_followers.setVisibility(visible);
        }

        return view;
    }
}
