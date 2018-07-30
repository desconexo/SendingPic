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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sendingpic.app.R;
import com.sendingpic.app.adapter.SeguindoAdapter;
import com.sendingpic.app.config.ConfiguracaoFirebase;
import com.sendingpic.app.model.Seguindo;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Seguindo> seguidores;

    private DatabaseReference mDatabase;
    private ValueEventListener eventListenerSeguidores;

    private TextView tv_no_followers;

    private FirebaseAuth mAuth;

    public HomeFragment(){

    }

    @Override
    public void onStart() {
        super.onStart();
        mDatabase.addValueEventListener(eventListenerSeguidores);
    }

    @Override
    public void onStop() {
        super.onStop();
        mDatabase.removeEventListener(eventListenerSeguidores);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        seguidores = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        listView = view.findViewById(R.id.lv_followers);
        tv_no_followers = view.findViewById(R.id.tv_no_followers);

        adapter = new SeguindoAdapter(getActivity(), seguidores, tv_no_followers);
        listView.setAdapter(adapter);

        mAuth = ConfiguracaoFirebase.getFirebaseAuth();

        mDatabase = ConfiguracaoFirebase.getFirebase().child("seguidor").child(mAuth.getUid());

        eventListenerSeguidores = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                seguidores.clear();

                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    Seguindo seguindo = dados.getValue(Seguindo.class);
                    seguidores.add(seguindo);
                    //Toast.makeText(getActivity(), seguindo.getIdConta() + " / " + seguindo.getIdContaSeguidor(), Toast.LENGTH_LONG).show();
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
