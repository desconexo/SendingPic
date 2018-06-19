package com.sendingpic.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sendingpic.app.R;
import com.sendingpic.app.config.ConfiguracaoFirebase;
import com.sendingpic.app.model.Notificacao;
import com.sendingpic.app.model.Usuario;

import java.util.ArrayList;

public class UsuarioNotificacaoAdapter extends ArrayAdapter<Usuario> {

    private ArrayList<Usuario> usuarios;
    private ArrayList<Notificacao> notificacoes;
    private Context context;
    private Usuario usuario;
    private View view;

    private DatabaseReference mDatabase;

    public UsuarioNotificacaoAdapter(Context context, ArrayList<Notificacao> listaNotificacoes, ArrayList<Usuario> listaUsuarios, View view){
        super(context, 0, listaUsuarios);
        this.usuarios = listaUsuarios;
        this.notificacoes = listaNotificacoes;
        this.context = context;
        this.view = view;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        view = this.view;





        return view;
    }
}
