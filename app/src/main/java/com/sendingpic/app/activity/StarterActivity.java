package com.sendingpic.app.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jgabrielfreitas.core.BlurImageView;

import com.sendingpic.app.R;
import com.sendingpic.app.adapter.TabAdapter;
import com.sendingpic.app.config.ConfiguracaoFirebase;
import com.sendingpic.app.helper.SlidingTabLayout;


public class StarterActivity extends AppCompatActivity {

    private BlurImageView blurImageView;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);

        blurImageView = findViewById(R.id.blurImage);
        blurImageView.setBlur(10);

        mAuth = ConfiguracaoFirebase.getFirebaseAuth();
        mStorage = FirebaseStorage.getInstance().getReference();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.vp_pagina);
        slidingTabLayout = findViewById(R.id.stl_tabs);

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(tabAdapter);
        slidingTabLayout.setCustomTabView(R.layout.tab_view, R.id.text_item_tab);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.color_menu_padrao));
        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_starter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_pesquisa:
                return true;
            case R.id.item_galeria:
                startActivity(new Intent(StarterActivity.this, NewPhotoActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
