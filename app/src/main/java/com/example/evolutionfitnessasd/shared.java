package com.example.evolutionfitnessasd;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

public class shared {
    private static String NOME;
    private static String COGNOME;

    public static String getNOME() {
        return NOME;
    }

    public static void setNOME(String nome) {
        NOME = nome;
    }

    public static String getCOGNOME() {
        return COGNOME;
    }

    public static void setCOGNOME(String cognome) {
        COGNOME = cognome;
    }



}
