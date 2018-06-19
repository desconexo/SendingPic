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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sendingpic.app.R;
import com.sendingpic.app.config.ConfiguracaoFirebase;
import com.sendingpic.app.model.Notificacao;
import com.sendingpic.app.model.Post;
import com.sendingpic.app.model.Usuario;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificacaoAdapter extends ArrayAdapter<Notificacao> {

    private ArrayList<Notificacao> notificacoes;
    private ArrayList<Usuario> usuarios;
    private ArrayAdapter adapter;
    private Context context;
    private TextView tv_notification;
    private TextView username;

    private Usuario usuario;

    private DatabaseReference mDatabase;
    private ValueEventListener valueEventListener;

    public NotificacaoAdapter(Context context, ArrayList<Notificacao> objects, TextView tv){
        super(context, 0, objects);
        this.notificacoes = objects;
        this.context = context;
        this.tv_notification = tv;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if (notificacoes != null){
            int visible = 100;
            tv_notification.setVisibility(visible);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.notification, parent, false);
            username = view.findViewById(R.id.username_notification);

            Notificacao notificacao = notificacoes.get(position);

            adapter = new UsuarioNotificacaoAdapter(context, notificacoes, usuarios, view);

            mDatabase = ConfiguracaoFirebase.getFirebase();
            DatabaseReference pesquisa = mDatabase.child("usuarios").child(notificacao.getUserId());

            usuario = new Usuario();

            pesquisa.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    for (DataSnapshot dados : dataSnapshot.getChildren()){
                        Usuario usuarioRecebido = dados.getValue(Usuario.class);
                        usuario = usuarioRecebido;
                        Toast.makeText(context, usuarioRecebido.getUsuario(), Toast.LENGTH_LONG).show();
                        break;
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });

            username.setText(usuario.getUsuario());

        }else{
            int visible = 0;
            tv_notification.setVisibility(visible);
        }

        return view;
    }
}
