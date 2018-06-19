package com.sendingpic.app.helper;

import android.util.Base64;

/**
 * Created by feeri on 12/03/2018.
 */

public class Base64Custom {

    public static String codificar(String texto){
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    public static String decodificar(String texto){
        return new String(Base64.decode(texto, Base64.DEFAULT));
    }
}
