package com.sendingpic.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sendingpic.app.R;
import com.sendingpic.app.adapter.NotificacaoAdapter;
import com.sendingpic.app.config.ConfiguracaoFirebase;
import com.sendingpic.app.model.Notificacao;

import java.util.ArrayList;


public class OptionsFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Notificacao> notificacoes;

    private DatabaseReference mDatabase;
    private ValueEventListener eventListenerNotificacoes;

    private TextView tv_notification;

    FirebaseAuth mAuth;

    public OptionsFragment(){

    }

    @Override
    public void onStart() {
        super.onStart();
        mDatabase.addValueEventListener(eventListenerNotificacoes);
    }

    @Override
    public void onStop() {
        super.onStop();
        mDatabase.removeEventListener(eventListenerNotificacoes);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        notificacoes = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_options, container, false);

        listView = view.findViewById(R.id.lv_notification);

        tv_notification = view.findViewById(R.id.tv_notification);
        adapter = new NotificacaoAdapter(getActivity(), notificacoes, tv_notification);

        listView.setAdapter(adapter);

        mAuth = ConfiguracaoFirebase.getFirebaseAuth();

        mDatabase = ConfiguracaoFirebase.getFirebase().child("notificacoes").child(mAuth.getUid());

        eventListenerNotificacoes = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notificacoes.clear();

                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    Notificacao notificacao = dados.getValue(Notificacao.class);
                    notificacoes.add(notificacao);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };









        return view;
    }
}
