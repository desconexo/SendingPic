package com.sendingpic.app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.sendingpic.app.R;
import com.sendingpic.app.fragment.PerfilFragment;
import com.sendingpic.app.fragment.HomeFragment;

/**
 * Created by feeri on 12/03/2018.
 */

public class TabAdapter extends FragmentStatePagerAdapter {

    private Context context;
    //private String[] tituloAbas = new String[]{"TIME LINE", "OPÇÕES", "PERFIL"};
    private int[] icones = new int[]{R.drawable.ic_action_home, R.drawable.ic_person};
    private int tamanhoIcone;

    public TabAdapter(FragmentManager fm, Context c) {
        super(fm);
        this.context = c;
        double escala = this.context.getResources().getDisplayMetrics().density;
        tamanhoIcone = (int) (28 * escala);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new PerfilFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return icones.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        Drawable drawable = ContextCompat.getDrawable(this.context, icones[position]);
        drawable.setBounds(0, 0, tamanhoIcone, tamanhoIcone);

        ImageSpan imageSpan = new ImageSpan(drawable);

        SpannableString spannableString = new SpannableString(" ");
        spannableString.setSpan(imageSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }
}
